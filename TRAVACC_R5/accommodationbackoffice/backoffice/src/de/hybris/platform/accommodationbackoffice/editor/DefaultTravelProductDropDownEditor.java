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

package de.hybris.platform.accommodationbackoffice.editor;

import de.hybris.platform.accommodationbackoffice.constants.AccommodationbackofficeConstants;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.travelservices.model.TravelProductModel;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModelList;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.editors.CockpitEditorRenderer;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.engine.WidgetInstanceManager;


/**
 * Default combo box editor for travel products of Modify Inventory wizard filtered based on the Product type selected.
 */
public class DefaultTravelProductDropDownEditor implements CockpitEditorRenderer<TravelProductModel>
{

	protected static final String STOCK_LEVEL_FINDER_EDITOR = "de.hybris.platform.commerceservices.backoffice.editor.stocklevelfindereditor";
	protected static final String STOCK_LEVEL_SEARCH_FIELD = "stockLevelSearchField";
	protected static final String PARENT_OBJECT = "parentObject";

	@Override
	public void render(final Component parent, final EditorContext<TravelProductModel> context,
			final EditorListener<TravelProductModel> listener)
	{
		final Combobox combobox = new Combobox();
		combobox.setId((String) context.getParameter("editorProperty"));
		final List<TravelProductModel> filteredTravelProducts = Objects
				.isNull(context.getParameter(AccommodationbackofficeConstants.ROOM_TYPES)) ? Collections.emptyList()
						: (List<TravelProductModel>) context.getParameter(AccommodationbackofficeConstants.ROOM_TYPES);

		final ListModelList<TravelProductModel> model = new ListModelList<TravelProductModel>(filteredTravelProducts);
		combobox.setModel(model);
		combobox.setParent(parent);
		final boolean isDisabled = context.getParameterAsBoolean("isDisabled", false);
		if (isDisabled)
		{
			combobox.setValue(null);
		}
		combobox.setDisabled(isDisabled);
		combobox.setReadonly(true);
		combobox.setAutodrop(true);
		combobox.setItemRenderer(new ComboitemRenderer<TravelProductModel>()
		{
			@Override
			public void render(final Comboitem item, final TravelProductModel data, final int index)
			{
				item.setValue(data);
				item.setLabel(data.getCode());
			}
		});
		combobox.addEventListener(Events.ON_SELECT, event -> {
			final Div searchButtonDiv = (Div) (event.getTarget().getParent().getParent().getParent().getChildren().get(3));
			searchButtonDiv.setVisible(true);

			final WidgetInstanceManager widgetInstanceManager = (WidgetInstanceManager) context
					.getParameter(AccommodationbackofficeConstants.WIDGET_MANAGER);
			final TravelProductModel travelProductModel = ((Combobox) event.getTarget()).getSelectedItem().getValue();
			widgetInstanceManager.getModel().put(AccommodationbackofficeConstants.SELECTED_ACCOMMODATION, travelProductModel);

			final Div searchStockForProductDiv = new Div();
			searchStockForProductDiv.setStyle("height:50px");
			final Editor stockFinderEditor = createEditor(widgetInstanceManager,
					AccommodationbackofficeConstants.REFERENCE_TYPE + "(" + StockLevelModel._TYPECODE + ")", STOCK_LEVEL_FINDER_EDITOR,
					true);

			final Map<String, Object> stockFinderEditorParam = new HashMap<>();
			stockFinderEditorParam.put(STOCK_LEVEL_SEARCH_FIELD, "product");
			stockFinderEditorParam.put(PARENT_OBJECT, travelProductModel);
			stockFinderEditor.setParameters(stockFinderEditorParam);
			stockFinderEditor.initialize();
			searchStockForProductDiv.appendChild(stockFinderEditor);
			if (searchButtonDiv.getChildren().size() > 1)
			{
				searchButtonDiv.getChildren().remove(1);
			}
			searchButtonDiv.appendChild(searchStockForProductDiv);
			searchButtonDiv.setVisible(true);
		});
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
}
