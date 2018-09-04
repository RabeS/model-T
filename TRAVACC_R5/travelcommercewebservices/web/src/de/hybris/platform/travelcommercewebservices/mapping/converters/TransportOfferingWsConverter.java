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

import de.hybris.platform.commercefacades.travel.TransportFacilityData;
import de.hybris.platform.commercefacades.travel.TransportOfferingData;
import de.hybris.platform.webservicescommons.mapping.WsDTOMapping;
import de.hybris.travelcommercewebservicescommons.dto.TerminalWsDTO;
import de.hybris.travelcommercewebservicescommons.dto.TransportFacilityWsDTO;
import de.hybris.travelcommercewebservicescommons.dto.TransportOfferingWsDTO;
import de.hybris.travelcommercewebservicescommons.dto.TransportVehicleWsDTO;
import de.hybris.travelcommercewebservicescommons.dto.TravelProviderWsDTO;
import de.hybris.travelcommercewebservicescommons.dto.TravelSectorWsDTO;

import java.util.List;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;


/**
 * Converter for {@link TransportOfferingData}
 */
@WsDTOMapping
public class TransportOfferingWsConverter extends CustomConverter<TransportOfferingData, TransportOfferingWsDTO>
{
	@Override
	public TransportOfferingWsDTO convert(final TransportOfferingData transportOfferingData,
			final Type<? extends TransportOfferingWsDTO> type, final MappingContext mappingContext)
	{
		final TransportOfferingWsDTO transportOfferingWsDTO = new TransportOfferingWsDTO();
		if (Objects.isNull(transportOfferingData))
		{
			return transportOfferingWsDTO;
		}
		transportOfferingWsDTO.setCode(transportOfferingData.getCode());
		transportOfferingWsDTO.setNumber(transportOfferingData.getNumber());
		transportOfferingWsDTO.setDepartureTime(transportOfferingData.getDepartureTime());
		transportOfferingWsDTO.setDepartureTimeZoneId(transportOfferingData.getDepartureTimeZoneId());
		transportOfferingWsDTO.setArrivalTime(transportOfferingData.getArrivalTime());
		transportOfferingWsDTO.setArrivalTimeZoneId(transportOfferingData.getArrivalTimeZoneId());
		transportOfferingWsDTO.setUpdatedDepartureTime(transportOfferingData.getUpdatedDepartureTime());
		transportOfferingWsDTO.setDurationValue(transportOfferingData.getDurationValue());
		transportOfferingWsDTO.setDuration(transportOfferingData.getDuration());
		transportOfferingWsDTO.setStatus(transportOfferingData.getStatus());
		transportOfferingWsDTO.setType(transportOfferingData.getType());
		transportOfferingWsDTO.setOriginLocationCity(transportOfferingData.getOriginLocationCity());
		transportOfferingWsDTO.setDestinationLocationCity(transportOfferingData.getDestinationLocationCity());
		transportOfferingWsDTO.setOriginLocationCountry(transportOfferingData.getOriginLocationCountry());
		transportOfferingWsDTO.setDestinationLocationCountry(transportOfferingData.getDestinationLocationCountry());
		transportOfferingWsDTO.setActive(transportOfferingData.isActive());

		transportOfferingWsDTO.setOriginTerminal(mapperFacade.map(transportOfferingData.getOriginTerminal(), TerminalWsDTO.class));
		transportOfferingWsDTO
				.setDestinationTerminal(mapperFacade.map(transportOfferingData.getDestinationTerminal(), TerminalWsDTO.class));
		transportOfferingWsDTO.setSector(mapperFacade.map(transportOfferingData.getSector(), TravelSectorWsDTO.class));
		transportOfferingWsDTO
				.setTravelProvider(mapperFacade.map(transportOfferingData.getTravelProvider(), TravelProviderWsDTO.class));
		transportOfferingWsDTO
				.setTransportVehicle(mapperFacade.map(transportOfferingData.getTransportVehicle(), TransportVehicleWsDTO.class));

		final List<TransportFacilityData> listTransportStopLocations = transportOfferingData.getStopLocations();
		if (CollectionUtils.isNotEmpty(listTransportStopLocations))
		{
			transportOfferingWsDTO
					.setStopLocations(mapperFacade.mapAsList(listTransportStopLocations, TransportFacilityWsDTO.class));
		}
		return transportOfferingWsDTO;
	}
}
