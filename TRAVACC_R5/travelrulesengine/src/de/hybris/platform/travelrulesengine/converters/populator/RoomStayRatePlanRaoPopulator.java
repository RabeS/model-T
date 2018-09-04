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

import de.hybris.platform.commercefacades.accommodation.RoomStayData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.travelrulesengine.rao.RoomStayRAO;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * The type Room stay rate plan rao populator.
 */
public class RoomStayRatePlanRaoPopulator implements Populator<RoomStayData, RoomStayRAO>
{
	@Override
	public void populate(final RoomStayData source, final RoomStayRAO target) throws ConversionException
	{
		if (Objects.isNull(source))
		{
			return;
		}

		final List<String> ratePlans = source.getRatePlans().stream().map(ratePlanData -> ratePlanData.getCode()).distinct()
				.collect(Collectors.toList());
		target.setRatePlans(ratePlans);
	}
}
