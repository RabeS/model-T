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

import de.hybris.platform.commercefacades.accommodation.AccommodationReservationData;
import de.hybris.platform.commercefacades.accommodation.ReservedRoomStayData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.travelrulesengine.rao.AccommodationReservationRAO;
import de.hybris.platform.cronjob.enums.DayOfWeek;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;


/**
 * The type Accommodation reservation days of week rao populator.
 */
public class AccommodationReservationDaysOfWeekRaoPopulator
		implements Populator<AccommodationReservationData, AccommodationReservationRAO>
{
	private static final long DAYS_IN_WEEK = 7;

	@Override
	public void populate(final AccommodationReservationData source, final AccommodationReservationRAO target)
			throws ConversionException
	{
		final List<ReservedRoomStayData> reservedRoomStayData = source.getRoomStays().stream().filter(
				reservedRoom -> Objects.nonNull(reservedRoom.getCheckInDate()) && Objects.nonNull(reservedRoom.getCheckOutDate()))
				.collect(Collectors.toList());

		if (CollectionUtils.isEmpty(reservedRoomStayData))
		{
			return;
		}
		final Optional<Date> endDate = reservedRoomStayData.stream().map(ReservedRoomStayData::getCheckOutDate)
				.max(Date::compareTo);

		final Optional<Date> startDate = reservedRoomStayData.stream().map(ReservedRoomStayData::getCheckInDate)
				.min(Date::compareTo);

		if(!endDate.isPresent() || !startDate.isPresent())
		{
			return;
		}

		final LocalDate localStartDate = startDate.get().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		final LocalDate localEndDate = endDate.get().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		final long days = ChronoUnit.DAYS.between(localStartDate, localEndDate);

		if (days >= DAYS_IN_WEEK)
		{
			final Set<String> allDays = Arrays.stream(DayOfWeek.values()).map(DayOfWeek::getCode).collect(Collectors.toSet());
			target.setDaysOfWeek(allDays);
			return;
		}

		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate.get());
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
