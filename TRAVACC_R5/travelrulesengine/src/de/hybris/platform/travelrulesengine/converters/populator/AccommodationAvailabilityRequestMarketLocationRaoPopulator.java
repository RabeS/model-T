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

import de.hybris.platform.commercefacades.accommodation.AccommodationAvailabilityRequestData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.travelrulesengine.rao.AccommodationAvailabilityRequestRAO;

import java.util.Objects;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;


public class AccommodationAvailabilityRequestMarketLocationRaoPopulator extends AbstractAccommodationMarketLocationRaoPopulator
		implements Populator<AccommodationAvailabilityRequestData, AccommodationAvailabilityRequestRAO>
{
	@Override
	public void populate(final AccommodationAvailabilityRequestData source,
			final AccommodationAvailabilityRequestRAO target) throws ConversionException
	{
		if (Objects.isNull(source))
		{
			return;
		}

		final Set<String> marketLocations = getMarketLocations();
		if (CollectionUtils.isNotEmpty(marketLocations))
		{
			target.setMarketLocations(marketLocations);
		}
	}
}
