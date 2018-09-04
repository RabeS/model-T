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
import de.hybris.platform.commercefacades.accommodation.RoomStayData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.travelrulesengine.rao.RoomStayRAO;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;


/**
 * This class populates the basic information for the RoomStayRAO
 */
public class RoomStayBasicRaoPopulator implements Populator<RoomStayData, RoomStayRAO>
{
	@Override
	public void populate(final RoomStayData source, final RoomStayRAO target) throws ConversionException
	{
		target.setRoomStayRefNumber(source.getRoomStayRefNumber());
		target.setCode(source.getRoomTypes().get(0).getCode());

		final List<String> categoryCodes = source.getRoomTypes().stream()
				.filter(roomTypeData -> CollectionUtils.isNotEmpty(roomTypeData.getAccommodationCategories()))
				.flatMap(roomTypeData -> roomTypeData.getAccommodationCategories().stream())
				.map(AccommodationCategoryData::getCode).collect(Collectors.toList());

		target.setAccommodationCategories(categoryCodes);
	}
}
