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

package de.hybris.platform.transportbackoffice.widgets.inventory.handler;

import de.hybris.platform.basecommerce.enums.InStockStatus;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.travelbackoffice.utils.TravelbackofficeUtils;
import de.hybris.platform.travelservices.stocklevel.StockLevelAttributes;

import java.util.List;
import java.util.Objects;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Row;

import com.hybris.cockpitng.components.Editor;


/**
 * This handler is used to handle the next step logic for Step2, in Create Inventory for Ancillary
 */
public class AssignStockInventoryForProductConfigurationHandler extends AbstractStockInventoryConfigurationHandler
{

	@Override
	protected StockLevelAttributes createStockLevelAttribute(final Row row)
	{
		final List<Component> children = row.getChildren();
		final StockLevelAttributes stockAttribute = new StockLevelAttributes();
		final ProductModel product = (ProductModel) ((Editor) children.get(0)).getValue();
		final Integer availableQuantity = ((Intbox) children.get(1)).getValue();
		final Integer oversellingQuantity = ((Intbox) children.get(2)).getValue();

		if (Objects.isNull(product) || Objects.isNull(availableQuantity))
		{
			return null;
		}
		TravelbackofficeUtils.validateStockAttributes(children);
		stockAttribute.setCode(product.getCode());
		stockAttribute.setAvailableQuantity(availableQuantity);
		stockAttribute.setOversellingQuantity(Objects.isNull(oversellingQuantity) ? 0 : oversellingQuantity);
		final Editor instockStatus = ((Editor) children.get(3));
		if (Objects.nonNull(instockStatus.getValue()))
		{
			stockAttribute.setInStockStatus(InStockStatus.valueOf(instockStatus.getValue().toString()));
		}
		return stockAttribute;
	}
}
