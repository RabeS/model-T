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
import de.hybris.platform.travelbackoffice.constants.TravelbackofficeConstants;
import de.hybris.platform.travelservices.model.product.AccommodationModel;
import de.hybris.platform.travelservices.model.warehouse.AccommodationOfferingModel;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Vlayout;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.config.jaxb.wizard.ViewType;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.configurableflow.renderer.DefaultCustomViewRenderer;


/**
 * This created the View for the Assign Room and Floor Wizard
 */
public class AccommodationInventoryRenderer extends DefaultCustomViewRenderer
{
	private static final String MODEL_LIST_VALUES = "modelListValues";
	private static final String DATE_TYPE_EDITOR = "java.util.Date";
	private static final String STRING_TYPE_EDITOR = "java.lang.String";
	protected static final String PRODUCTTYPE_DROPDOWN_EDITOR = "de.hybris.platform.accommodationbackoffice.editor.producttypedropdown";
	protected static final String PRODUCTTYPE_DROPDOWN_EDITOR_VALUE = "product.type.dropdown.selected.value";
	private Map<String, String> accommodationTypes;

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
		final Div accommodationOfferingDiv = new Div();
		final Div accommodationTypeDiv = new Div();
		final Div startDateDiv = new Div();
		final Div endDateDiv = new Div();

		final Label accommodationOfferingLabel = new Label(
				Labels.getLabel(AccommodationbackofficeConstants.ASSIGN_ROOM_PROPERTY_TITLE) + TravelbackofficeConstants.COLON);
		final Label accommodationTypeLabel = new Label(
				Labels.getLabel(AccommodationbackofficeConstants.ASSIGN_ROOM_PRODUCT_TYPE_TITLE) + TravelbackofficeConstants.COLON);
		final Label startDateLabel = new Label(
				Labels.getLabel(AccommodationbackofficeConstants.START_DATE) + TravelbackofficeConstants.COLON);
		final Label endDateLabel = new Label(
				Labels.getLabel(AccommodationbackofficeConstants.END_DATE) + TravelbackofficeConstants.COLON);

		accommodationOfferingDiv.appendChild(accommodationOfferingLabel);
		accommodationOfferingDiv.setStyle("height:70px");

		final Editor accommodationOfferingEditor = (Editor) createEditor(widgetInstanceManager,
				AccommodationbackofficeConstants.REFERENCE_TYPE + "(" + AccommodationOfferingModel._TYPECODE + ")", null, false,
				new HashMap<>());

		final AccommodationOfferingModel accommodationofferingValue = widgetInstanceManager.getModel()
				.getValue(AccommodationbackofficeConstants.ACCOMMODATION_OFFERING, AccommodationOfferingModel.class);
		if (Objects.nonNull(accommodationofferingValue))
		{
			accommodationOfferingEditor.setValue(accommodationofferingValue);
		}

		accommodationOfferingEditor.addEventListener(Editor.ON_VALUE_CHANGED, event -> {
			performAccommodationOfferingChangeAction(widgetInstanceManager, accommodationTypeDiv, accommodationTypeLabel, event);

		});

		accommodationOfferingDiv.appendChild(accommodationOfferingEditor);
		container.appendChild(accommodationOfferingDiv);

		accommodationTypeDiv.appendChild(accommodationTypeLabel);
		accommodationTypeDiv.setStyle("height:70px");

		final Map<String, Object> accommodationParam = new HashMap<>();
		ListModelList<String> modelListValues = null;
		if (Objects.nonNull(accommodationofferingValue))
		{
			modelListValues = new ListModelList<>(getAccommodationTypes().keySet());
		}
		else
		{
			modelListValues = new ListModelList<>(new HashSet<>());
		}
		accommodationParam.put("isDisabled", false);
		accommodationParam.put(AccommodationbackofficeConstants.WIDGET_MANAGER, widgetInstanceManager);
		accommodationParam.put(MODEL_LIST_VALUES, modelListValues);

		final Editor productTypeEditor = (Editor) createEditor(widgetInstanceManager, STRING_TYPE_EDITOR,
				PRODUCTTYPE_DROPDOWN_EDITOR, true, accommodationParam);

		productTypeEditor.initialize();
		widgetInstanceManager.getModel().put(PRODUCTTYPE_DROPDOWN_EDITOR, productTypeEditor);

		accommodationTypeDiv.appendChild(productTypeEditor);
		container.appendChild(accommodationTypeDiv);

		startDateDiv.appendChild(startDateLabel);
		startDateDiv.setStyle("height:70px");

