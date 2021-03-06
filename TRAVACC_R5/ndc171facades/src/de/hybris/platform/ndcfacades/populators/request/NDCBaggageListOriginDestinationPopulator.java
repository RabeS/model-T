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

package de.hybris.platform.ndcfacades.populators.request;

import de.hybris.platform.commercefacades.travel.ItineraryData;
import de.hybris.platform.commercefacades.travel.ancillary.data.OfferRequestData;
import de.hybris.platform.commercefacades.travel.enums.TripType;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.ndcfacades.ndc.BaggageListRQ;
import de.hybris.platform.ndcfacades.ndc.BaggageListRQ.Query.OriginDestination;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.List;


/**
 * NDC OriginDestination populator for {@link BaggageListRQ}
 */
public class NDCBaggageListOriginDestinationPopulator extends NDCAbstractOffersOriginDestinationPopulator implements Populator<BaggageListRQ, OfferRequestData>
{

	@Override
	public void populate(final BaggageListRQ source, final OfferRequestData target) throws ConversionException
	{
		final List<OriginDestination> originDestinations = source.getQuery().getOriginDestination();
		final List<ItineraryData> itineraries = new ArrayList<>();
		originDestinations.forEach(originDestination -> {
			final ItineraryData itinerary = createItinerary(originDestination.getTotalJourney(), originDestination.getOriginDestinationKey(), originDestination.getFlight());
			itinerary.setTripType(originDestinations.size() == 1 ? TripType.SINGLE : TripType.RETURN);
			itineraries.add(itinerary);
		});
		target.setItineraries(itineraries);
		target.setSelectedOffers(getSelectedOffer());
	}

}
