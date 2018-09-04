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

import de.hybris.platform.commercefacades.travel.ancillary.data.OfferGroupData;
import de.hybris.platform.webservicescommons.mapping.WsDTOMapping;
import de.hybris.travelcommercewebservicescommons.dto.OfferGroupWsDTO;
import de.hybris.travelcommercewebservicescommons.dto.OfferPricingInfoWsDTO;
import de.hybris.travelcommercewebservicescommons.dto.OriginDestinationOfferInfoWsDTO;
import de.hybris.travelcommercewebservicescommons.dto.TravelRestrictionWsDTO;

import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;


/**
 * Converter for {@link OfferGroupData}
 */
@WsDTOMapping
public class OfferGroupsWsConverter extends CustomConverter<OfferGroupData, OfferGroupWsDTO>
{
	@Override
	public OfferGroupWsDTO convert(final OfferGroupData source, final Type<? extends OfferGroupWsDTO> destinationType,
			final MappingContext mappingContext)
	{
		final OfferGroupWsDTO offerGroupWsDTO = new OfferGroupWsDTO();
		if (Objects.isNull(source))
		{
			return offerGroupWsDTO;
		}
		offerGroupWsDTO.setIgnoreRules(source.isIgnoreRules());

		if (Objects.nonNull(source.getTravelRestriction()))
		{
			offerGroupWsDTO.setTravelRestriction(mapperFacade.map(source.getTravelRestriction(), TravelRestrictionWsDTO.class));
		}
		if (CollectionUtils.isNotEmpty(source.getOfferPricingInfos()))
		{
			offerGroupWsDTO.setOfferPricingInfos(mapperFacade.mapAsList(source.getOfferPricingInfos(), OfferPricingInfoWsDTO
					.class));
		}
		if (CollectionUtils.isNotEmpty(source.getOriginDestinationOfferInfos()))
		{
			offerGroupWsDTO.setOriginDestinationOfferInfos(
					mapperFacade.mapAsList(source.getOriginDestinationOfferInfos(), OriginDestinationOfferInfoWsDTO.class));
		}
		return offerGroupWsDTO;
	}
}
