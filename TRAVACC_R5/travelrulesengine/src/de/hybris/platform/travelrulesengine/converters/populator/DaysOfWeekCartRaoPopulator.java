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

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.cronjob.enums.DayOfWeek;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.travelservices.model.order.AccommodationOrderEntryGroupModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;


/**
 * The type Days of week cart rao populator.
 */
public class DaysOfWeekCartRaoPopulator implements Populator<AbstractOrderModel, CartRAO>
{
	private static final long DAYS_IN_WEEK = 7;

	@Override
	public void populate(final AbstractOrderModel source, final CartRAO target) throws ConversionException
	{
		if (Objects.isNull(source) || CollectionUtils.isEmpty(source.getEntries()))
		{
			return;
		}

		final List<AccommodationOrderEntryGroupModel> orderEntryGroups = source.getEntries().stream()
				.filter(entry -> Objects.nonNull(entry.getEntryGroup()))
				.filter(entry -> entry.getEntryGroup() instanceof AccommodationOrderEntryGroupModel)
				.map(entry -> (AccommodationOrderEntryGroupModel) entry.getEntryGroup())
				.collect(Collectors.toList());

		if (CollectionUtils.isEmpty(orderEntryGroups))
		{
			return;
		}

		final Optional<Date> endDate = orderEntryGroups.stream().map(AccommodationOrderEntryGroupModel::getEndingDate)
				.max(Date::compareTo);
		final Optional<Date> startDate = orderEntryGroups.stream().map(AccommodationOrderEntryGroupModel::getStartingDate)
				.min(Date::compareTo);

		if (!endDate.isPresent() || !startDate.isPresent())
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
