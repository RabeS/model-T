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
import de.hybris.platform.ndcfacades.ndc.ServiceListRS;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;


/**
 * NDC Success Populator for {@link ServiceListRS}
 */
public class NDCServiceListSuccessPopulator extends NDCRSSuccessPopulator implements Populator<OfferResponseData, ServiceListRS>
{
	@Override
	public void populate(final OfferResponseData offerResponseData, final ServiceListRS serviceListRS) throws ConversionException
	{
		serviceListRS.setSuccess(getNDCSuccessType());
	}
}
