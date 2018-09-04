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
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.travelbackoffice.constants.TravelbackofficeConstants;
import de.hybris.platform.travelbackofficeservices.stock.TravelBackofficeStockService;
import de.hybris.platform.travelbackofficeservices.services.TravelProductService;
import de.hybris.platform.travelservices.model.TravelProductModel;
import de.hybris.platform.travelservices.model.warehouse.AccommodationOfferingModel;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Vlayout;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.config.jaxb.wizard.ViewType;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.configurableflow.renderer.DefaultCustomViewRenderer;


/**
 * This is used to Render the Modify Inventory wizard
 */
public class ManageAccommodationInventoryRenderer extends DefaultCustomViewRenderer
{

	protected static final String STOCK_LEVEL_SEARCH_FIELD = "stockLevelSearchField";
	protected static final String PARENT_OBJECT = "parentObject";
	protected static final String TRAVEL_PRODUCT_TYPE_DROPDOWN_EDITOR = "de.hybris.platform.accommodationbackoffice.editor.travelproducttypedropdown";
	protected static final String STOCK_LEVEL_FINDER_EDITOR = "de.hybris.platform.commerceservices.backoffice.editor.stocklevelfindereditor";
	private TravelProductService travelProductService;

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
		final Div container = new Div();
		container.setVisible(true);
		final Div propertyDiv = new Div();
		final Div productTypeDiv = new Div();
		final Div productDiv = new Div();
		final Div searchButtonDiv = new Div();

		final Label propertyLabel = new Label(
				Labels.getLabel(AccommodationbackofficeConstants.ASSIGN_ROOM_PROPERTY_TITLE) + TravelbackofficeConstants.COLON);
		final Label productTypeLabel = new Label(
				Labels.getLabel(AccommodationbackofficeConstants.ASSIGN_ROOM_PRODUCT_TYPE_TITLE) + TravelbackofficeConstants.COLON);

		propertyDiv.appendChild(propertyLabel);
		propertyDiv.setStyle("height:70px");

		final Editor propertyEditor = createEditor(widgetInstanceManager,
				AccommodationbackofficeConstants.REFERENCE_TYPE + "(" + AccommodationOfferingModel._TYPECODE + ")", null, false);
		propertyEditor.initialize();
		propertyEditor.addEventListener(Editor.ON_VALUE_CHANGED, event -> {
			performAccommodationOfferingChangeAction(widgetInstanceManager, productTypeDiv, productDiv, searchButtonDiv,
					productTypeLabel, event);
		});

		propertyDiv.appendChild(propertyEditor);

		searchButtonDiv.setStyle("margin-top:10px");

		container.appendChild(propertyDiv);
		container.appendChild(productTypeDiv);
		container.appendChild(productDiv);
		container.appendChild(searchButtonDiv);
		return container;
	}

	/**
	 * @param widgetInstanceManager
	 * @param productTypeDiv
	 * @param productDiv
	 * @param searchButtonDiv
	 * @param productTypeLabel
	 * @param event
	 */
	protected void performAccommodationOfferingChangeAction(final WidgetInstanceManager widgetInstanceManager,
			final Div productTypeDiv, final Div productDiv, final Div searchButtonDiv, final Label productTypeLabel,
			final Event event)
	{
		final AccommodationOfferingModel accommodationoffering = (AccommodationOfferingModel) ((Editor) event.getTarget())
				.getValue();
		widgetInstanceManager.getModel().put(AccommodationbackofficeConstants.ACCOMMODATION_OFFERING, accommodationoffering);

		if (Objects.isNull(accommodationoffering))
		{
			productTypeDiv.getChildren().removeAll(productTypeDiv.getChildren());
			productDiv.getChildren().removeAll(productDiv.getChildren());
			searchButtonDiv.getChildren().removeAll(searchButtonDiv.getChildren());
			searchButtonDiv.setVisible(false);
		}
		else
		{
			productTypeDiv.setVisible(true);
			productTypeDiv.appendChild(productTypeLabel);
			populateProductType(widgetInstanceManager, productTypeDiv, productDiv);

			final Div searchStockForWarehouseDiv = new Div();
			searchStockForWarehouseDiv.setStyle("height:50px");
			final Editor stockFinderEditor = createEditor(widgetInstanceManager,
					AccommodationbackofficeConstants.REFERENCE_TYPE + "(" + StockLevelModel._TYPECODE + ")", STOCK_LEVEL_FINDER_EDITOR,
					true);

			final Map<String, Object> stockFinderEditorParam = new HashMap<>();
			stockFinderEditorParam.put(STOCK_LEVEL_SEARCH_FIELD, "warehouse");
			stockFinderEditorParam.put(PARENT_OBJECT, accommodationoffering);
			stockFinderEditor.setParameters(stockFinderEditorParam);
			stockFinderEditor.initialize();
			searchStockForWarehouseDiv.appendChild(stockFinderEditor);
			searchButtonDiv.appendChild(searchStockForWarehouseDiv);
			searchButtonDiv.setVisible(true);

		}
	}

	/**
	 * @param widgetInstanceManager
	 * @param productTypeDiv
	 * @param roomTypeDiv
	 */
	protected void populateProductType(final WidgetInstanceManager widgetInstanceManager, final Div productTypeDiv,
			final Div roomTypeDiv)
	{
		final AccommodationOfferingModel accommodationOfferingModel = widgetInstanceManager.getModel()
				.getValue(AccommodationbackofficeConstants.ACCOMMODATION_OFFERING, AccommodationOfferingModel.class);

		if (Objects.nonNull(accommodationOfferingModel))
		{
			final Editor accommodationEditor = createEditor(widgetInstanceManager,
					AccommodationbackofficeConstants.REFERENCE_TYPE + "(" + TravelProductModel._TYPECODE + ")",
					TRAVEL_PRODUCT_TYPE_DROPDOWN_EDITOR, true);

			final Map<String, Object> accommodationParam = new HashMap<>();
			accommodationParam.put(AccommodationbackofficeConstants.ACCOMMODATIONS,
					geTravelProductService().getProductsForAccommodationOffering(accommodationOfferingModel.getCode()));
			accommodationParam.put("isDisabled", false);
			accommodationParam.put("isOtherFieldRequired", true);
			accommodationParam.put(AccommodationbackofficeConstants.WIDGET_MANAGER, widgetInstanceManager);
			accommodationEditor.setParameters(accommodationParam);
			accommodationEditor.initialize();
			productTypeDiv.appendChild(accommodationEditor);

		}
	}

	/**
	 * @param widgetInstanceManager
	 * @param type
	 * @param defaultEditor
	 * @param isReadOnly
	 * @return
	 */
	protected Editor createEditor(final WidgetInstanceManager widgetInstanceManager, final String type, final String defaultEditor,
			final boolean isReadOnly)
	{
		final Editor editor = new Editor();
		editor.setReadOnly(isReadOnly);
		editor.setNestedObjectCreationDisabled(false);
		editor.setWidgetInstanceManager(widgetInstanceManager);
		editor.setType(type);
		editor.setDefaultEditor(defaultEditor);
		return editor;
	}

	/**
	 * @return
	 */
	protected TravelProductService geTravelProductService()
	{
		return travelProductService;
	}

	/**
	 * @param travelProductService
	 */
	@Required
	public void setTravelProductService(final TravelProductService travelProductService)
	{
		this.travelProductService = travelProductService;
	}

}
