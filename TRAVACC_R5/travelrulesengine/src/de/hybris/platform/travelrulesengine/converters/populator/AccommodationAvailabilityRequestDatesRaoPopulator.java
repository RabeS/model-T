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
package de.hybris.platform.travelrulesengine.converters.populator;

import de.hybris.platform.commercefacades.accommodation.AccommodationAvailabilityRequestData;
import de.hybris.platform.commercefacades.accommodation.search.StayDateRangeData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.travelrulesengine.rao.AccommodationAvailabilityRequestRAO;
import de.hybris.platform.travelrulesengine.utils.TravelRuleUtils;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * This class populates the date fields in the AccommodationAvailabilityRequestRAO
 */
public class AccommodationAvailabilityRequestDatesRaoPopulator
		implements Populator<AccommodationAvailabilityRequestData, AccommodationAvailabilityRequestRAO>
{
	private TimeService timeService;

	@Override
	public void populate(final AccommodationAvailabilityRequestData source, final AccommodationAvailabilityRequestRAO target)
			throws ConversionException
	{
		final StayDateRangeData stayDateRange = source.getCriterion().getStayDateRange();
		target.setCheckInDate(stayDateRange.getStartTime());
		target.setCheckOutDate(DateUtils.addDays(stayDateRange.getEndTime(), -1));

		final Long diff = stayDateRange.getEndTime().getTime() - stayDateRange.getStartTime().getTime();
		target.setStayDuration((int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
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
