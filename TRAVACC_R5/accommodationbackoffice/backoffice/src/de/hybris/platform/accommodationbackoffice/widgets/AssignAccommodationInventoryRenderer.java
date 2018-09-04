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
import de.hybris.platform.basecommerce.enums.InStockStatus;
import de.hybris.platform.travelbackoffice.constants.TravelbackofficeConstants;
import de.hybris.platform.travelservices.stocklevel.StockLevelAttributes;

import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Vlayout;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.config.jaxb.wizard.ViewType;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.configurableflow.renderer.DefaultCustomViewRenderer;


/**
 * This created the View for the Assigning Inventory
 */
public class AssignAccommodationInventoryRenderer extends DefaultCustomViewRenderer
{
	private EventListener<Event> addButtonEventListener;
	private EventListener<Event> removeButtonEventListener;
	protected static final String PRODUCTTYPE_DROPDOWN_EDITOR_VALUE = "product.type.dropdown.selected.value";

	@Override
	public void render(final Component parent, final ViewType customView, final Map<String, String> parameters,
			final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
	{

		final Vlayout vlayout = new Vlayout();
		vlayout.setParent(parent);
		vlayout.appendChild(createLayout(widgetInstanceManager));
	}

	/**
	 * @param widgetInstanceManager
	 * @return
	 */
	protected Component createLayout(final WidgetInstanceManager widgetInstanceManager)
	{

		final ManageAccommodationStockLevelInfo manageStockLevel = widgetInstanceManager.getModel().getValue("manageStockLevelInfo",
				ManageAccommodationStockLevelInfo.class);

		final Div container = new Div();
		container.setVisible(true);

		final Grid assignInventoryGrid = new Grid();
		final Column col1 = new Column();
		final Column col2 = new Column();
		final Column col3 = new Column();
		final Column col4 = new Column();
		final Column col5 = new Column();
		final Column col6 = new Column();

		final Columns assignInventoryGridColumns = new Columns();
		assignInventoryGridColumns.setParent(assignInventoryGrid);

		final Rows assignInventoryGridRows = new Rows();
		assignInventoryGridRows.setParent(assignInventoryGrid);

		assignInventoryGrid.setClass("manage-inventory-grid");
		assignInventoryGrid.setSizedByContent(true);
		assignInventoryGrid.getColumns().appendChild(col1);
		assignInventoryGrid.getColumns().appendChild(col2);
		assignInventoryGrid.getColumns().appendChild(col3);
		assignInventoryGrid.getColumns().appendChild(col4);
		assignInventoryGrid.getColumns().appendChild(col5);
		assignInventoryGrid.getColumns().appendChild(col6);
		assignInventoryGrid.setId(AccommodationbackofficeConstants.CREATE_ACCOMMODATION_INVENTORY_MANAGE_STOCK_PROPERTIES_GRID_ID);
		final Row row1 = new Row();
		row1.setId(String.valueOf(0));

		final Label productLabel = new Label(
				Labels.getLabel(AccommodationbackofficeConstants.PRODUCT_CODE_LABEL) + TravelbackofficeConstants.COLON);
		final Label availableQuantityLabel = new Label(
				Labels.getLabel(AccommodationbackofficeConstants.AVAILABLE_QUANTITY_LABEL) + TravelbackofficeConstants.COLON);
		final Label oversellingLabel = new Label(
				Labels.getLabel(AccommodationbackofficeConstants.OVERSELLING_QUANTITY_LABEL) + TravelbackofficeConstants.COLON);
		final Label inStockStatusLabel = new Label(
				Labels.getLabel(AccommodationbackofficeConstants.INSTOCK_STATUS_LABEL) + TravelbackofficeConstants.COLON);

		row1.appendChild(productLabel);
		row1.appendChild(availableQuantityLabel);
		row1.appendChild(oversellingLabel);
		row1.appendChild(inStockStatusLabel);
		row1.appendChild(new Label());
		row1.appendChild(new Label());

		assignInventoryGrid.getRows().appendChild(row1);

		removeButtonEventListener = event -> {
			if (StringUtils.equals(Events.ON_CLICK, event.getName()))
			{
				if (CollectionUtils.size(assignInventoryGrid.getRows().getChildren()) > 2)
				{
					final Component removeComponent = event.getTarget().getParent();
					assignInventoryGrid.getRows().removeChild(removeComponent);
					final Row lastRow = (Row) assignInventoryGrid.getRows().getLastChild();
					if (lastRow.getIndex() > 0)
					{
						lastRow.getChildren().get(4).setVisible(Boolean.TRUE);
					}
					container.appendChild(assignInventoryGrid);
					container.getRedrawCallback();
				}
			}
		};
		addButtonEventListener = event -> {
			if (StringUtils.equals(Events.ON_CLICK, event.getName()))
			{
				assignInventoryGrid.getRows()
						.appendChild(createInventoryAttributesColumnLayout(widgetInstanceManager,
								Integer.parseInt(event.getTarget().getId()) + 1, addButtonEventListener, removeButtonEventListener, null,
								Boolean.TRUE));
				container.appendChild(assignInventoryGrid);
				event.getTarget().setVisible(Boolean.FALSE);
				container.getRedrawCallback();
			}
		};

		final Boolean fromPreviewStep = widgetInstanceManager.getModel().getValue(TravelbackofficeConstants.FROM_PREVIOUS_STEP,
				Boolean.class);

		if (CollectionUtils.isNotEmpty(manageStockLevel.getStockLevelAttributes()) && Objects.nonNull(fromPreviewStep)
				&& fromPreviewStep.booleanValue())
		{
			for (int count = 1; count <= manageStockLevel.getStockLevelAttributes().size(); count++)
			{
				final boolean isPlusButtonNeedToAdd = manageStockLevel.getStockLevelAttributes().size() == 1 ? Boolean.TRUE
						: count == manageStockLevel.getStockLevelAttributes().size();
				assignInventoryGrid.getRows()
						.appendChild(createInventoryAttributesColumnLayout(widgetInstanceManager, count, addButtonEventListener,
								removeButtonEventListener, manageStockLevel.getStockLevelAttributes().get(count - 1),
								isPlusButtonNeedToAdd));
			}
		}
		else
		{
			assignInventoryGrid.getRows().appendChild(createInventoryAttributesColumnLayout(widgetInstanceManager, 1,
					addButtonEventListener, removeButtonEventListener, null, Boolean.TRUE));
		}
		container.appendChild(assignInventoryGrid);
		return container;

	}


	/**
	 * @param widgetInstanceManager
	 * @param index
	 * @param addButtonlistener
	 * @param removeButtonlistener
	 * @param stockLevelAttributes
	 * @param isPlusButtonNeedToAdd
	 * @return
	 */
	protected Row createInventoryAttributesColumnLayout(final WidgetInstanceManager widgetInstanceManager, final Integer index,
			final EventListener<Event> addButtonlistener, final EventListener<Event> removeButtonlistener,
			final StockLevelAttributes stockLevelAttributes, final boolean isPlusButtonNeedToAdd)
	{
		final Row row = new Row();
		row.setId(index.toString());
		final String productTypeEditorValue = widgetInstanceManager.getModel()
				.getValue(AccommodationbackofficeConstants.SELECTED_ACCOMMODATION_TYPE, String.class);
		final Editor accommodationEditor = createEditor(widgetInstanceManager, "Reference(" + productTypeEditorValue + ")", null,
				false, null);
		final Intbox availableQuantityTextBox = new Intbox();
		final Intbox oversellingTextBox = new Intbox();
		InStockStatus defaultInStockStatus = InStockStatus.NOTSPECIFIED;
		final Editor inStockStatusEditor = createEditor(widgetInstanceManager, "java.lang.Enum(" + InStockStatus._TYPECODE + ")",
				defaultInStockStatus, false, null);

		if (Objects.nonNull(stockLevelAttributes))
		{
			accommodationEditor.setValue(stockLevelAttributes.getAncillaryProduct());
			availableQuantityTextBox.setText(String.valueOf(stockLevelAttributes.getAvailableQuantity()));
			oversellingTextBox.setText(String.valueOf(stockLevelAttributes.getOversellingQuantity()));
			defaultInStockStatus = stockLevelAttributes.getInStockStatus();
		}
		row.appendChild(accommodationEditor);
		row.appendChild(availableQuantityTextBox);
		row.appendChild(oversellingTextBox);
		row.appendChild(inStockStatusEditor);

		final Button buttonAdd = new Button();

		buttonAdd.setId(index.toString());
		buttonAdd.setClass("yw-advancedsearch-add-btn z-button");
		buttonAdd.setStyle("right: 60px");
		buttonAdd.addEventListener(Events.ON_CLICK, addButtonlistener);
		row.appendChild(buttonAdd);
		buttonAdd.setVisible(isPlusButtonNeedToAdd);

		final Button buttonRemove = new Button();
		buttonRemove.setId(index.toString());
		buttonRemove.setClass("yw-advancedsearch-delete-btn ye-delete-btn z-button");
		buttonRemove.addEventListener(Events.ON_CLICK, removeButtonlistener);
		row.appendChild(buttonRemove);
		return row;
	}

	/**
	 * @param widgetInstanceManager
	 * @param type
	 * @param initialValue
	 * @param nestedObjectCreation
	 * @param property
	 * @return
	 */
	protected Editor createEditor(final WidgetInstanceManager widgetInstanceManager, final String type, final Object initialValue,
			final boolean nestedObjectCreation, final String property)
	{
		final Editor editor = new Editor();
		editor.setNestedObjectCreationDisabled(nestedObjectCreation);
		editor.setWidgetInstanceManager(widgetInstanceManager);
		editor.setType(type);
		if (Objects.nonNull(initialValue))
		{
			editor.setInitialValue(initialValue);
		}
		editor.setOptional(Boolean.TRUE);
		editor.setProperty(property);
		editor.initialize();
		return editor;
	}

}
