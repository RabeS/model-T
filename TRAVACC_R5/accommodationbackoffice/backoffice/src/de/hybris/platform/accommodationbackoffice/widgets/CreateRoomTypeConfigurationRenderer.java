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
import de.hybris.platform.travelservices.model.accommodation.RoomFloorConfigurationModel;
import de.hybris.platform.travelservices.model.product.AccommodationModel;
import de.hybris.platform.travelservices.model.warehouse.AccommodationOfferingModel;
import de.hybris.platform.travelservices.services.AccommodationService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.config.jaxb.wizard.ViewType;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.configurableflow.renderer.DefaultCustomViewRenderer;


/**
 * This created the View for the Assign Room and Floor Wizard
 */
public class CreateRoomTypeConfigurationRenderer extends DefaultCustomViewRenderer
{

	private EventListener<Event> addButtonEventListener;
	private EventListener<Event> removeButtonEventListener;
	private AccommodationService defaultAccommodationService;
	protected static final String ACCOMMODATION_DROPDOWN_EDITOR = "de.hybris.platform.accommodationbackoffice.editor.accommodationdropdown";
	protected static final String SEARCH = "assign.room.and.floor.searchButton.name";

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
		final Div roomTypeDiv = new Div();
		final Div searchButtonDiv = new Div();
		final Div roomFloorConfigurationDiv = new Div();

		final Label propertyLabel = new Label(
				Labels.getLabel(AccommodationbackofficeConstants.ASSIGN_ROOM_PROPERTY_TITLE) + TravelbackofficeConstants.COLON);
		final Label productTypeLabel = new Label(
				Labels.getLabel(AccommodationbackofficeConstants.ASSIGN_ROOM_PRODUCT_TYPE_TITLE) + TravelbackofficeConstants.COLON);

		propertyDiv.appendChild(propertyLabel);
		propertyDiv.setStyle("height:70px");

		final Editor propertyEditor = (Editor) createAccommodationOffering(widgetInstanceManager,
				AccommodationbackofficeConstants.REFERENCE_TYPE + "(" + AccommodationOfferingModel._TYPECODE + ")", false);
		propertyEditor.addEventListener(Editor.ON_VALUE_CHANGED, event -> {

			performAccommodationOfferingChangeAction(widgetInstanceManager, productTypeDiv, roomTypeDiv, searchButtonDiv,
					roomFloorConfigurationDiv, productTypeLabel, event);
		});
		propertyDiv.appendChild(propertyEditor);
		container.appendChild(propertyDiv);

		productTypeDiv.setStyle("height:70px");
		productTypeDiv.setVisible(false);
		container.appendChild(productTypeDiv);

		roomTypeDiv.setStyle("height:70px");
		roomTypeDiv.setVisible(false);
		container.appendChild(roomTypeDiv);

		final Button searchButton = new Button();
		searchButton.setStyle("left: 1150px width:100px");
		searchButton.setLabel(Labels.getLabel(SEARCH));
		searchButton.setClass("yw-advancedsearch-search-btn z-button searchFieldAlignRight");
		searchButton.setId("searchButton");
		searchButton.addEventListener(Events.ON_CLICK, event -> {
			performSearchAction(widgetInstanceManager, roomFloorConfigurationDiv, event);
		});

		searchButtonDiv.appendChild(searchButton);
		searchButtonDiv.setVisible(false);
		container.appendChild(searchButtonDiv);

