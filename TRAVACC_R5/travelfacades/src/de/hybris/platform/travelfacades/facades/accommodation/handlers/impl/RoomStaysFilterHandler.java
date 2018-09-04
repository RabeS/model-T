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
import de.hybris.platform.commercefacades.accommodation.RoomStayData;
import de.hybris.platform.commercefacades.accommodation.RoomTypeData;
import de.hybris.platform.travelfacades.facades.accommodation.handlers.AccommodationDetailsHandler;
import de.hybris.platform.travelrulesengine.services.TravelRulesService;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.spockframework.util.CollectionUtil;
import org.springframework.beans.factory.annotation.Required;


/**
 * The type Room stays filter handler.
 */
public class RoomStaysFilterHandler implements AccommodationDetailsHandler
{
	private TravelRulesService travelRulesService;

	@Override
	public void handle(final AccommodationAvailabilityRequestData availabilityRequestData,
			final AccommodationAvailabilityResponseData accommodationAvailabilityResponseData)
	{
		showAccommodationCategories(availabilityRequestData, accommodationAvailabilityResponseData.getRoomStays());
		showAccommodations(availabilityRequestData, accommodationAvailabilityResponseData.getRoomStays());
	}

	/**
	 * Show accommodation categories.
	 *
	 * @param availabilityRequestData
	 * 		the availability request data
	 * @param roomStays
	 * 		the room stays
	 */
	protected void showAccommodationCategories(final AccommodationAvailabilityRequestData availabilityRequestData,
			final List<RoomStayData> roomStays)
	{
		final List<String> returnedRoomStayCategory = getTravelRulesService().showAccommodationCategories(availabilityRequestData);
		discardAccommodationCategories(returnedRoomStayCategory, roomStays);
	}

	/**
	 * Discard accommodation categories that are not in the returnedRoomStayCategory list.
	 * The accommodationCategoryData code is checked against the returnedRoomStayCategory.
	 *
	 * @param returnedRoomStayCategory
	 * 		the returned room stay category
	 * @param roomStays
	 * 		the room stays
	 */
	protected void discardAccommodationCategories(final List<String> returnedRoomStayCategory, final List<RoomStayData> roomStays)
	{
		final List<RoomStayData> roomStaysToRemove = new LinkedList<>();

		for (final RoomStayData roomStayData : roomStays)
		{
			for (final RoomTypeData roomType : roomStayData.getRoomTypes())
			{
				final boolean filterResult =
						CollectionUtils.isNotEmpty(roomType.getAccommodationCategories()) && roomType.getAccommodationCategories()
								.stream().anyMatch(category -> BooleanUtils.isFalse(category.isIgnoreRules()) &&
										!returnedRoomStayCategory.contains(category.getCode()));

				if (filterResult)
				{
					roomStaysToRemove.add(roomStayData);
					break;
				}
			}
		}
		roomStays.removeAll(roomStaysToRemove);
	}

	/**
	 * Show accommodations.
	 *
	 * @param availabilityRequestData
	 * 		the availability request data
	 * @param roomStays
	 * 		the room stays
	 */
	protected void showAccommodations(final AccommodationAvailabilityRequestData availabilityRequestData,
			final List<RoomStayData> roomStays)
	{
		final List<String> returnedAccommodations = roomStays.stream()
				.flatMap(roomStayData -> getTravelRulesService().showAccommodations(availabilityRequestData, roomStayData).stream())
				.collect(Collectors.toList());
		final List<String> distinctReturnedAccommodations = returnedAccommodations.stream().distinct().collect(Collectors.toList());

		discardAccommodations(distinctReturnedAccommodations, roomStays);
	}

	/**
	 * Discard accommodations.
	 *
	 * @param returnedAccommodations
	 * 		the returned accommodations
	 * @param roomStays
	 * 		the room stays
	 */
	protected void discardAccommodations(final List<String> returnedAccommodations, List<RoomStayData> roomStays)
	{
		roomStays.removeIf(
				roomStayData -> !roomStayData.isIgnoreRules() && !returnedAccommodations.contains(roomStayData.getCode()));
	}

	/**
	 * Gets travel rules service.
	 *
	 * @return the travel rules service
	 */
	protected TravelRulesService getTravelRulesService()
	{
		return travelRulesService;
	}

	/**
	 * Sets travel rules service.
	 *
	 * @param travelRulesService
	 * 		the travel rules service
	 */
	@Required
	public void setTravelRulesService(final TravelRulesService travelRulesService)
	{
		this.travelRulesService = travelRulesService;
	}
}
