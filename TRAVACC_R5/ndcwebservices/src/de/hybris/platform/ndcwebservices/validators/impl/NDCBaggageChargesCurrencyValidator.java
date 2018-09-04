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

package de.hybris.platform.ndcwebservices.validators.impl;

import de.hybris.platform.ndcfacades.ndc.BaggageChargesRQ;
import de.hybris.platform.ndcfacades.ndc.ErrorsType;
import de.hybris.platform.ndcwebservices.constants.NdcwebservicesConstants;

import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;


/**
 * The concrete class to validates currency for {@link BaggageChargesRQ}
 */
public class NDCBaggageChargesCurrencyValidator extends NDCOffersCurrencyValidator<BaggageChargesRQ>
{
	@Override
	public void validate(final BaggageChargesRQ baggageChargesRQ, final ErrorsType errorsType)
	{
		if (Objects.nonNull(baggageChargesRQ.getParameters()) && Objects.nonNull(baggageChargesRQ.getParameters().getCurrCodes())
				&& CollectionUtils.isNotEmpty(baggageChargesRQ.getParameters().getCurrCodes().getCurrCode()))
		{
			isValidIsoCode(baggageChargesRQ.getParameters().getCurrCodes().getCurrCode().get(0).getValue(), errorsType);
			return;
		}
		addError(errorsType,
				getConfigurationService().getConfiguration().getString(NdcwebservicesConstants.MISSING_OFFERS_CURRENCY));
	}
}
