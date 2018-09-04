package de.hybris.platform.travelrulesengine.converters.populator;

import de.hybris.platform.commercefacades.accommodation.AccommodationReservationData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.travelrulesengine.rao.AccommodationReservationRAO;
import de.hybris.platform.travelrulesengine.utils.TravelRuleUtils;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Required;


/**
 * The type Accommodation reservation search date rao populator.
 */
public class AccommodationReservationSearchDateRaoPopulator
		implements Populator<AccommodationReservationData, AccommodationReservationRAO>
{
	private TimeService timeService;

	@Override
	public void populate(final AccommodationReservationData source, final AccommodationReservationRAO target)
			throws ConversionException
	{
		target.setSearchDate(TravelRuleUtils.setDateToMidnight(getTimeService().getCurrentTime()));
	}

	/**
	 * Gets time service.
	 *
	 * @return the time service
	 */
	protected TimeService getTimeService()
	{
		return timeService;
	}

	/**
	 * Sets time service.
	 *
	 * @param timeService
	 * 		the time service
	 */
	@Required
	public void setTimeService(final TimeService timeService)
	{
		this.timeService = timeService;
	}
}
