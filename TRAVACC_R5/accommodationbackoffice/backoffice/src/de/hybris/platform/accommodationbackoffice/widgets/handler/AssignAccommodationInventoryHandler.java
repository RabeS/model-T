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

package de.hybris.platform.accommodationbackoffice.widgets.handler;

import de.hybris.platform.accommodationbackoffice.constants.AccommodationbackofficeConstants;
import de.hybris.platform.accommodationbackofficeservices.stocklevel.ManageAccommodationStockLevelInfo;
import de.hybris.platform.basecommerce.enums.InStockStatus;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.travelbackoffice.utils.TravelbackofficeUtils;
import de.hybris.platform.travelservices.stocklevel.StockLevelAttributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.config.jaxb.wizard.CustomType;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandler;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandlerAdapter;


/**
 * This is the handler for the validations for the Step #1 of the Add - manage inventory
 */
public class AssignAccommodationInventoryHandler implements FlowActionHandler
{

	private static final Logger LOG = Logger.getLogger(AssignAccommodationInventoryHandler.class);
	private static final String ERROR_MESSAGE = "create.inventory.bookingclass.assigninventory.wizard.error";
	private static final String ERROR_UNKNOWN_MESSAGE = "create.inventory.bookingclass.assigninventory.wizard.popup.error.unknown.message";
	public static final String ERROR_POPUP_TITLE = "error.title.message";

	@Override
	public void perform(final CustomType customType, final FlowActionHandlerAdapter adapter, final Map<String, String> parameters)
	{

		if (Objects.nonNull(adapter.getWidgetInstanceManager().getWidgetslot()))
		{
			final ManageAccommodationStockLevelInfo manageStockLevel = adapter.getWidgetInstanceManager().getModel()
					.getValue("manageStockLevelInfo", ManageAccommodationStockLevelInfo.class);
			final Object grid = adapter.getWidgetInstanceManager().getWidgetslot()
					.getFellow(AccommodationbackofficeConstants.CREATE_ACCOMMODATION_INVENTORY_MANAGE_STOCK_PROPERTIES_GRID_ID);
			if (Objects.nonNull(grid))
			{
				final List<Component> children = ((Grid) grid).getRows().getChildren();
				final int stockLevelAttributesSize = children.size() - 1;
				manageStockLevel.setStockLevelAttributes(new ArrayList<>(stockLevelAttributesSize));
				try
				{
					children.forEach(child -> {
						final int id = Integer.parseInt(child.getId());
						if (id > 0)
						{
							final StockLevelAttributes stockAttribute = createStockLevelAttribute((Row) child);
							if (Objects.nonNull(stockAttribute))
							{
								manageStockLevel.getStockLevelAttributes().add(stockAttribute);
							}
						}
					});
					if (CollectionUtils.size(manageStockLevel.getStockLevelAttributes()) < stockLevelAttributesSize)
					{
						displayErrorMessage(Labels.getLabel(ERROR_MESSAGE), Labels.getLabel(ERROR_POPUP_TITLE));
						manageStockLevel.setStockLevelAttributes(Collections.emptyList());
					}
					else
					{
						adapter.next();
					}
				}
				catch (final NumberFormatException ex)
				{
					LOG.error("Required information not found.");
					displayErrorMessage(Labels.getLabel(ERROR_UNKNOWN_MESSAGE), Labels.getLabel(ERROR_POPUP_TITLE));
				}
			}
			else
			{
				LOG.error("Required information not found.");
				displayErrorMessage(Labels.getLabel(ERROR_UNKNOWN_MESSAGE), Labels.getLabel(ERROR_POPUP_TITLE));
			}
		}

	}

	/**
	 * @param row
	 * @return
	 */
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
		stockAttribute.setAncillaryProduct(product);
		stockAttribute.setAvailableQuantity(availableQuantity);
		stockAttribute.setOversellingQuantity(Objects.isNull(oversellingQuantity) ? 0 : oversellingQuantity);
		final Editor instockStatus = ((Editor) children.get(3));
		if (Objects.nonNull(instockStatus.getValue()))
		{
			stockAttribute.setInStockStatus(InStockStatus.valueOf(instockStatus.getValue().toString()));
		}
		return stockAttribute;
	}

	/**
	 * @param errorMsg
	 * @param title
	 */
	protected void displayErrorMessage(final String errorMsg, final String title)
	{
		Messagebox.show(errorMsg, title, Messagebox.OK, Messagebox.ERROR, null);
	}

}
