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

import de.hybris.platform.commercefacades.travel.OriginDestinationOptionData;
import de.hybris.platform.webservicescommons.mapping.WsDTOMapping;
import de.hybris.travelcommercewebservicescommons.dto.OriginDestinationOptionWsDTO;
import de.hybris.travelcommercewebservicescommons.dto.TransportOfferingWsDTO;

import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;


/**
 * Converter for {@link OriginDestinationOptionData}
 */
@WsDTOMapping
public class OriginDestinationOptionWsConverter extends CustomConverter<OriginDestinationOptionData, OriginDestinationOptionWsDTO>
{
	@Override
	public OriginDestinationOptionWsDTO convert(final OriginDestinationOptionData source,
			final Type<? extends OriginDestinationOptionWsDTO> type, final MappingContext mappingContext)
	{
		final OriginDestinationOptionWsDTO originDestinationOptionWsDTO = new OriginDestinationOptionWsDTO();
		if (Objects.isNull(source))
		{
			return originDestinationOptionWsDTO;
		}
		originDestinationOptionWsDTO.setActive(source.isActive());
		originDestinationOptionWsDTO.setOriginDestinationRefNumber(source.getOriginDestinationRefNumber());
		originDestinationOptionWsDTO.setTravelRouteCode(source.getTravelRouteCode());

		if (CollectionUtils.isNotEmpty(source.getTransportOfferings()))
		{
			originDestinationOptionWsDTO
					.setTransportOfferings(mapperFacade.mapAsList(source.getTransportOfferings(), TransportOfferingWsDTO.class));
		}

		return originDestinationOptionWsDTO;
	}
}
