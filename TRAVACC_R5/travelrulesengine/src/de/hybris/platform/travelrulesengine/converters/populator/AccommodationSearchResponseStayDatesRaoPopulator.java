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
import de.hybris.platform.commercefacades.accommodation.search.StayDateRangeData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.travelrulesengine.rao.AccommodationSearchResponseRAO;

import java.util.Objects;

import org.apache.commons.lang3.time.DateUtils;


/**
 * The type Accommodation search response stay dates rao populator.
 */
public class AccommodationSearchResponseStayDatesRaoPopulator
		implements Populator<AccommodationSearchResponseData, AccommodationSearchResponseRAO>
{
	@Override
	public void populate(final AccommodationSearchResponseData source, final AccommodationSearchResponseRAO target)
			throws ConversionException
	{
		final StayDateRangeData stayDateRange = source.getCriterion().getStayDateRange();
		if (Objects.nonNull(stayDateRange))
		{
			target.setCheckInDate(stayDateRange.getStartTime());
			target.setCheckOutDate(DateUtils.addDays(stayDateRange.getEndTime(), -1));
		}
	}
}
