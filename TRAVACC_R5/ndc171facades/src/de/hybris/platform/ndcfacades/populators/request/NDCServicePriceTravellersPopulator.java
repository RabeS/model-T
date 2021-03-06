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

import de.hybris.platform.commercefacades.travel.TravellerData;
import de.hybris.platform.commercefacades.travel.ancillary.data.OfferRequestData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.ndcfacades.ndc.ServicePriceRQ;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;


/**
 * The NDC Travellers Populator, create a list of {@link TravellerData} based on the AnonymousTraveler information
 */
public class NDCServicePriceTravellersPopulator extends NDCRQTravellersPopulator
		implements Populator<ServicePriceRQ, OfferRequestData>
{

	@Override
	public void populate(final ServicePriceRQ source, final OfferRequestData target) throws ConversionException
	{
		final List<TravellerData> travellers = getTravellersFromNDCRQ(source.getDataLists().getPassengerList().getPassenger());
		if (CollectionUtils.isNotEmpty(target.getItineraries()))
		{
			target.getItineraries().forEach(itineraryData -> itineraryData.setTravellers(travellers));
		}
	}
}
