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

import de.hybris.platform.commercefacades.accommodation.AccommodationCategoryData;
import de.hybris.platform.commercefacades.accommodation.AccommodationReservationData;
import de.hybris.platform.commercefacades.accommodation.ReservedRoomStayData;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.travelrulesengine.rao.AccommodationRAO;
import de.hybris.platform.travelrulesengine.rao.AccommodationReservationRAO;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;


/**
 * The type Accommodation reservation accommodation rao populator.
 */
public class AccommodationReservationAccommodationRaoPopulator
		implements Populator<AccommodationReservationData, AccommodationReservationRAO>
{
	@Override
	public void populate(final AccommodationReservationData source, final AccommodationReservationRAO target)
			throws ConversionException
	{

		if (Objects.isNull(source))
		{
			return;
		}
		final Set<AccommodationRAO> accommodationRAOs = new HashSet<>();

		for (final ReservedRoomStayData roomStay : source.getRoomStays())
		{
			final AccommodationRAO accommodationRao = new AccommodationRAO();

			final List<String> categoryCodes = roomStay.getRoomTypes().stream()
					.flatMap(roomTypeData -> roomTypeData.getAccommodationCategories().stream())
					.map(AccommodationCategoryData::getCode).collect(Collectors.toList());

			accommodationRao.setAccommodationCategories(categoryCodes);
			accommodationRao.setRatePlan(roomStay.getRatePlans().get(0).getCode());
			accommodationRao.setCode(roomStay.getRoomTypes().get(0).getCode());
			accommodationRAOs.add(accommodationRao);
		}
		target.setAccommodations(accommodationRAOs);
	}

}
