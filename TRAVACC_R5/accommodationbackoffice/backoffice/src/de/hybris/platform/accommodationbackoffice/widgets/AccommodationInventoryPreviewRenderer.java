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

package de.hybris.platform.accommodationbackoffice.widgets;

import de.hybris.platform.accommodationbackoffice.constants.AccommodationbackofficeConstants;
import de.hybris.platform.accommodationbackofficeservices.stocklevel.ManageAccommodationStockLevelInfo;
import de.hybris.platform.travelbackoffice.constants.TravelbackofficeConstants;
import de.hybris.platform.travelservices.stocklevel.StockLevelAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Vlayout;

import com.hybris.cockpitng.config.jaxb.wizard.ViewType;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.widgets.configurableflow.renderer.DefaultCustomViewRenderer;


/**
 * Creates the view of the Preview page for Create Inventory for Accommodation.
 */
public class AccommodationInventoryPreviewRenderer extends DefaultCustomViewRenderer
{

	private static final String PARENT_CONTENT = "create.accommodation.inventory.preview.wizard.content.parenttitle";
	private static final String INSTOCK_STATUS_NA_LABEL = "create.inventory.bookingclass.preview.wizard.instockstatus.na";
	private static final String PREVIEW_WARNING_MESSAGE = "manage.accommodation.inventory.preview.warning.message";
	private LabelService labelService;

