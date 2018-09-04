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

import de.hybris.platform.commercefacades.travel.ItineraryPricingInfoData;
import de.hybris.platform.commercefacades.travel.PricedItineraryData;
import de.hybris.platform.webservicescommons.mapping.WsDTOMapping;
import de.hybris.travelcommercewebservicescommons.dto.ItineraryPricingInfoWsDTO;
import de.hybris.travelcommercewebservicescommons.dto.ItineraryWsDTO;
import de.hybris.travelcommercewebservicescommons.dto.PTCFareBreakdownWsDTO;
import de.hybris.travelcommercewebservicescommons.dto.PricedItineraryWsDTO;
import de.hybris.travelcommercewebservicescommons.dto.TotalFareWsDTO;
import de.hybris.travelcommercewebservicescommons.dto.TravelBundleTemplateWsDTO;
import de.hybris.travelcommercewebservicescommons.dto.TravellerBreakdownWsDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;


/**
 * Converter for {@link PricedItineraryData}
 */
@WsDTOMapping
public class PricedItineraryDataWsConverter extends CustomConverter<PricedItineraryData, PricedItineraryWsDTO>
{
	@Override
	public PricedItineraryWsDTO convert(final PricedItineraryData source,
			final Type<? extends PricedItineraryWsDTO> destinationType, final MappingContext mappingContext)
	{
		final PricedItineraryWsDTO pricedItineraryWsDTO = new PricedItineraryWsDTO();
		if (Objects.isNull(source))
		{
			return pricedItineraryWsDTO;
		}
		pricedItineraryWsDTO.setOriginDestinationRefNumber(source.getOriginDestinationRefNumber());
		pricedItineraryWsDTO.setId(source.getId());
		pricedItineraryWsDTO.setAvailable(source.isAvailable());

		final List<ItineraryPricingInfoData> listItineraryPricingInfoData = source.getItineraryPricingInfos();

		if (CollectionUtils.isNotEmpty(listItineraryPricingInfoData))
		{
			final List<ItineraryPricingInfoWsDTO> listItineraryPricingInfoWsDTO = new ArrayList<>();
			for (final ItineraryPricingInfoData itinerary : listItineraryPricingInfoData)
			{
				ItineraryPricingInfoWsDTO itineraryPricingInfoWsDTO = new ItineraryPricingInfoWsDTO();
				itineraryPricingInfoWsDTO.setTotalFare(mapperFacade.map(itinerary.getTotalFare(), TotalFareWsDTO.class));

				if (CollectionUtils.isNotEmpty(itinerary.getBundleTemplates()))
				{
					itineraryPricingInfoWsDTO
							.setBundleTemplates(mapperFacade.mapAsList(itinerary.getBundleTemplates(), TravelBundleTemplateWsDTO
									.class));
				}

				if (CollectionUtils.isNotEmpty(itinerary.getPtcFareBreakdownDatas()))
				{
					itineraryPricingInfoWsDTO.setPtcFareBreakdownDatas(
							mapperFacade.mapAsList(itinerary.getPtcFareBreakdownDatas(), PTCFareBreakdownWsDTO.class));
				}
				if (CollectionUtils.isNotEmpty(itinerary.getTravellerBreakdowns()))
				{
					itineraryPricingInfoWsDTO.setTravellerBreakdowns(
							mapperFacade.mapAsList(itinerary.getTravellerBreakdowns(), TravellerBreakdownWsDTO.class));
				}
				itineraryPricingInfoWsDTO.setAvailable(itinerary.isAvailable());
				itineraryPricingInfoWsDTO.setBundleType(itinerary.getBundleType());
				itineraryPricingInfoWsDTO.setBundleTypeName(itinerary.getBundleTypeName());
				itineraryPricingInfoWsDTO.setPromotional(itinerary.isPromotional());
				itineraryPricingInfoWsDTO.setSelected(itinerary.isSelected());
				itineraryPricingInfoWsDTO.setItineraryIdentifier(itinerary.getItineraryIdentifier());

				listItineraryPricingInfoWsDTO.add(itineraryPricingInfoWsDTO);
			}
			pricedItineraryWsDTO.setItineraryPricingInfos(listItineraryPricingInfoWsDTO);
		}
		pricedItineraryWsDTO.setItinerary(mapperFacade.map(source.getItinerary(), ItineraryWsDTO.class));

		return pricedItineraryWsDTO;
	}

}
