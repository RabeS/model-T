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
import de.hybris.platform.travelrulesengine.rao.AccommodationAvailabilityRequestRAO;

import java.util.Objects;

import org.apache.commons.lang3.time.DateUtils;


/**
 * The type Accommodation availability request stay dates rao populator.
 */
public class AccommodationAvailabilityRequestStayDatesRaoPopulator
		implements Populator<AccommodationAvailabilityRequestData, AccommodationAvailabilityRequestRAO>
{
	@Override
	public void populate(final AccommodationAvailabilityRequestData source, final AccommodationAvailabilityRequestRAO target)
			throws ConversionException
	{
		final StayDateRangeData stayDateRangeData = source.getCriterion().getStayDateRange();
		if (Objects.nonNull(stayDateRangeData))
		{
			target.setCheckInDate(stayDateRangeData.getStartTime());
			target.setCheckOutDate(DateUtils.addDays(stayDateRangeData.getEndTime(), -1));
		}
	}
}