	@Override
	public void render(final Component parent, final ViewType customView, final Map<String, String> parameters,
			final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
	{
		final ManageAccommodationStockLevelInfo manageStockLevel = widgetInstanceManager.getModel().getValue("manageStockLevelInfo",
				ManageAccommodationStockLevelInfo.class);
		widgetInstanceManager.getModel().put(TravelbackofficeConstants.FROM_PREVIOUS_STEP, Boolean.TRUE);

		final Component childContent = createLayoutForContent(manageStockLevel);

		final Vlayout vlayout = new Vlayout();
		vlayout.setParent(parent);
		final Label label = new Label(Labels.getLabel(PARENT_CONTENT));
		label.setStyle("font-weight: bold");
		vlayout.appendChild(label);
		vlayout.appendChild(childContent);
		vlayout.appendChild(createLayout(widgetInstanceManager));
	}

	/**
	 * @param manageStockLevel
	 * @return
	 */
	protected Component createLayoutForContent(final ManageAccommodationStockLevelInfo manageStockLevel)
	{
		final Div container = new Div();
		container.setVisible(true);

		final Grid accommodationConfigGrid = new Grid();
		final Column col1 = new Column();
		final Column col2 = new Column();

		final Columns accommodationConfigGridColumns = new Columns();
		accommodationConfigGridColumns.setParent(accommodationConfigGrid);

		final Rows accommodationConfigGridRows = new Rows();
		accommodationConfigGridRows.setParent(accommodationConfigGrid);
		accommodationConfigGrid.setSizedByContent(true);
		accommodationConfigGrid.getColumns().appendChild(col1);
		accommodationConfigGrid.getColumns().appendChild(col2);

		accommodationConfigGrid.getRows().appendChild(createContentRow(AccommodationbackofficeConstants.ASSIGN_ROOM_PROPERTY_TITLE,
				manageStockLevel.getAccommodationOffering().getCode()));
		accommodationConfigGrid.getRows().appendChild(
				createContentRow(AccommodationbackofficeConstants.ASSIGN_ROOM_PRODUCT_TYPE_TITLE, manageStockLevel.getProductType()));
		accommodationConfigGrid.getRows().appendChild(
				createContentRow(AccommodationbackofficeConstants.START_DATE, manageStockLevel.getStartDate().toString()));
		accommodationConfigGrid.getRows()
				.appendChild(createContentRow(AccommodationbackofficeConstants.END_DATE, manageStockLevel.getEndDate().toString()));
		container.appendChild(accommodationConfigGrid);

		final Div warningMessageDiv = new Div();
		warningMessageDiv.setStyle("margin-top: 40px");
		warningMessageDiv.setStyle("margin-bottom: -25px");
		final Label label = createLabel(Labels.getLabel(PREVIEW_WARNING_MESSAGE), true);
		warningMessageDiv.appendChild(label);
		container.appendChild(warningMessageDiv);

		return container;
	}

	/**
	 * @param firstColumnText
	 * @param secondColumnText
	 * @return
	 */
	protected Row createContentRow(final String firstColumnText, final String secondColumnText)
	{
		final Row row = new Row();
		final Label firstColumn = createLabel((StringUtils.isEmpty(firstColumnText) ? StringUtils.EMPTY
				: Labels.getLabel(firstColumnText) + TravelbackofficeConstants.COLON + " "), true);
		final Label secondColumn = createLabel(secondColumnText, false);
		row.appendChild(firstColumn);
		row.appendChild(secondColumn);
		return row;
	}

	/**
	 * Method creates layout for GridLayout for StockLevel Attributes
	 */
	protected Component createLayout(final WidgetInstanceManager widgetInstanceManager)
	{
		final Div container = new Div();
		container.setVisible(true);

		final Grid accommodationConfigGrid = new Grid();
		final Column col1 = new Column();
		final Column col2 = new Column();
		final Column col3 = new Column();
		final Column col4 = new Column();

		final Columns accommodationConfigGridColumns = new Columns();
		accommodationConfigGridColumns.setParent(accommodationConfigGrid);

		final Rows accommodationConfigGridRows = new Rows();
		accommodationConfigGridRows.setParent(accommodationConfigGrid);
		accommodationConfigGrid.setStyle("margin-top: 30px");
		accommodationConfigGrid.setSizedByContent(true);
		accommodationConfigGrid.getColumns().appendChild(col1);
		accommodationConfigGrid.getColumns().appendChild(col2);
		accommodationConfigGrid.getColumns().appendChild(col3);
		accommodationConfigGrid.getColumns().appendChild(col4);

		final Row row1 = new Row();
		row1.appendChild(
				createLabel(Labels.getLabel(AccommodationbackofficeConstants.PRODUCT_LABEL) + TravelbackofficeConstants.COLON, true));
		row1.appendChild(createLabel(
				Labels.getLabel(AccommodationbackofficeConstants.AVAILABLE_QUANTITY_LABEL) + TravelbackofficeConstants.COLON, true));
		row1.appendChild(createLabel(
				Labels.getLabel(AccommodationbackofficeConstants.OVERSELLING_QUANTITY_LABEL) + TravelbackofficeConstants.COLON,
				true));
		row1.appendChild(createLabel(
				Labels.getLabel(AccommodationbackofficeConstants.INSTOCK_STATUS_LABEL) + TravelbackofficeConstants.COLON, true));

		accommodationConfigGrid.getRows().appendChild(row1);
		final ManageAccommodationStockLevelInfo manageStockLevel = widgetInstanceManager.getModel().getValue(
				TravelbackofficeConstants.CREATE_INVENTORY_BOOKING_CLASS_MANAGE_STOCK_LEVEL_ITEM_ID,
				ManageAccommodationStockLevelInfo.class);
		manageStockLevel.getStockLevelAttributes().forEach(
				stockAttribute -> accommodationConfigGrid.getRows().appendChild(createStockAttributesColumnLayout(stockAttribute)));
		container.appendChild(accommodationConfigGrid);
		return container;
	}

	/**
	 * @param stockLevelAttribute
	 * @return
	 */
	protected Row createStockAttributesColumnLayout(final StockLevelAttributes stockLevelAttribute)
	{
		final Row row = new Row();
		row.appendChild(createLabel(stockLevelAttribute.getCode(), false));
		row.appendChild(createLabel(String.valueOf(stockLevelAttribute.getAvailableQuantity()), false));
		row.appendChild(createLabel(String.valueOf(stockLevelAttribute.getOversellingQuantity()), false));
		row.appendChild(
				createLabel((Objects.isNull(stockLevelAttribute.getInStockStatus()) ? Labels.getLabel(INSTOCK_STATUS_NA_LABEL)
						: getLabelService().getObjectLabel(stockLevelAttribute.getInStockStatus())), false));
		return row;
	}

	/**
	 * @param labelName
	 * @param isBold
	 * @return
	 */
	protected Label createLabel(final String labelName, final boolean isBold)
	{
		final Label label = new Label(labelName);
		if (isBold)
		{
			label.setStyle("font-weight: bold");
		}
		return label;
	}

	/**
	 * @return the labelService
	 */
	protected LabelService getLabelService()
	{
		return labelService;
	}

	/**
	 * @param labelService
	 *           the labelService to set
	 */
	@Required
	public void setLabelService(final LabelService labelService)
	{
		this.labelService = labelService;
	}

}
