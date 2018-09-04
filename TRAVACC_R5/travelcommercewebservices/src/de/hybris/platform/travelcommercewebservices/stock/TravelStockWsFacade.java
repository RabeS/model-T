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
package de.hybris.platform.travelcommercewebservices.stock;

import de.hybris.platform.commercefacades.product.data.StockData;

import java.util.List;


/**
 * The interface Travel stock ws facade.
 */
public interface TravelStockWsFacade
{
	/**
	 * Gets stock levels for warehouse.
	 *
	 * @param warehouseCodes
	 * 		the warehouse code
	 * @return the stock levels for warehouse
	 */
	List<StockData> getStockLevelsForWarehouse(List<String> warehouseCodes);

	/**
	 * Gets product stock levels for warehouse.
	 *
	 * @param productCode
	 * 		the product code
	 * @param warehouseCodes
	 * 		the warehouse codes
	 * @return the product stock levels for warehouse
	 */
	List<StockData> getProductStockLevelsForWarehouses(String productCode, List<String> warehouseCodes);
}
