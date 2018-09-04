package de.hybris.platform.travelfacades.facades.accommodation.handlers.impl;

import de.hybris.platform.commercefacades.accommodation.AccommodationAvailabilityRequestData;
import de.hybris.platform.commercefacades.accommodation.AccommodationAvailabilityResponseData;
import de.hybris.platform.commercefacades.accommodation.RoomStayData;
import de.hybris.platform.travelfacades.facades.accommodation.handlers.AccommodationDetailsHandler;
import de.hybris.platform.travelrulesengine.services.TravelRulesService;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * This handler filters the ratePlans list of the {@link AccommodationAvailabilityResponseData} based on the list of codes coming
 * from the result of the evaluation by the rule engine
 */
public class RatePlansFilterHandler implements AccommodationDetailsHandler
{
	private TravelRulesService travelRulesService;

	@Override
	public void handle(final AccommodationAvailabilityRequestData availabilityRequestData,
			final AccommodationAvailabilityResponseData accommodationAvailabilityResponseData)
	{
		accommodationAvailabilityResponseData.getRoomStays().forEach(roomStayData -> {
			final List<String> ratePlans = getTravelRulesService().showRatePlans(availabilityRequestData, roomStayData);
			discardRatePlans(ratePlans, roomStayData);
		});
	}

	/**
	 * Discards undesired rate plans
	 *
	 * @param ratePlans
	 * 		the rate plans
	 * @param roomStayData
	 * 		the room stay data
	 */
	protected void discardRatePlans(final List<String> ratePlans, final RoomStayData roomStayData)
	{
		roomStayData.getRatePlans().removeIf(ratePlan -> !ratePlan.isIgnoreRules() && !ratePlans.contains(ratePlan.getCode()));
	}

	/**
	 * @return the travelRulesService
	 */
	protected TravelRulesService getTravelRulesService()
	{
		return travelRulesService;
	}

	/**
	 * @param travelRulesService
	 * 		the travelRulesService to set
	 */
	@Required
	public void setTravelRulesService(final TravelRulesService travelRulesService)
	{
		this.travelRulesService = travelRulesService;
	}
}
