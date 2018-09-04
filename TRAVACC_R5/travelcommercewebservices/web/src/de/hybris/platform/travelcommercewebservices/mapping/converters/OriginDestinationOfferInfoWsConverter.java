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

import de.hybris.platform.commercefacades.travel.ancillary.data.OriginDestinationOfferInfoData;
import de.hybris.travelcommercewebservicescommons.dto.OfferPricingInfoWsDTO;
import de.hybris.travelcommercewebservicescommons.dto.OriginDestinationOfferInfoWsDTO;
import de.hybris.travelcommercewebservicescommons.dto.TransportOfferingWsDTO;

import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;


/**
 * Converter for {@link OriginDestinationOfferInfoData}
 */

public class OriginDestinationOfferInfoWsConverter
		extends CustomConverter<OriginDestinationOfferInfoData, OriginDestinationOfferInfoWsDTO>
{
	@Override
	public OriginDestinationOfferInfoWsDTO convert(final OriginDestinationOfferInfoData source,
			final Type<? extends OriginDestinationOfferInfoWsDTO> type, final MappingContext mappingContext)
	{
		final OriginDestinationOfferInfoWsDTO originDestinationOfferInfoWsDTO = new OriginDestinationOfferInfoWsDTO();
		if (Objects.isNull(source))
		{
			return originDestinationOfferInfoWsDTO;
		}
		originDestinationOfferInfoWsDTO.setTravelRouteCode(source.getTravelRouteCode());
		originDestinationOfferInfoWsDTO.setOriginDestinationRefNumber(source.getOriginDestinationRefNumber());

		if (CollectionUtils.isNotEmpty(source.getTransportOfferings()))
		{
			originDestinationOfferInfoWsDTO
					.setTransportOfferings(mapperFacade.mapAsList(source.getTransportOfferings(), TransportOfferingWsDTO.class));
		}
		if (CollectionUtils.isNotEmpty(source.getOfferPricingInfos()))
		{
			originDestinationOfferInfoWsDTO
					.setOfferPricingInfos(mapperFacade.mapAsList(source.getOfferPricingInfos(), OfferPricingInfoWsDTO.class));
		}

		return originDestinationOfferInfoWsDTO;
	}
}
