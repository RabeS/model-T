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
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.travelbackoffice.constants.TravelbackofficeConstants;
import de.hybris.platform.travelservices.model.accommodation.RoomFloorConfigurationModel;
import de.hybris.platform.travelservices.model.product.AccommodationModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.StringUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.Button;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;

import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationUtils;
import com.hybris.cockpitng.config.jaxb.wizard.CustomType;
import com.hybris.cockpitng.util.MessageboxUtils;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandler;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandlerAdapter;


/**
 * This saves the Room Floor Configuration for the Selected Accommodation
 */
public class SaveRoomTypeConfigurationHandler implements FlowActionHandler
{

	private ModelService modelService;
	private static final Logger LOG = Logger.getLogger(SaveRoomTypeConfigurationHandler.class);
	protected static final String SUCCESS_MESSAGE = "assign.room.and.floor.notification.success.message";

	@Override
	public void perform(final CustomType customType, final FlowActionHandlerAdapter adapter, final Map<String, String> parameters)
	{

		final AccommodationModel accommodation = adapter.getWidgetInstanceManager().getModel()
				.getValue(AccommodationbackofficeConstants.SELECTED_ACCOMMODATION, AccommodationModel.class);

		final Object grid = adapter.getWidgetInstanceManager().getModel()
				.getValue(AccommodationbackofficeConstants.ROOM_FLOOR_CONFIG_GRID, Grid.class);

		if (Objects.isNull(accommodation) || Objects.isNull(grid))
		{
			adapter.done();
			return;
		}

		if (Objects.nonNull(grid))
		{
			final List<Component> children = ((Grid) grid).getRows().getChildren();
			final int roomFloorConfigurationSize = children.size() - 1;
			accommodation.setRoomFloorConfigurations(new ArrayList<>(roomFloorConfigurationSize));
			try
			{
				children.forEach(child -> {
					final int id = Integer.parseInt(child.getId());
					if (id > 0)
					{
						final RoomFloorConfigurationModel roomFloorConfiguration = createRoomFloorConfiguration(
								(Row) child);
						if (Objects.nonNull(roomFloorConfiguration))
						{
							accommodation.getRoomFloorConfigurations().add(roomFloorConfiguration);
						}
					}
				});

				if (CollectionUtils.size(accommodation.getRoomFloorConfigurations()) < roomFloorConfigurationSize)
				{
					displayErrorMessage(Labels.getLabel(AccommodationbackofficeConstants.ERROR_INCOMPLETE_INFO_MESSAGE),
							Labels.getLabel(AccommodationbackofficeConstants.ERROR_POPUP_TITLE));
				}
				else
				{
					Messagebox.show(Labels.getLabel(AccommodationbackofficeConstants.POPUP_CONFIRMATION_MESSAGE),
							Labels.getLabel(AccommodationbackofficeConstants.POPUP_TITLE_MESSAGE), MessageboxUtils.NO_YES_OPTION,
							"z-messagebox-icon z-messagebox-information", clickEvent -> {

								if (Button.YES.equals(clickEvent.getButton()))
								{
									getModelService().save(accommodation);

									NotificationUtils.notifyUser(
											NotificationUtils.getWidgetNotificationSource(adapter.getWidgetInstanceManager()),
											TravelbackofficeConstants.TRAVEL_GLOBAL_NOTIFICATION_EVENT_TYPE, NotificationEvent.Level.SUCCESS,
											Labels.getLabel(SUCCESS_MESSAGE));

									adapter.done();
								}
							});
				}
			}
			catch (final Exception ex)
			{
				LOG.error("Required information not found.");
				displayErrorMessage(Labels.getLabel(AccommodationbackofficeConstants.ERROR_UNKNOWN_MESSAGE),
						Labels.getLabel(AccommodationbackofficeConstants.ERROR_POPUP_TITLE));
			}
		}
		else
		{
			LOG.error("Required information not found.");
			displayErrorMessage(Labels.getLabel(AccommodationbackofficeConstants.ERROR_UNKNOWN_MESSAGE),
					Labels.getLabel(AccommodationbackofficeConstants.ERROR_POPUP_TITLE));
		}
	}

	/**
	 * @param row
	 * @return
	 */
	protected RoomFloorConfigurationModel createRoomFloorConfiguration(final Row row)
	{
		final List<Component> children = row.getChildren();
		final RoomFloorConfigurationModel roomFloorConfigurationModel = getModelService()
				.create(RoomFloorConfigurationModel.class);
		final String roomNumber = ((Textbox) children.get(0)).getValue();
		final String floorNumber = ((Textbox) children.get(1)).getValue();

		if (StringUtils.isEmpty(roomNumber) || StringUtils.isEmpty(floorNumber))
		{
			return null;
		}
		roomFloorConfigurationModel.setRoomNumber(roomNumber);
		roomFloorConfigurationModel.setFloorNumber(floorNumber);
		getModelService().save(roomFloorConfigurationModel);
		return roomFloorConfigurationModel;
	}

	/**
	 * Method displays the error message on {@link Messagebox}
	 *
	 * @param errorMsg
	 * @param title
	 */
	protected void displayErrorMessage(final String errorMsg, final String title)
	{
		Messagebox.show(errorMsg, title, Messagebox.OK, Messagebox.ERROR, null);
	}

	/**
	 * @return
	 */
	protected ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 */
	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

}
