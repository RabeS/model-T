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

import de.hybris.platform.commercefacades.travel.seatmap.data.SeatMapData;
import de.hybris.platform.commercefacades.travel.seatmap.data.SeatMapResponseData;
import de.hybris.platform.travelcommercewebservices.dto.SeatMapResponseWsDTO;
import de.hybris.platform.travelcommercewebservices.dto.SeatMapWsDTO;
import de.hybris.platform.travelcommercewebservices.dto.SegmentInfoWsDTO;
import de.hybris.platform.webservicescommons.mapping.WsDTOMapping;
import de.hybris.travelcommercewebservicescommons.dto.SeatMapDetailWsDTO;
import de.hybris.travelcommercewebservicescommons.dto.SelectedSeatWsDTO;
import de.hybris.travelcommercewebservicescommons.dto.TransportOfferingWsDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;


/**
 * Converter for {@link SeatMapResponseData}
 */
@WsDTOMapping
public class SeatMapWsConverter extends CustomConverter<SeatMapResponseData, SeatMapResponseWsDTO>
{
	@Override
	public SeatMapResponseWsDTO convert(final SeatMapResponseData source,
			final Type<? extends SeatMapResponseWsDTO> destinationType, final MappingContext mappingContext)
	{
		final SeatMapResponseWsDTO seatMapResponseWsDTO = new SeatMapResponseWsDTO();
		if (Objects.isNull(source))
		{
			return seatMapResponseWsDTO;
		}
		if (CollectionUtils.isNotEmpty(source.getSegmentInfoDatas()))
		{
			seatMapResponseWsDTO.setSegmentInfoDatas(mapperFacade.mapAsList(source.getSegmentInfoDatas(), SegmentInfoWsDTO.class));
		}

		final List<SeatMapWsDTO> listSeatMapWsDTO = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(source.getSeatMap()))
		{
			for (final SeatMapData seatMap : source.getSeatMap())
			{
				final SeatMapWsDTO seatMapWsDTO = new SeatMapWsDTO();
				seatMapWsDTO.setAccommodationMapCode(seatMap.getAccommodationMapCode());
				seatMapWsDTO.setBookingReferenceId(seatMap.getBookingReferenceId());
				seatMapWsDTO.setFareBasisCode(seatMap.getFareBasisCode());

				seatMapWsDTO.setTransportOffering(mapperFacade.map(seatMap.getTransportOffering(), TransportOfferingWsDTO.class));
				if (Objects.nonNull(seatMap.getSeatMapDetail()))
				{
					seatMapWsDTO.setSeatMapDetail(mapperFacade.map(seatMap.getSeatMapDetail(), SeatMapDetailWsDTO.class));
				}
				if (CollectionUtils.isNotEmpty(seatMap.getSelectedSeats()))
				{
					seatMapWsDTO.setSelectedSeats(mapperFacade.mapAsList(seatMap.getSelectedSeats(), SelectedSeatWsDTO.class));
				}
				listSeatMapWsDTO.add(seatMapWsDTO);
			}
			seatMapResponseWsDTO.setSeatMap(listSeatMapWsDTO);
		}

		return seatMapResponseWsDTO;
	}
}
