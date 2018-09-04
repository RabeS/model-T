/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */

package de.hybris.platform.travelfacades.facades.accommodation.handlers.impl;

import de.hybris.platform.commercefacades.accommodation.AccommodationAvailabilityRequestData;
import de.hybris.platform.commercefacades.accommodation.AccommodationAvailabilityResponseData;
import de.hybris.platform.commercefacades.accommodation.search.RoomStayCandidateData;
import de.hybris.platform.commercefacades.travel.PassengerTypeQuantityData;
import de.hybris.platform.travelservices.model.accommodation.GuestOccupancyModel;
import de.hybris.platform.travelservices.model.accommodation.RatePlanModel;
import de.hybris.platform.travelservices.model.product.AccommodationModel;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;


/**
 * This handler populates the {@link AccommodationAvailabilityResponseData} with the list of {@link
 * de.hybris.platform.commercefacades.accommodation.RatePlanData}***
 */
public class RatePlansHandler extends AbstractRatePlansHandler
{

	@Override
	public void handle(final AccommodationAvailabilityRequestData availabilityRequestData,
			final AccommodationAvailabilityResponseData accommodationAvailabilityResponseData)
	{
		final String accommodationOfferingCode = availabilityRequestData.getCriterion().getAccommodationReference()
				.getAccommodationOfferingCode();
		accommodationAvailabilityResponseData.getRoomStays().forEach(roomStay ->
		{
			final AccommodationModel accommodation = getAccommodationService()
					.getAccommodationForAccommodationOffering(accommodationOfferingCode, roomStay.getRoomTypes().get(0).getCode());

			final List<RatePlanModel> filteredRatePlans = new LinkedList<>();
			filterRatePlansByGuestOccupancies(filteredRatePlans, accommodation.getRatePlan(), availabilityRequestData);

			roomStay.setRatePlans(getRatePlanConverter().convertAll(filteredRatePlans));
			updateGuestOccupancy(roomStay, accommodation);
			updateCancelPenaltiesDescription(accommodation.getRatePlan(), roomStay);
		});
	}

	/**
	 * Filter rate plans by guest occupancies object.
	 *
	 * @param filteredRatePlans
	 * 		the filtered rate plans
	 * @param ratePlans
	 * 		the rate plans
	 * @param availabilityRequestData
	 * 		the availability request data
	 * @return the object
	 */
	protected void filterRatePlansByGuestOccupancies(final List<RatePlanModel> filteredRatePlans,
			final Collection<RatePlanModel> ratePlans, final AccommodationAvailabilityRequestData availabilityRequestData)
	{
		final List<Map<String, Integer>> roomStayCandidatesMaps = availabilityRequestData.getCriterion().getRoomStayCandidates()
				.stream().map(this::getPassengerTypeQuantityMap).collect(Collectors.toList());

		for (final RatePlanModel ratePlan : ratePlans)
		{
			if (CollectionUtils.isEmpty(ratePlan.getGuestOccupancies()))
			{
				filteredRatePlans.add(ratePlan);
				continue;
			}

			checkGuestOccupanciesAgainstRoomStayCandidates(filteredRatePlans, roomStayCandidatesMaps, ratePlan);
		}
	}

	/**
	 * Returns a map for the specified roomStayCandidateData, where the key is the Code of the passenger type and the value is its
	 * quantity.
	 *
	 * @param roomStayCandidateData
	 * 		the room stay candidate data
	 *
	 * @return a map where the key is the Code of the passenger type and the value is its quantity.
	 */
	protected Map<String, Integer> getPassengerTypeQuantityMap(final RoomStayCandidateData roomStayCandidateData)
	{
		return roomStayCandidateData.getPassengerTypeQuantityList().stream()
				.collect(Collectors.toMap(ptc -> ptc.getPassengerType().getCode(), PassengerTypeQuantityData::getQuantity));
	}

	/**
	 * Check guest occupancies against room stay candidates.
	 *
	 * @param filteredRatePlans
	 * 		the filtered rate plans
	 * @param roomStayCandidatesMaps
	 * 		the room stay candidates maps
	 * @param ratePlan
	 * 		the rate plan
	 */
	protected void checkGuestOccupanciesAgainstRoomStayCandidates(final List<RatePlanModel> filteredRatePlans,
			final List<Map<String, Integer>> roomStayCandidatesMaps, final RatePlanModel ratePlan)
	{
		for (final Map<String, Integer> roomStayCandidatesMap : roomStayCandidatesMaps)
		{
			boolean validRatePlan = true;
			for (final GuestOccupancyModel guestOccupancy : ratePlan.getGuestOccupancies())
			{
				final Integer candidateQuantity = roomStayCandidatesMap.get(guestOccupancy.getPassengerType().getCode());
				if (guestOccupancy.getQuantityMin() > candidateQuantity || guestOccupancy.getQuantityMax() < candidateQuantity)
				{
					validRatePlan = false;
					break;
				}
			}

			if(validRatePlan)
			{
				filteredRatePlans.add(ratePlan);
				break;
			}
		}
	}
}
