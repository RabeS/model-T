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
import de.hybris.platform.travelbackoffice.constants.TravelbackofficeConstants;
import de.hybris.platform.travelservices.model.TravelProductModel;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.editors.CockpitEditorRenderer;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.engine.WidgetInstanceManager;


/**
 * Default combo box editor for travel product type of Modify Inventory wizard populated based on the Accommodation
 * offering selected.
 */
public class DefaultTravelProductTypeDropDownEditor implements CockpitEditorRenderer<TravelProductModel>
{

	protected static final String DEFAULT_CATALOG_VERSION = "Online";
	public static final String TRAVEL_PRODUCT_DROPDOWN_EDITOR = "de.hybris.platform.accommodationbackoffice.editor.travelproductdropdown";

	@Resource(name = "accommodationTypeMap")
	private Map<String, String> accommodationTypes;

	@Override
	public void render(final Component parent, final EditorContext<TravelProductModel> context,
			final EditorListener<TravelProductModel> listener)
	{
		final Combobox combobox = new Combobox();
		combobox.setId((String) context.getParameter("editorProperty"));
		final List<TravelProductModel> travelProducts = Objects
				.isNull(context.getParameter(AccommodationbackofficeConstants.ACCOMMODATIONS)) ? Collections.emptyList()
						: (List<TravelProductModel>) context.getParameter(AccommodationbackofficeConstants.ACCOMMODATIONS);

		final Map<String, TravelProductModel> travelProductsTypeMap = new HashMap<>();

		for (final TravelProductModel travelProductModel : travelProducts)
		{
			if (!travelProductsTypeMap.containsKey(travelProductModel.getItemtype()))
			{
				travelProductsTypeMap.put(travelProductModel.getItemtype(), travelProductModel);
			}
		}
		final ListModelList<TravelProductModel> model = new ListModelList<>(travelProductsTypeMap.values());
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
				if (MapUtils.isEmpty(getAccommodationTypes()))
				{
					item.setLabel(data.getItemtype());
				}
				else
				{
					item.setLabel(Labels.getLabel(getAccommodationTypes().get(data.getItemtype())));
				}
			}
		});

		combobox.addEventListener(Events.ON_SELECT, event -> {
			final Boolean isOtherFieldRequired = context.getParameterAsBoolean("isOtherFieldRequired", false);
			if (isOtherFieldRequired)
			{
				appendTravelProductDropDownEditor(combobox, context);
			}
			final Div searchButtonDiv = (Div) (event.getTarget().getParent().getParent().getParent().getChildren().get(3));
			if (searchButtonDiv.getChildren().size() > 1)
			{
				searchButtonDiv.getChildren().remove(1);
			}
		});
	}

	/**
	 * @param combobox
	 * @param context
	 */
	protected void appendTravelProductDropDownEditor(final Combobox combobox, final EditorContext<TravelProductModel> context)
	{
		final TravelProductModel selectedVal = getSelectedItemValue(combobox);
		final WidgetInstanceManager widgetInstanceManager = (WidgetInstanceManager) context
				.getParameter(AccommodationbackofficeConstants.WIDGET_MANAGER);

		final Component divComponent = combobox.getParent().getParent().getParent();
		final Div travelProductDiv = (Div) divComponent.getChildren().get(2);
		travelProductDiv.getChildren().removeAll(travelProductDiv.getChildren());
		final Label travelProductLabel = new Label(
				Labels.getLabel(AccommodationbackofficeConstants.MODIFY_PRODUCT_TITLE) + TravelbackofficeConstants.COLON);
		travelProductDiv.appendChild(travelProductLabel);
		travelProductDiv.setVisible(true);

		final Editor travelProductDropDownEditor = createTravelProductDropDownEditor(widgetInstanceManager,
				AccommodationbackofficeConstants.REFERENCE_TYPE + "(" + selectedVal.getItemtype() + ")",
				TRAVEL_PRODUCT_DROPDOWN_EDITOR, Events.ON_OPEN, true, travelProductDiv);

		final Map<String, Object> travelProductsParam = new HashMap<>();
		travelProductsParam.put(AccommodationbackofficeConstants.ROOM_TYPES, getTravelProductsForType(selectedVal,
				(List<TravelProductModel>) context.getParameter(AccommodationbackofficeConstants.ACCOMMODATIONS)));
		travelProductsParam.put("isDisabled", false);
		travelProductsParam.put(AccommodationbackofficeConstants.WIDGET_MANAGER, widgetInstanceManager);

		travelProductDropDownEditor.setParameters(travelProductsParam);
		travelProductDropDownEditor.initialize();

		travelProductDiv.appendChild(travelProductDropDownEditor);

	}

	/**
	 * Returns selected {@link TravelProductModel} from combobox.
	 *
	 * @param combobox
	 */
	protected TravelProductModel getSelectedItemValue(final Combobox combobox)
	{
		TravelProductModel selectedVal = null;

		final Comboitem selectedItem = combobox.getSelectedItem();
		if (selectedItem != null)
		{
			selectedVal = selectedItem.getValue();
		}
		return selectedVal;
	}

	/**
	 * @param selectedVal
	 * @param travelProducts
	 * @return
	 */
	protected List<TravelProductModel> getTravelProductsForType(final TravelProductModel selectedVal,
			final List<TravelProductModel> travelProducts)
	{
		return travelProducts.stream().filter(selectedVal.getClass()::isInstance)
				.filter(travelProduct -> travelProduct.getCatalogVersion().getVersion().equals(DEFAULT_CATALOG_VERSION))
				.map(selectedVal.getClass()::cast).collect(Collectors.toList());
	}

	/**
	 * @param widgetInstanceManager
	 * @param type
	 * @param travelProductDropdownEditor
	 * @param onOpen
	 * @param isReadOnly
	 * @param travelProductDiv
	 * @return
	 */
	protected Editor createTravelProductDropDownEditor(final WidgetInstanceManager widgetInstanceManager, final String type,
			final String travelProductDropdownEditor, final String onOpen, final boolean isReadOnly, final Div travelProductDiv)
	{
		final Editor editor = new Editor();
		editor.setReadOnly(isReadOnly);
		editor.setNestedObjectCreationDisabled(false);
		editor.setWidgetInstanceManager(widgetInstanceManager);
		editor.setType(type);

		editor.setDefaultEditor(travelProductDropdownEditor);

		editor.addEventListener(Events.ON_SELECT, event1 -> {
			((Combobox) event1.getTarget()).getValue();
		});
		return editor;
	}

	/**
	 * @return
	 */
	protected Map<String, String> getAccommodationTypes()
	{
		return accommodationTypes;
	}

	/**
	 * @param accommodationTypes
	 */
	public void setAccommodationTypes(final Map<String, String> accommodationTypes)
	{
		this.accommodationTypes = accommodationTypes;
	}

}
