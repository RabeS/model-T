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

import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.ListModelList;

import com.hybris.cockpitng.editors.CockpitEditorRenderer;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.engine.WidgetInstanceManager;


/**
 * Default combo box editor for accommodations product type of Assign Room Number and Floor wizard populated based on
 * the Accommodation offering selected.
 */
public class DefaultProductTypeDropDownEditor implements CockpitEditorRenderer<String>
{
	@Resource(name = "accommodationTypeMap")
	private Map<String, String> accommodationTypes;

	@Override
	public void render(final Component parent, final EditorContext<String> context, final EditorListener<String> listener)
	{
		final Combobox combobox = new Combobox();
		combobox.setId((String) context.getParameter("editorProperty"));

		final ListModelList<String> model = (ListModelList<String>) context.getParameter("modelListValues");
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

		combobox.setItemRenderer(new ComboitemRenderer<String>()
		{
			@Override
			public void render(final Comboitem item, final String data, final int index)
			{
				item.setValue(data);

				if (Objects.isNull(getAccommodationTypes()))
				{
					item.setLabel(data);
				}
				else
				{
					item.setLabel(getAccommodationTypes().get(data));
				}
			}
		});

		final WidgetInstanceManager widgetInstanceManager = (WidgetInstanceManager) context
				.getParameter(AccommodationbackofficeConstants.WIDGET_MANAGER);

		final String accommodationValue = widgetInstanceManager.getModel()
				.getValue(AccommodationbackofficeConstants.SELECTED_ACCOMMODATION, String.class);
		if (Objects.nonNull(accommodationValue))
		{
			combobox.setValue(accommodationValue);
		}

		combobox.addEventListener(Events.ON_SELECT, event -> {

			final String selectedAccommodationValue = getAccommodationTypes()
					.get(((Combobox) event.getTarget()).getSelectedItem().getValue());
			widgetInstanceManager.getModel().put(AccommodationbackofficeConstants.SELECTED_ACCOMMODATION,
					selectedAccommodationValue);
			widgetInstanceManager.getModel().put(AccommodationbackofficeConstants.SELECTED_ACCOMMODATION_TYPE,
					((Combobox) event.getTarget()).getSelectedItem().getValue());
		});
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
