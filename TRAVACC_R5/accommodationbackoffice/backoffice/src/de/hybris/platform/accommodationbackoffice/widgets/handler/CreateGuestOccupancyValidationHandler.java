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

package de.hybris.platform.accommodationbackoffice.widgets.handler;

import de.hybris.platform.accommodationbackoffice.constants.AccommodationbackofficeConstants;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.travelbackoffice.constants.TravelbackofficeConstants;
import de.hybris.platform.travelservices.model.accommodation.GuestOccupancyModel;

import java.util.Collections;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.Button;

import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEventTypes;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationUtils;
import com.hybris.cockpitng.config.jaxb.wizard.CustomType;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandler;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandlerAdapter;


/**
 * Custom validation handler for "Create Guest Occupancy" wizard.
 */
public class CreateGuestOccupancyValidationHandler extends AbstractAccommodationBackofficeValidationHandler
		implements FlowActionHandler
{
	private static final Logger LOG = Logger.getLogger(CreateGuestOccupancyValidationHandler.class);

	protected static final String GUEST_OCCUPANCY_CODE = "create.guestoccupancy.wizard.code.name";
	protected static final String NEW_GUEST_OCCUPANCY_CODE = "newGuestOccupancy";

	private ModelService modelService;

	@Override
	public void perform(final CustomType customType, final FlowActionHandlerAdapter adapter, final Map<String, String> parameters)
	{
		final GuestOccupancyModel guestOccupancyModel = adapter.getWidgetInstanceManager().getModel()
				.getValue(NEW_GUEST_OCCUPANCY_CODE, GuestOccupancyModel.class);

		if (StringUtils.length(guestOccupancyModel.getCode()) > AccommodationbackofficeConstants.CODE_MAXIMUM_CHARS_ALLOWED)
		{
			final Object[] args = { Labels.getLabel(GUEST_OCCUPANCY_CODE) };
			Messagebox.show(
					Labels.getLabel(AccommodationbackofficeConstants.EXCEEDED_MAXIMUM_CHARACTERS_ALLOWED_ERROR_MESSAGE, args),
					Labels.getLabel(AccommodationbackofficeConstants.WIZARD_ERROR_TITLE), new Button[] { Button.OK }, null, null);
			return;
		}

		if (!validateUniqueForAttributes(Collections.singletonMap(GuestOccupancyModel.CODE, guestOccupancyModel.getCode()),
				GuestOccupancyModel._TYPECODE))
		{
			final Object[] args = { guestOccupancyModel.getCode(), GuestOccupancyModel.CODE };
			Messagebox.show(Labels.getLabel(AccommodationbackofficeConstants.WIZARD_DUPLICATE_CODE_ERROR_MESSAGE, args),
					Labels.getLabel(AccommodationbackofficeConstants.WIZARD_ERROR_TITLE), Messagebox.OK, Messagebox.EXCLAMATION);
			return;
		}

		try
		{
			getModelService().save(guestOccupancyModel);
			NotificationUtils.notifyUser(NotificationUtils.getWidgetNotificationSource(adapter.getWidgetInstanceManager()),
					TravelbackofficeConstants.TRAVEL_GLOBAL_NOTIFICATION_EVENT_TYPE, NotificationEvent.Level.SUCCESS,
					Labels.getLabel(AccommodationbackofficeConstants.CREATE_GUEST_OCCUPANCY_SUCCESS_MESSAGE));
			adapter.done();
		}
		catch (final ModelSavingException mse)
		{
			LOG.debug(mse);
			NotificationUtils.notifyUser(NotificationUtils.getWidgetNotificationSource(adapter.getWidgetInstanceManager()),
					NotificationEventTypes.EVENT_TYPE_OBJECT_CREATION, NotificationEvent.Level.FAILURE, mse);
		}
	}

	/**
	 * Gets model service.
	 *
	 * @return the model service
	 */
	protected ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * Sets model service.
	 *
	 * @param modelService
	 * 		the model service
	 */
	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}
}