		container.appendChild(roomFloorConfigurationDiv);
		return container;
	}

	/**
	 * @param widgetInstanceManager
	 * @param roomFloorConfigurationDiv
	 * @param event
	 */
	protected void performSearchAction(final WidgetInstanceManager widgetInstanceManager, final Div roomFloorConfigurationDiv,
			final Event event)
	{
		roomFloorConfigurationDiv.setVisible(true);

		if (CollectionUtils.isNotEmpty(roomFloorConfigurationDiv.getChildren()))
		{
			roomFloorConfigurationDiv.getChildren().forEach(component -> roomFloorConfigurationDiv.removeChild(component));
		}
		final Combobox roomTypeCombo = (Combobox) (event.getTarget().getParent().getParent().getChildren().get(2).getChildren()
				.get(1).getChildren().get(0));
		final AccommodationModel selectedAccommodation = roomTypeCombo.getSelectedItem().getValue();
		final Div innerDiv = (Div) createLayoutForRoomFloorConfigurations(widgetInstanceManager,
				selectedAccommodation.getRoomFloorConfigurations());
		roomFloorConfigurationDiv.appendChild(innerDiv);
	}

	/**
	 * @param widgetInstanceManager
	 * @param productTypeDiv
	 * @param roomTypeDiv
	 * @param searchButtonDiv
	 * @param roomFloorConfigurationDiv
	 * @param productTypeLabel
	 * @param event
	 */
	protected void performAccommodationOfferingChangeAction(final WidgetInstanceManager widgetInstanceManager,
			final Div productTypeDiv, final Div roomTypeDiv, final Div searchButtonDiv, final Div roomFloorConfigurationDiv,
			final Label productTypeLabel, final Event event)
	{
		final AccommodationOfferingModel accommodationoffering = (AccommodationOfferingModel) ((Editor) event.getTarget())
				.getValue();

		widgetInstanceManager.getModel().put(AccommodationbackofficeConstants.ACCOMMODATION_OFFERING, accommodationoffering);
		if (Objects.isNull(accommodationoffering))
		{
			productTypeDiv.getChildren().removeAll(productTypeDiv.getChildren());
			roomTypeDiv.getChildren().removeAll(roomTypeDiv.getChildren());
			roomFloorConfigurationDiv.getChildren().removeAll(roomFloorConfigurationDiv.getChildren());
			searchButtonDiv.setVisible(false);
		}
		else
		{
			productTypeDiv.setVisible(true);
			productTypeDiv.appendChild(productTypeLabel);
			populateProductType(widgetInstanceManager, productTypeDiv, roomTypeDiv, searchButtonDiv, roomFloorConfigurationDiv);
		}
	}

	/**
	 * @param widgetInstanceManager
	 * @param productTypeDiv
	 * @param roomTypeDiv
	 * @param searchButtonDiv
	 * @param roomFloorConfigurationDiv
	 */
	protected void populateProductType(final WidgetInstanceManager widgetInstanceManager, final Div productTypeDiv,
			final Div roomTypeDiv, final Div searchButtonDiv, final Div roomFloorConfigurationDiv)
	{
		final AccommodationOfferingModel accommodationOfferingModel = widgetInstanceManager.getModel()
				.getValue(AccommodationbackofficeConstants.ACCOMMODATION_OFFERING, AccommodationOfferingModel.class);

		if (Objects.nonNull(accommodationOfferingModel))
		{
			final Editor accommodationEditor = createAccommodationTypeEditor(widgetInstanceManager,
					AccommodationbackofficeConstants.REFERENCE_TYPE + "(" + AccommodationModel._TYPECODE + ")",
					ACCOMMODATION_DROPDOWN_EDITOR, true);

			final Map<String, Object> accommodationParam = new HashMap<>();
			accommodationParam.put(AccommodationbackofficeConstants.ACCOMMODATIONS,
					getDefaultAccommodationService().getAccommodationForAccommodationOffering(accommodationOfferingModel.getCode()));
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
	protected Editor createAccommodationTypeEditor(final WidgetInstanceManager widgetInstanceManager, final String type,
			final String defaultEditor, final boolean isReadOnly)
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
	 * @param widgetInstanceManager
	 * @param roomFloorConfigurations
	 * @return
	 */
	protected Component createLayoutForRoomFloorConfigurations(final WidgetInstanceManager widgetInstanceManager,
			final List<RoomFloorConfigurationModel> roomFloorConfigurations)
	{
		final Div container = new Div();
		container.setVisible(true);

		final Grid roomFloorConfigurationGrid = new Grid();
		final Column roomNumberColumn = new Column();
		final Column floorNumberColumn = new Column();
		final Column addButtonColumn = new Column();
		final Column deleteButtonColumn = new Column();

		final Columns stockLevelGridColumns = new Columns();
		stockLevelGridColumns.setParent(roomFloorConfigurationGrid);

		final Rows roomFloorConfigurationGridRows = new Rows();
		roomFloorConfigurationGridRows.setParent(roomFloorConfigurationGrid);

		roomFloorConfigurationGrid.setClass("accommodation-configuration-grid");
		roomFloorConfigurationGrid.setSizedByContent(true);
		roomFloorConfigurationGrid.getColumns().appendChild(roomNumberColumn);
		roomFloorConfigurationGrid.getColumns().appendChild(floorNumberColumn);
		roomFloorConfigurationGrid.getColumns().appendChild(addButtonColumn);
		roomFloorConfigurationGrid.getColumns().appendChild(deleteButtonColumn);
		roomFloorConfigurationGrid.setId("searchCreateID");
		final Row row1 = new Row();
		row1.setId(String.valueOf(0));
		final Label roomLabel = new Label(Labels.getLabel(AccommodationbackofficeConstants.ROOM_NUMBER_LABEL));
		final Label floorLabel = new Label(Labels.getLabel(AccommodationbackofficeConstants.FLOOR_NUMBER_LABEL));
		row1.appendChild(roomLabel);
		row1.appendChild(floorLabel);
		row1.appendChild(new Label());
		row1.appendChild(new Label());

		roomFloorConfigurationGrid.getRows().appendChild(row1);

		removeButtonEventListener = event -> {
			if (StringUtils.equals(Events.ON_CLICK, event.getName()))
			{
				if (CollectionUtils.size(roomFloorConfigurationGrid.getRows().getChildren()) > 2)
				{
					final Component removeComponent = event.getTarget().getParent();
					roomFloorConfigurationGrid.getRows().removeChild(removeComponent);
					final Row lastRow = (Row) roomFloorConfigurationGrid.getRows().getLastChild();
					if (lastRow.getIndex() > 0)
					{
						lastRow.getChildren().get(2).setVisible(Boolean.TRUE);
					}
					container.appendChild(roomFloorConfigurationGrid);
					container.getRedrawCallback();
					widgetInstanceManager.getModel().put(AccommodationbackofficeConstants.ROOM_FLOOR_CONFIG_GRID,
							roomFloorConfigurationGrid);
				}
			}
		};
		addButtonEventListener = event -> {
			if (StringUtils.equals(Events.ON_CLICK, event.getName()))
			{
				roomFloorConfigurationGrid.getRows()
						.appendChild(createRoomFloorConfigurationColumnLayout(Integer.parseInt(event.getTarget().getId()) + 1,
								addButtonEventListener, removeButtonEventListener, Boolean.TRUE, null));
				container.appendChild(roomFloorConfigurationGrid);
				event.getTarget().setVisible(Boolean.FALSE);
				container.getRedrawCallback();
				widgetInstanceManager.getModel().put(AccommodationbackofficeConstants.ROOM_FLOOR_CONFIG_GRID,
						roomFloorConfigurationGrid);
			}
		};

		if (CollectionUtils.isNotEmpty(roomFloorConfigurations))
		{
			for (int count = 1; count <= roomFloorConfigurations.size(); count++)
			{
				final boolean isPlusButtonNeedToAdd = roomFloorConfigurations.size() == 1 ? Boolean.TRUE
						: count == roomFloorConfigurations.size();

				roomFloorConfigurationGrid.getRows()
						.appendChild(createRoomFloorConfigurationColumnLayout(1, addButtonEventListener, removeButtonEventListener,
								isPlusButtonNeedToAdd, roomFloorConfigurations.get(count - 1)));
			}
		}
		else
		{
			roomFloorConfigurationGrid.getRows().appendChild(createRoomFloorConfigurationColumnLayout(1,
					addButtonEventListener, removeButtonEventListener, Boolean.TRUE, null));
		}
		container.appendChild(roomFloorConfigurationGrid);
		widgetInstanceManager.getModel().put("roomFloorConfigurationGrid", roomFloorConfigurationGrid);
		return container;
	}

	/**
	 * @param widgetInstanceManager
	 * @param type
	 * @param nestedObjectCreation
	 * @return
	 */
	protected Component createAccommodationOffering(final WidgetInstanceManager widgetInstanceManager, final String type,
			final boolean nestedObjectCreation)
	{
		final Editor editor = new Editor();
		editor.setNestedObjectCreationDisabled(nestedObjectCreation);
		editor.setWidgetInstanceManager(widgetInstanceManager);
		editor.setType(type);
		editor.initialize();
		return editor;
	}

	/**
	 * @param index
	 * @param addButtonlistener
	 * @param removeButtonlistener
	 * @param isPlusButtonNeedToAdd
	 * @param roomFloorConfigurationModel
	 * @return
	 */
	protected Row createRoomFloorConfigurationColumnLayout(final Integer index, final EventListener<Event> addButtonlistener,
			final EventListener<Event> removeButtonlistener, final boolean isPlusButtonNeedToAdd,
			final RoomFloorConfigurationModel roomFloorConfigurationModel)
	{
		final Row row = new Row();
		row.setId(index.toString());
		final Textbox roomNumberTextBox = new Textbox();
		final Textbox floorNumberTextBox = new Textbox();
		row.appendChild(roomNumberTextBox);
		row.appendChild(floorNumberTextBox);
		final Button buttonAdd = new Button();

		if (Objects.nonNull(roomFloorConfigurationModel))
		{
			roomNumberTextBox.setText(roomFloorConfigurationModel.getRoomNumber());
			floorNumberTextBox.setText(roomFloorConfigurationModel.getFloorNumber());
		}

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
	 * @return
	 */
	protected AccommodationService getDefaultAccommodationService()
	{
		return defaultAccommodationService;
	}

	/**
	 * @param defaultAccommodationService
	 */
	@Required
	public void setDefaultAccommodationService(final AccommodationService defaultAccommodationService)
	{
		this.defaultAccommodationService = defaultAccommodationService;
	}
}
