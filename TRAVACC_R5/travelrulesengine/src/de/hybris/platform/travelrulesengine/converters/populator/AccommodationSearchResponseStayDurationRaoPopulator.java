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

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


/**
 * The type Accommodation search response stay duration rao populator.
 */
public class AccommodationSearchResponseStayDurationRaoPopulator
		implements Populator<AccommodationSearchResponseData, AccommodationSearchResponseRAO>
{
	@Override
	public void populate(final AccommodationSearchResponseData source, final AccommodationSearchResponseRAO target)
			throws ConversionException
	{
		final StayDateRangeData stayDateRange = source.getCriterion().getStayDateRange();
		if (Objects.nonNull(stayDateRange))
		{
			final Long diff = stayDateRange.getEndTime().getTime() - stayDateRange.getStartTime().getTime();
			target.setStayDuration((int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
		}

	}
}
