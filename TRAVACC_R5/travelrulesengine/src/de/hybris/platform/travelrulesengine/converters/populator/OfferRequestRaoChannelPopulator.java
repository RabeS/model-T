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

package de.hybris.platform.travelrulesengine.converters.populator;

import de.hybris.platform.commercefacades.travel.ancillary.data.OfferRequestData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.travelrulesengine.rao.OfferRequestRAO;

/**
 * This class populates sales application value in Offer Request Rao
 */
public class OfferRequestRaoChannelPopulator extends AbstractAccommodationChannelRaoPopulator
		implements Populator<OfferRequestData, OfferRequestRAO>
{
	@Override
	public void populate(final OfferRequestData source, final OfferRequestRAO target) throws ConversionException
	{
		target.setSalesApplication(getSalesApplication());
	}
}
