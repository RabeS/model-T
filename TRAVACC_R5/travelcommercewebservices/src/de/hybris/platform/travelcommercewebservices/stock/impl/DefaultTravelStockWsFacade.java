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
package de.hybris.platform.travelcommercewebservices.stock.impl;

import de.hybris.platform.commercefacades.product.data.StockData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.WarehouseService;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.travelcommercewebservices.stock.TravelStockWsFacade;
import de.hybris.platform.travelservices.stock.TravelStockService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


/**
 * The Default travel stock ws facade.
 */
public class DefaultTravelStockWsFacade implements TravelStockWsFacade
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultTravelStockWsFacade.class);

	private WarehouseService warehouseService;
	private TravelStockService travelStockService;
	private ProductService productService;
	private Converter<StockLevelModel, StockData> stockDataConverter;

	@Override
	public List<StockData> getStockLevelsForWarehouse(final List<String> warehouseCodes)
	{
		if (CollectionUtils.isEmpty(warehouseCodes))
		{
			return Collections.emptyList();
		}
		try
		{
			final List<WarehouseModel> warehouseModels = warehouseCodes.stream()
					.map(warehouseCode -> getWarehouseService().getWarehouseForCode(warehouseCode))
					.collect(Collectors.toList());

			final List<StockLevelModel> stockLevels = getTravelStockService().findStockLevelsForWarehouses(warehouseModels);
			if (CollectionUtils.isEmpty(stockLevels))
			{
				return Collections.emptyList();
			}

			return getStockDataConverter().convertAll(stockLevels);
		}
		catch (final UnknownIdentifierException e)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Cannot find the warehouse: ", e);
			}
		}
		return null;
	}

	@Override
	public List<StockData> getProductStockLevelsForWarehouses(final String productCode, final List<String> warehouseCodes)
	{
		if (CollectionUtils.isEmpty(warehouseCodes) || StringUtils.isEmpty(productCode))
		{
			return Collections.emptyList();
		}

		try
		{
			final List<WarehouseModel> warehouseModels = warehouseCodes.stream()
					.map(warehouseCode -> getWarehouseService().getWarehouseForCode(warehouseCode))
					.collect(Collectors.toList());
			final ProductModel productModel = getProductService().getProductForCode(productCode);
			final List<StockLevelModel> stockLevels = (List<StockLevelModel>) getTravelStockService()
					.getStockLevels(productModel, warehouseModels);
			if (CollectionUtils.isEmpty(stockLevels))
			{
				return Collections.emptyList();
			}

			return getStockDataConverter().convertAll(stockLevels);
		}
		catch (final UnknownIdentifierException e)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Model not found: ", e);
			}
		}
		return null;
	}



	/**
	 * Gets warehouse service.
	 *
	 * @return the warehouse service
	 */
	protected WarehouseService getWarehouseService()
	{
		return warehouseService;
	}

	/**
	 * Sets warehouse service.
	 *
	 * @param warehouseService
	 * 		the warehouse service
	 */
	@Required
	public void setWarehouseService(final WarehouseService warehouseService)
	{
		this.warehouseService = warehouseService;
	}

	/**
	 * Gets travel stock service.
	 *
	 * @return the travel stock service
	 */
	protected TravelStockService getTravelStockService()
	{
		return travelStockService;
	}

	/**
	 * Sets travel stock service.
	 *
	 * @param travelStockService
	 * 		the travel stock service
	 */
	@Required
	public void setTravelStockService(final TravelStockService travelStockService)
	{
		this.travelStockService = travelStockService;
	}

	/**
	 * Gets stock data converter.
	 *
	 * @return the stock data converter
	 */
	protected Converter<StockLevelModel, StockData> getStockDataConverter()
	{
		return stockDataConverter;
	}

	/**
	 * Sets stock data converter.
	 *
	 * @param stockDataConverter
	 * 		the stock data converter
	 */
	@Required
	public void setStockDataConverter(
			final Converter<StockLevelModel, StockData> stockDataConverter)
	{
		this.stockDataConverter = stockDataConverter;
	}

	/**
	 * Gets product service.
	 *
	 * @return the product service
	 */
	protected ProductService getProductService()
	{
		return productService;
	}

	/**
	 * Sets product service.
	 *
	 * @param productService
	 * 		the product service
	 */
	@Required
	public void setProductService(final ProductService productService)
	{
		this.productService = productService;
	}
}
