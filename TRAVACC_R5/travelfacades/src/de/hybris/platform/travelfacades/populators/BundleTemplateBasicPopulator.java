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

package de.hybris.platform.travelfacades.populators;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.commercefacades.travel.TravelBundleTemplateData;
import de.hybris.platform.configurablebundlefacades.data.BundleTemplateData;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.Objects;


public class BundleTemplateBasicPopulator<SOURCE extends BundleTemplateModel, TARGET extends BundleTemplateData>
		implements Populator<SOURCE, TARGET>
{

	@Override
	public void populate(final BundleTemplateModel source, final BundleTemplateData target) throws ConversionException
	{
		validateParameterNotNullStandardMessage("target", target);
		validateParameterNotNullStandardMessage("source", source);

		target.setId(source.getId());
		target.setName(source.getName());

		if (!(target instanceof TravelBundleTemplateData))
		{
			return;
		}
		if (Objects.nonNull(source.getType()))
		{
			final TravelBundleTemplateData travelBundleTemplateData = (TravelBundleTemplateData) target;
			travelBundleTemplateData.setBundleType(source.getType().getCode());
		}
	}
}
