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
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Required;


public class BundleTemplateChildTemplatesPopulator<SOURCE extends BundleTemplateModel, TARGET extends BundleTemplateData>
		implements Populator<SOURCE, TARGET>
{
	private Converter<BundleTemplateModel, TravelBundleTemplateData> travelBundleDataTemplateConverter;

	@Override
	public void populate(final BundleTemplateModel source, final BundleTemplateData target) throws ConversionException
	{
		validateParameterNotNullStandardMessage("target", target);
		validateParameterNotNullStandardMessage("source", source);

		target.setBundleTemplates(source.getChildTemplates().stream().map(getTravelBundleDataTemplateConverter()::convert)
				.collect(Collectors.toList()));

	}

	/**
	 * @return
	 */
	protected Converter<BundleTemplateModel, TravelBundleTemplateData> getTravelBundleDataTemplateConverter()
	{
		return travelBundleDataTemplateConverter;
	}

	/**
	 * @param travelBundleDataTemplateConverter
	 */
	@Required
	public void setTravelBundleDataTemplateConverter(
			final Converter<BundleTemplateModel, TravelBundleTemplateData> travelBundleDataTemplateConverter)
	{
		this.travelBundleDataTemplateConverter = travelBundleDataTemplateConverter;
	}
	
	
}
