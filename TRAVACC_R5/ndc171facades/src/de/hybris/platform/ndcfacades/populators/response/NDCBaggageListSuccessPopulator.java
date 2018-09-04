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
import de.hybris.platform.ndcfacades.ndc.BaggageListRS;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;


/**
 * NDC Success Populator for {@link BaggageListRS}
 */
public class NDCBaggageListSuccessPopulator extends NDCRSSuccessPopulator implements Populator<OfferResponseData, BaggageListRS>
{
	@Override
	public void populate(final OfferResponseData offerResponseData, final BaggageListRS baggageListRS) throws ConversionException
	{
		baggageListRS.setSuccess(getNDCSuccessType());
	}
}
