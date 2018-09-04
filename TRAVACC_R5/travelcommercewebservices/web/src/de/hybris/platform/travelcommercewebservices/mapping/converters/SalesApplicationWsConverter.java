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

import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.webservicescommons.mapping.WsDTOMapping;
import de.hybris.travelcommercewebservicescommons.dto.SalesApplicationWsDTO;

import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;


/**
 * Converter for {@link SalesApplicationWsDTO}
 */
@WsDTOMapping
public class SalesApplicationWsConverter extends CustomConverter<SalesApplicationWsDTO, SalesApplication>
{
	@Override
	public SalesApplication convert(final SalesApplicationWsDTO source, final Type<? extends SalesApplication> destinationType,
			final MappingContext mappingContext)
	{
		return SalesApplication.valueOf(source.getCode());
	}
}
