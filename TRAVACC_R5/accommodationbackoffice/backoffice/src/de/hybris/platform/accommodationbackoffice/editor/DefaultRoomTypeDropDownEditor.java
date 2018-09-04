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
import de.hybris.platform.travelservices.model.product.AccommodationModel;

import java.util.Collections;
import java.util.List;
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
 * Default combo box editor for accommodations room type of Assign Room Number and Floor filtered based on the Product
 * type selected.
 */
public class DefaultRoomTypeDropDownEditor implements CockpitEditorRenderer<AccommodationModel>
{

	@Override
	public void render(final Component parent, final EditorContext<AccommodationModel> context,
			final EditorListener<AccommodationModel> listener)
	{
		final Combobox combobox = new Combobox();
		combobox.setId((String) context.getParameter("editorProperty"));
		final List<AccommodationModel> filteredAccommodations = Objects
				.isNull(context.getParameter(AccommodationbackofficeConstants.ROOM_TYPES)) ? Collections.emptyList()
						: (List<AccommodationModel>) context.getParameter(AccommodationbackofficeConstants.ROOM_TYPES);

		final ListModelList<AccommodationModel> model = new ListModelList<AccommodationModel>(filteredAccommodations);
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
		combobox.setId("selectedRoomType");
		combobox.setItemRenderer(new ComboitemRenderer<AccommodationModel>()
		{
			@Override
			public void render(final Comboitem item, final AccommodationModel data, final int index)
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
			final AccommodationModel accommodationModel = ((Combobox) event.getTarget()).getSelectedItem().getValue();
			widgetInstanceManager.getModel().put(AccommodationbackofficeConstants.SELECTED_ACCOMMODATION, accommodationModel);
			final Div roomFloorConfigurationDiv = (Div) (event.getTarget().getParent().getParent().getParent().getChildren()
					.get(4));
			roomFloorConfigurationDiv.getChildren().forEach(component -> roomFloorConfigurationDiv.removeChild(component));
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
