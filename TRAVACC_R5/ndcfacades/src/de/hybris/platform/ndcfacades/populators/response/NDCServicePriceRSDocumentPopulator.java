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
package de.hybris.platform.ndcfacades.populators.response;

import de.hybris.platform.commercefacades.travel.ancillary.data.OfferResponseData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.ndcfacades.ndc.ServicePriceRS;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;


/**
 * NDC Msg Document Type Populator for {@link ServicePriceRS}
 */
public class NDCServicePriceRSDocumentPopulator extends NDCMsgDocumentTypePopulator
		implements Populator<OfferResponseData, ServicePriceRS>
{
	@Override
	public void populate(final OfferResponseData offerResponseData, final ServicePriceRS servicePriceRS) throws ConversionException
	{
		servicePriceRS.setDocument(getMsgDocumentType());
	}
}