		final Editor startDateEditor = (Editor) createEditor(widgetInstanceManager, DATE_TYPE_EDITOR, null, false, new HashMap<>());
		startDateEditor.addEventListener(Editor.ON_VALUE_CHANGED, event -> {
			widgetInstanceManager.getModel().put(AccommodationbackofficeConstants.START_DATE,
					((Editor) event.getTarget()).getValue());
		});

		final Date startDateEditorValue = widgetInstanceManager.getModel().getValue(AccommodationbackofficeConstants.START_DATE,
				Date.class);
		if (Objects.nonNull(startDateEditorValue))
		{
			startDateEditor.setValue(startDateEditorValue);
		}

		startDateDiv.appendChild(startDateEditor);
		container.appendChild(startDateDiv);

		endDateDiv.appendChild(endDateLabel);
		endDateDiv.setStyle("height:70px");

		final Editor endDateEditor = (Editor) createEditor(widgetInstanceManager, DATE_TYPE_EDITOR, null, false, new HashMap<>());
		endDateEditor.addEventListener(Editor.ON_VALUE_CHANGED, event -> {
			widgetInstanceManager.getModel().put(AccommodationbackofficeConstants.END_DATE, ((Editor) event.getTarget()).getValue());
		});

		final Date endDateEditorValue = widgetInstanceManager.getModel().getValue(AccommodationbackofficeConstants.END_DATE,
				Date.class);
		if (Objects.nonNull(endDateEditorValue))
		{
			endDateEditor.setValue(endDateEditorValue);
		}
		endDateDiv.appendChild(endDateEditor);
		container.appendChild(endDateDiv);
		return container;
	}

	/**
	 * @param widgetInstanceManager
	 * @param accommodationTypeDiv
	 * @param accommodationTypeLabel
	 * @param event
	 */
	protected void performAccommodationOfferingChangeAction(final WidgetInstanceManager widgetInstanceManager,
			final Div accommodationTypeDiv, final Label accommodationTypeLabel, final Event event)
	{
		accommodationTypeDiv.getChildren().removeAll(accommodationTypeDiv.getChildren());
		accommodationTypeDiv.appendChild(accommodationTypeLabel);
		final Map<String, Object> accommodationParam = new HashMap<>();
		final AccommodationOfferingModel accommodationoffering = (AccommodationOfferingModel) ((Editor) event.getTarget())
				.getValue();
		widgetInstanceManager.getModel().put(AccommodationbackofficeConstants.ACCOMMODATION_OFFERING, accommodationoffering);

		ListModelList<String> modelListValues = null;
		if (Objects.isNull(accommodationoffering))
		{
			widgetInstanceManager.getModel().put(AccommodationbackofficeConstants.SELECTED_ACCOMMODATION, (Strings.EMPTY));
			modelListValues = new ListModelList<>(new HashSet<>());
		}
		else
		{
			modelListValues = new ListModelList<>(getAccommodationTypes().keySet());
		}
		accommodationParam.put("isDisabled", false);
		accommodationParam.put(AccommodationbackofficeConstants.WIDGET_MANAGER, widgetInstanceManager);
		accommodationParam.put(MODEL_LIST_VALUES, modelListValues);

		final Editor accommodationEditor = (Editor) createEditor(widgetInstanceManager,
				AccommodationbackofficeConstants.REFERENCE_TYPE + "(" + AccommodationModel._TYPECODE + ")",
				PRODUCTTYPE_DROPDOWN_EDITOR, true, accommodationParam);

		accommodationTypeDiv.appendChild(accommodationEditor);
	}

	/**
	 * @param widgetInstanceManager
	 * @param type
	 * @param accommodationDropdownEditor
	 * @param nestedObjectCreation
	 * @param editorParams
	 * @return
	 */
	protected Component createEditor(final WidgetInstanceManager widgetInstanceManager, final String type,
			final String defaultEditor, final boolean nestedObjectCreation, final Map<String, Object> editorParams)
	{
		final Editor editor = new Editor();
		editor.setNestedObjectCreationDisabled(nestedObjectCreation);
		editor.setWidgetInstanceManager(widgetInstanceManager);
		editor.setType(type);
		if (Objects.nonNull(defaultEditor))
		{
			editor.setDefaultEditor(defaultEditor);
		}
		editor.setParameters(editorParams);
		editor.initialize();
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
	@Required
	public void setAccommodationTypes(final Map<String, String> accommodationTypes)
	{
		this.accommodationTypes = accommodationTypes;
	}

}
