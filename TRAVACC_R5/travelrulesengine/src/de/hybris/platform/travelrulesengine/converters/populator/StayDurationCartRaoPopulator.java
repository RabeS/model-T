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
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.travelservices.model.order.AccommodationOrderEntryGroupModel;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;


/**
 * The type Stay duration cart rao populator.
 */
public class StayDurationCartRaoPopulator implements Populator<AbstractOrderModel, CartRAO>
{

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

		if(!endDate.isPresent() || !startDate.isPresent())
		{
			return;
		}

		final long diff = endDate.get().getTime() - startDate.get().getTime();
		target.setStayDuration((int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
	}
}
