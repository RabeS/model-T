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
package de.hybris.platform.travelfacades.facades.accommodation.populators;

import de.hybris.platform.commercefacades.accommodation.PropertyData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.travelservices.model.warehouse.AccommodationOfferingModel;

import java.util.Objects;


/**
 * The type Accommodation provider populator.
 *
 * @param <SOURCE>
 * 		the type parameter
 * @param <TARGET>
 * 		the type parameter
 */
public class AccommodationOfferingProviderPopulator<SOURCE extends AccommodationOfferingModel, TARGET extends PropertyData>
		implements Populator<SOURCE, TARGET>
{
	@Override
	public void populate(final SOURCE source, final TARGET target) throws ConversionException
	{
		if (Objects.nonNull(source.getProvider()))
		{
			target.setBrandName(source.getProvider().getName());
			target.setBrandCode(source.getProvider().getCode());
		}
	}
}
