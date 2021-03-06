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

import de.hybris.platform.commercefacades.travel.ancillary.data.OfferRequestData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.ndcfacades.ndc.BaggageChargesRQ;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.Objects;


/**
 * Concrete class to populate currency for {@link BaggageChargesRQ}
 */
public class NDCBaggageChargesCurrencyPopulator extends NDCAbstractOffersCurrencyPopulator
		implements Populator<BaggageChargesRQ, OfferRequestData>
{
	@Override
	public void populate(final BaggageChargesRQ source, final OfferRequestData target) throws ConversionException
	{
		if (!Objects.isNull(source.getParameters()) && !Objects.isNull(source.getParameters().getCurrCodes()))
		{
			populate(source.getParameters().getCurrCodes());
		}
	}
}
