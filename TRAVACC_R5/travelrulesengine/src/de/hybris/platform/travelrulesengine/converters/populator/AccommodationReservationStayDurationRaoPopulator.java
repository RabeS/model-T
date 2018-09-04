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
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.travelrulesengine.rao.AccommodationReservationRAO;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;


/**
 * The type Accommodation reservation stay duration rao populator.
 */
public class AccommodationReservationStayDurationRaoPopulator
		implements Populator<AccommodationReservationData, AccommodationReservationRAO>
{

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

		final Long diff = endDate.get().getTime() - startDate.get().getTime();
		target.setStayDuration((int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
	}

}
