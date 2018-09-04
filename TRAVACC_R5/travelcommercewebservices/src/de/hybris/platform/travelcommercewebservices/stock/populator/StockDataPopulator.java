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
package de.hybris.platform.travelcommercewebservices.stock.populator;

import de.hybris.platform.commercefacades.product.data.StockData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.springframework.util.Assert;

/**
 * The Stock data populator.
 */
public class StockDataPopulator implements Populator<StockLevelModel, StockData>
{
	@Override
	public void populate(final StockLevelModel source, final StockData target) throws ConversionException
	{

		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		if (source.getInStockStatus() != null)
		{
			target.setInStockStatus(source.getInStockStatus());
		}
		target.setProductCode(source.getProductCode());
		target.setAvailable(source.getAvailable());
		target.setReserved(source.getReserved());
		target.setOverselling(source.getOverSelling());

	}
}
