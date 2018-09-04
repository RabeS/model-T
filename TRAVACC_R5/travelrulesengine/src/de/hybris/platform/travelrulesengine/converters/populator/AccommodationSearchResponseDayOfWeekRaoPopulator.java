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
import de.hybris.platform.cronjob.enums.DayOfWeek;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.travelrulesengine.rao.AccommodationSearchResponseRAO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


public class AccommodationSearchResponseDayOfWeekRaoPopulator
		implements Populator<AccommodationSearchResponseData, AccommodationSearchResponseRAO>
{
	private static final long DAYS_IN_WEEK = 7;

	@Override
	public void populate(final AccommodationSearchResponseData source, final AccommodationSearchResponseRAO target)
			throws ConversionException
	{
		final StayDateRangeData stayDateRange = source.getCriterion().getStayDateRange();
		if (Objects.isNull(stayDateRange))
		{
			return;
		}
		final LocalDate localStartDate = stayDateRange.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		final LocalDate localEndDate = stayDateRange.getEndTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		final long days = ChronoUnit.DAYS.between(localStartDate, localEndDate);

		if (days > DAYS_IN_WEEK)
		{
			final Set<String> allDays = Arrays.stream(DayOfWeek.values()).map(DayOfWeek::getCode).collect(Collectors.toSet());
			target.setDaysOfWeek(allDays);
			return;
		}

		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(stayDateRange.getStartTime());
		final Set<String> daysOfWeek = new HashSet<>();
		for (int i = 0; i < days; i++)
		{
			final String dayOfWeekFromEvaluatedDate = LocalDateTime.ofInstant(calendar.toInstant(), ZoneId.systemDefault())
					.getDayOfWeek().toString();
			daysOfWeek.add(dayOfWeekFromEvaluatedDate);

			calendar.add(Calendar.DATE, 1);
		}
		target.setDaysOfWeek(daysOfWeek);

	}
}
