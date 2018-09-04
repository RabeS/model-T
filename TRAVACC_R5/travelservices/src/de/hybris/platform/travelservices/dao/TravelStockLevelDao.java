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

package de.hybris.platform.travelservices.dao;

import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.stock.impl.StockLevelDao;

import java.util.Collection;
import java.util.Date;
import java.util.List;


/**
 * Extension of StockLevelDao to provide a travel specific functionality like searching for stock level for particular date.
 */
public interface TravelStockLevelDao extends StockLevelDao
{
	/**
	 * Returns a stock level for product for a particular date.
	 *
	 * @param productCode
	 * @param warehouseModels
	 * @param date
	 * @return
	 */
	StockLevelModel findStockLevel(String productCode, Collection<WarehouseModel> warehouseModels, Date date);

	/**
	 * Returns list of {@link StockLevelModel} for given list of {@link WarehouseModel}.
	 *
	 * @param warehouses
	 */
	List<StockLevelModel> findStockLevelsForWarehouses(List<WarehouseModel> warehouses);
	
}
