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

package de.hybris.platform.travelservices.strategies.stock.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.travelservices.stock.TravelCommerceStockService;
import de.hybris.platform.travelservices.strategies.stock.StockResolvingStrategyByEntryType;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Required;


/**
 * The type Default entry type stock resolving strategy.
 */
public class DefaultEntryTypeStockResolvingStrategy implements StockResolvingStrategyByEntryType
{

	private TravelCommerceStockService commerceStockService;

	@Override
	public Long getStock(final AbstractOrderEntryModel entry)
	{
		return getCommerceStockService().getStockLevelQuantity(entry.getProduct(), Collections.emptyList());
	}

	/**
	 * @return the commerceStockService
	 */
	protected TravelCommerceStockService getCommerceStockService()
	{
		return commerceStockService;
	}

	/**
	 * @param commerceStockService
	 *           the commerceStockService to set
	 */
	@Required
	public void setCommerceStockService(final TravelCommerceStockService commerceStockService)
	{
		this.commerceStockService = commerceStockService;
	}
}
