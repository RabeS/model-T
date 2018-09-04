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
package de.hybris.platform.travelfacades.ancillary.search.handlers.impl;


import de.hybris.platform.commercefacades.travel.ancillary.data.OfferRequestData;
import de.hybris.platform.commercefacades.travel.ancillary.data.OfferResponseData;
import de.hybris.platform.travelfacades.ancillary.search.handlers.AncillarySearchHandler;


/**
 * The Basic offer search response handler.
 */
public class BasicOfferSearchResponseHandler implements AncillarySearchHandler
{
	@Override
	public void handle(final OfferRequestData offerRequestData, final OfferResponseData offerResponseData)
	{
		offerResponseData.setItineraries(offerRequestData.getItineraries());
	}
}
