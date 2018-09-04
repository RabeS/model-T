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

package de.hybris.platform.travelservices.strategies.stock.accommodation.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.travelservices.strategies.stock.StockResolvingStrategyByEntryType;
import de.hybris.platform.travelservices.strategies.stock.StockResolvingStrategyByProductType;

import java.util.Map;
import java.util.Objects;


/**
 * The type Accommodation stock resolving strategy.
 */
public class AccommodationStockResolvingStrategy implements StockResolvingStrategyByEntryType
{

	private Map<String, StockResolvingStrategyByProductType> strategyByProductTypeMap;
	private static final String DEFAULT = "DEFAULT";

	@Override
	public Long getStock(final AbstractOrderEntryModel entry)
	{
		final StockResolvingStrategyByProductType strategy = getStrategyByProductTypeMap()
				.get(entry.getProduct().getClass().getSimpleName());
		return Objects.nonNull(strategy) ? strategy.getStock(entry) : getStrategyByProductTypeMap().get(DEFAULT).getStock(entry);
	}

	/**
	 * 
	 * @return the strategyByProductTypeMap
	 */
	protected Map<String, StockResolvingStrategyByProductType> getStrategyByProductTypeMap()
	{
		return strategyByProductTypeMap;
	}

	/**
	 * 
	 * @param strategyByProductTypeMap
	 *           the strategyByProductTypeMap to set
	 */
	public void setStrategyByProductTypeMap(final Map<String, StockResolvingStrategyByProductType> strategyByProductTypeMap)
	{
		this.strategyByProductTypeMap = strategyByProductTypeMap;
	}


}
