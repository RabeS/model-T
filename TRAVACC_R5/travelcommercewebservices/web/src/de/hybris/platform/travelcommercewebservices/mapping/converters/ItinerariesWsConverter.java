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

import de.hybris.platform.commercefacades.travel.ItineraryData;
import de.hybris.platform.webservicescommons.mapping.WsDTOMapping;
import de.hybris.travelcommercewebservicescommons.dto.ItineraryWsDTO;
import de.hybris.travelcommercewebservicescommons.dto.OriginDestinationOptionWsDTO;
import de.hybris.travelcommercewebservicescommons.dto.TravelRouteWsDTO;
import de.hybris.travelcommercewebservicescommons.dto.TravellerWsDTO;
import de.hybris.travelcommercewebservicescommons.enums.TripTypeWsDTO;

import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;


/**
 * Converter for {@link ItineraryData}
 */
@WsDTOMapping
public class ItinerariesWsConverter extends CustomConverter<ItineraryData, ItineraryWsDTO>
{
	@Override
	public ItineraryWsDTO convert(final ItineraryData source, final Type<? extends ItineraryWsDTO> destinationType,
			final MappingContext mappingContext)
	{
		final ItineraryWsDTO itineraryWsDTO = new ItineraryWsDTO();
		if (Objects.nonNull(source))
		{
			if (Objects.nonNull(source.getTripType()))
			{
				itineraryWsDTO.setTripType(mapperFacade.map(source.getTripType(), TripTypeWsDTO.class));
			}
			if (Objects.nonNull(source.getRoute()))
			{
				itineraryWsDTO.setRoute(mapperFacade.map(source.getRoute(), TravelRouteWsDTO.class));
			}
			itineraryWsDTO.setDuration(source.getDuration());

			if (CollectionUtils.isNotEmpty(source.getTravellers()))
			{
				itineraryWsDTO.setTravellers(mapperFacade.mapAsList(source.getTravellers(), TravellerWsDTO.class));
			}
			if (CollectionUtils.isNotEmpty(source.getOriginDestinationOptions()))
			{
				itineraryWsDTO.setOriginDestinationOptions(
						mapperFacade.mapAsList(source.getOriginDestinationOptions(), OriginDestinationOptionWsDTO.class));
			}
		}
		return itineraryWsDTO;
	}
}
