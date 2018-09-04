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
package de.hybris.platform.travelcommercewebservices.mapping.converters;

import de.hybris.platform.commercefacades.travel.TravelBundleTemplateData;
import de.hybris.platform.commercewebservicescommons.dto.product.ProductWsDTO;
import de.hybris.platform.webservicescommons.mapping.WsDTOMapping;
import de.hybris.travelcommercewebservicescommons.dto.TravelBundleTemplateWsDTO;

import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;


/**
 * Converter for {@link TravelBundleTemplateData}
 */
@WsDTOMapping
public class TravelBundleTemplateWsConverter extends CustomConverter<TravelBundleTemplateData, TravelBundleTemplateWsDTO>
{
	@Override
	public TravelBundleTemplateWsDTO convert(final TravelBundleTemplateData travelBundleTemplateData,
			final Type<? extends TravelBundleTemplateWsDTO> type, final MappingContext mappingContext)
	{
		final TravelBundleTemplateWsDTO travelBundleTemplateWsDTO = new TravelBundleTemplateWsDTO();
		if (Objects.isNull(travelBundleTemplateData))
		{
			return travelBundleTemplateWsDTO;
		}
		travelBundleTemplateWsDTO.setId(travelBundleTemplateData.getId());
		travelBundleTemplateWsDTO.setName(travelBundleTemplateData.getName());
		travelBundleTemplateWsDTO.setBundleType(travelBundleTemplateData.getBundleType());

		if (CollectionUtils.isNotEmpty(travelBundleTemplateData.getProducts()))
		{
			travelBundleTemplateWsDTO
					.setProducts(mapperFacade.mapAsList(travelBundleTemplateData.getProducts(), ProductWsDTO.class));
		}
		if (CollectionUtils.isNotEmpty(travelBundleTemplateData.getBundleTemplates()))
		{
			travelBundleTemplateWsDTO.setBundleTemplates(
					mapperFacade.mapAsList(travelBundleTemplateData.getBundleTemplates(), TravelBundleTemplateWsDTO.class));
		}

		return travelBundleTemplateWsDTO;
	}

}