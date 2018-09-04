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

package de.hybris.platform.ndcfacades.offers.handlers.impl;

import de.hybris.platform.commercefacades.travel.ancillary.data.OfferRequestData;
import de.hybris.platform.commercefacades.travel.ancillary.data.OfferResponseData;
import de.hybris.platform.commercefacades.travel.seatmap.data.SegmentInfoData;
import de.hybris.platform.travelfacades.ancillary.search.handlers.AncillarySearchHandler;


/**
 * Handler class to populate {@link SegmentInfoData} for the OfferResponseData.
 */
public class NDCBasicAccommodationMapHandler implements AncillarySearchHandler
{

	@Override
	public void handle(final OfferRequestData offerRequestData, final OfferResponseData offerResponseData)
	{
		offerResponseData.getSeatMap().setSegmentInfoDatas(offerRequestData.getSeatMapRequest().getSegmentInfoDatas());
	}

}
