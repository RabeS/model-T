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

import de.hybris.platform.commercefacades.accommodation.AccommodationSearchResponseData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.travelrulesengine.rao.AccommodationSearchResponseRAO;
import de.hybris.platform.travelrulesengine.utils.TravelRuleUtils;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Required;


/**
 * The type Accommodation search response search date rao populator.
 */
public class AccommodationSearchResponseSearchDateRaoPopulator
		implements Populator<AccommodationSearchResponseData, AccommodationSearchResponseRAO>
{
	private TimeService timeService;

	@Override
	public void populate(final AccommodationSearchResponseData source, final AccommodationSearchResponseRAO target)
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
