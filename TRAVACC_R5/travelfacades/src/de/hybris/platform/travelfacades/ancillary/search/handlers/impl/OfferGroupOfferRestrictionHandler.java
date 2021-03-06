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

import org.apache.commons.collections.CollectionUtils;


/**
 * Handler class to populate TravelRestriction for each product available to offer as ancillaries.
 */
public class OfferGroupOfferRestrictionHandler extends AbstractOfferRestrictionHandler implements AncillarySearchHandler
{

	@Override
	public void handle(final OfferRequestData offerRequestData, final OfferResponseData offerResponseData)
	{
		offerResponseData.getOfferGroups().stream()
				.filter(offerGroup -> CollectionUtils.isNotEmpty(offerGroup.getOfferPricingInfos()))
				.forEach(offerGroupData -> offerGroupData.getOfferPricingInfos().forEach(this::setTravelRestriction));
	}

}
