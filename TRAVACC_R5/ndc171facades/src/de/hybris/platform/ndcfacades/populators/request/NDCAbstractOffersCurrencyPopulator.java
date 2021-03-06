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

import de.hybris.platform.commercefacades.storesession.StoreSessionFacade;
import de.hybris.platform.ndcfacades.ndc.BaggageListRQ;
import de.hybris.platform.ndcfacades.ndc.MessageParamsBaseType.CurrCodes;
import de.hybris.platform.ndcfacades.ndc.MessageParamsBaseType.CurrCodes.FiledInCurrency;
import de.hybris.platform.ndcfacades.ndc.ServiceListRQ;

import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

/**
 * An abstract class to populates Currency from {@link BaggageListRQ} and {@link ServiceListRQ} to session.
 */
public abstract class NDCAbstractOffersCurrencyPopulator
{
	private StoreSessionFacade storeSessionFacade;

	protected void populate(final CurrCodes currCodes)
	{
		if (CollectionUtils.isNotEmpty(currCodes.getFiledInCurrency()))
		{
			final FiledInCurrency filledInCurrency = currCodes.getFiledInCurrency().stream().findFirst().orElse(null);
			if (Objects.nonNull(filledInCurrency.getCurrCode()))
			{
				getStoreSessionFacade().setCurrentCurrency(filledInCurrency.getCurrCode().getValue());
			}
		}
	}

	protected StoreSessionFacade getStoreSessionFacade()
	{
		return storeSessionFacade;
	}

	@Required
	public void setStoreSessionFacade(final StoreSessionFacade storeSessionFacade)
	{
		this.storeSessionFacade = storeSessionFacade;
	}

}
