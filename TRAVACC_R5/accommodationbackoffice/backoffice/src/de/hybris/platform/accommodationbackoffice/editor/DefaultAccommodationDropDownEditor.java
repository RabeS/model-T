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
import de.hybris.platform.travelservices.model.product.AccommodationModel;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
 * Default combo box editor for accommodations product type of Assign Room Number and Floor wizard populated based on
 * the Accommodation offering selected.
 */
/**
 *
 */
public class DefaultAccommodationDropDownEditor implements CockpitEditorRenderer<AccommodationModel>
{

	protected static final String DEFAULT_CATALOG_VERSION = "Online";
	protected static final String STOCK_LEVEL_FINDER_EDITOR = "de.hybris.platform.commerceservices.backoffice.editor.stocklevelfindereditor";

	@Override
	public void render(final Component parent, final EditorContext<AccommodationModel> context,
			final EditorListener<AccommodationModel> listener)
	{
		final Combobox combobox = new Combobox();
		combobox.setId((String) context.getParameter("editorProperty"));
		final List<AccommodationModel> accommodations = Objects
				.isNull(context.getParameter(AccommodationbackofficeConstants.ACCOMMODATIONS)) ? Collections.emptyList()
						: (List<AccommodationModel>) context.getParameter(AccommodationbackofficeConstants.ACCOMMODATIONS);

		final Map<String, AccommodationModel> accommodationTypeMap = new HashMap<>();

		for (final AccommodationModel accommodationModel : accommodations)
		{
			if (!accommodationTypeMap.containsKey(accommodationModel.getItemtype()))
			{
				accommodationTypeMap.put(accommodationModel.getItemtype(), accommodationModel);
			}
		}
		final ListModelList<AccommodationModel> model = new ListModelList<>(accommodationTypeMap.values());
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

		combobox.setItemRenderer(new ComboitemRenderer<AccommodationModel>()
		{
			@Override
			public void render(final Comboitem item, final AccommodationModel data, final int index)
			{
				item.setValue(data);
				item.setLabel(data.getItemtype());
			}
		});

		combobox.addEventListener(Events.ON_SELECT, event -> {
			final Boolean isOtherFieldRequired = context.getParameterAsBoolean("isOtherFieldRequired", false);
			if (isOtherFieldRequired)
			{
				appendRoomTypeDropDownEditor(combobox, context);
			}
		});
	}

	/**
	 * @param combobox
	 * @param context
	 */
	protected void appendRoomTypeDropDownEditor(final Combobox combobox, final EditorContext<AccommodationModel> context)
	{
		final AccommodationModel selectedVal = getSelectedItemValue(combobox);
		final WidgetInstanceManager widgetInstanceManager = (WidgetInstanceManager) context
				.getParameter(AccommodationbackofficeConstants.WIDGET_MANAGER);
		final Component divComponent = combobox.getParent().getParent().getParent();
		final Div roomTypeDiv = (Div) divComponent.getChildren().get(2);
		final Div searchButtonDiv = (Div) divComponent.getChildren().get(3);
		final Div roomFloorConfigurationDiv = (Div) divComponent.getChildren().get(4);
		roomFloorConfigurationDiv.getChildren().removeAll(roomFloorConfigurationDiv.getChildren());
		searchButtonDiv.setVisible(false);
		roomTypeDiv.getChildren().removeAll(roomTypeDiv.getChildren());
		final Label roomTypeLabel = new Label(
				Labels.getLabel(AccommodationbackofficeConstants.ASSIGN_ROOM_ROOM_TYPE_TITLE) + TravelbackofficeConstants.COLON);
		roomTypeDiv.appendChild(roomTypeLabel);

		roomTypeDiv.setVisible(true);
		final Editor roomTypeDropDownEditor = createRoomTypeDropDownEditor(widgetInstanceManager,
				AccommodationbackofficeConstants.REFERENCE_TYPE + "(" + selectedVal.getItemtype() + ")",
				AccommodationbackofficeConstants.ROOMTYPE_DROPDOWN_EDITOR, Events.ON_OPEN, true, roomTypeDiv);

		final Map<String, Object> roomTypeParam = new HashMap<>();
		roomTypeParam.put(AccommodationbackofficeConstants.ROOM_TYPES, getAccommodationsForType(selectedVal,
				(List<AccommodationModel>) context.getParameter(AccommodationbackofficeConstants.ACCOMMODATIONS)));
		roomTypeParam.put("isDisabled", false);
		roomTypeParam.put(AccommodationbackofficeConstants.WIDGET_MANAGER, widgetInstanceManager);
		roomTypeDropDownEditor.setParameters(roomTypeParam);
		roomTypeDropDownEditor.initialize();

		roomTypeDiv.appendChild(roomTypeDropDownEditor);

	}

	/**
	 * Returns selected {@link AccommodationModel} from combobox.
	 *
	 * @param combobox
	 */
	protected AccommodationModel getSelectedItemValue(final Combobox combobox)
	{
		AccommodationModel selectedVal = null;

		final Comboitem selectedItem = combobox.getSelectedItem();
		if (selectedItem != null)
		{
			selectedVal = selectedItem.getValue();
		}
		return selectedVal;
	}

	/**
	 * @param selectedVal
	 * @param accommodations
	 * @return
	 */
	protected List<AccommodationModel> getAccommodationsForType(final AccommodationModel selectedVal,
			final List<AccommodationModel> accommodations)
	{
		return accommodations.stream().filter(selectedVal.getClass()::isInstance)
				.filter(accommodation -> accommodation.getCatalogVersion().getVersion().equals(DEFAULT_CATALOG_VERSION))
				.map(selectedVal.getClass()::cast).collect(Collectors.toList());
	}

	/**
	 * @param widgetInstanceManager
	 * @param type
	 * @param roomtypeDropdownEditor
	 * @param onOpen
	 * @param isReadOnly
	 * @param roomTypeDiv
	 * @return
	 */
	protected Editor createRoomTypeDropDownEditor(final WidgetInstanceManager widgetInstanceManager, final String type,
			final String roomtypeDropdownEditor, final String onOpen, final boolean isReadOnly, final Div roomTypeDiv)
	{
		final Editor editor = new Editor();
		editor.setReadOnly(isReadOnly);
		editor.setNestedObjectCreationDisabled(false);
		editor.setWidgetInstanceManager(widgetInstanceManager);
		editor.setType(type);

		editor.setDefaultEditor(roomtypeDropdownEditor);

		editor.addEventListener(Events.ON_SELECT, event1 -> {
			((Combobox) event1.getTarget()).getValue();
		});
		return editor;
	}
}
