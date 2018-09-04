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
import de.hybris.platform.commercefacades.accommodation.PropertyData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.travelrulesengine.rao.AccommodationOfferingRAO;
import de.hybris.platform.travelrulesengine.rao.AccommodationSearchResponseRAO;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;


/**
 * The type Accommodation search response populator
 */
public class AccommodationSearchResponseAccommodationOfferingRaoPopulator
		implements Populator<AccommodationSearchResponseData, AccommodationSearchResponseRAO>
{
	@Override
	public void populate(final AccommodationSearchResponseData source, final AccommodationSearchResponseRAO target)
			throws ConversionException
	{
		if (Objects.isNull(source))
		{
			return;
		}

		final AccommodationOfferingRAO accommodationOfferingRAO = new AccommodationOfferingRAO();
		if (CollectionUtils.isNotEmpty(source.getProperties()))
		{
			for (final PropertyData property : source.getProperties())
			{
				if (CollectionUtils.isNotEmpty(property.getAccommodationOfferingTypes()))
				{
					final Set<String> accommodationOfferingTypes = new HashSet<>(property.getAccommodationOfferingTypes());
					accommodationOfferingRAO.setAccommodationOfferingTypes(accommodationOfferingTypes);
				}
			}
		}
		target.setAccommodationOffering(accommodationOfferingRAO);
	}

}
