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
import de.hybris.platform.travelservices.model.accommodation.AccommodationChainModel;

import java.util.Collections;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Messagebox;

import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEventTypes;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationUtils;
import com.hybris.cockpitng.config.jaxb.wizard.CustomType;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandler;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandlerAdapter;


/**
 * Custom validation handler for the Accommodation Chain Create Wizard
 */
public class AccommodationChainValidationHandler extends AbstractAccommodationBackofficeValidationHandler
		implements FlowActionHandler
{
	private static final Logger LOG = Logger.getLogger(AccommodationChainValidationHandler.class);
	private ModelService modelService;
	protected static final String NEW_ACCOMMODATION_CHAIN = "newAccommodationChain";

	@Override
	public void perform(final CustomType customType, final FlowActionHandlerAdapter adapter, final Map<String, String> parameters)
	{
		final AccommodationChainModel accommodationChainModel = adapter.getWidgetInstanceManager().getModel()
				.getValue(NEW_ACCOMMODATION_CHAIN, AccommodationChainModel.class);

		if (!validateUniqueForAttributes(Collections.singletonMap(AccommodationChainModel.CODE, accommodationChainModel.getCode()),
				AccommodationChainModel._TYPECODE))
		{
			final Object[] args = { accommodationChainModel.getCode(), AccommodationChainModel.CODE };
			Messagebox.show(Labels.getLabel(AccommodationbackofficeConstants.WIZARD_DUPLICATE_CODE_ERROR_MESSAGE, args),
					Labels.getLabel(AccommodationbackofficeConstants.WIZARD_ERROR_TITLE), Messagebox.OK, Messagebox.EXCLAMATION);
			return;
		}
		try
		{
			getModelService().save(accommodationChainModel);
			NotificationUtils.notifyUser(NotificationUtils.getWidgetNotificationSource(adapter.getWidgetInstanceManager()),
					TravelbackofficeConstants.TRAVEL_GLOBAL_NOTIFICATION_EVENT_TYPE, NotificationEvent.Level.SUCCESS,
					Labels.getLabel(AccommodationbackofficeConstants.CREATE_ACCOMMODATION_CHAIN_SUCCESS_MESSAGE));
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
	 * @return the model service
	 */
	protected ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 * 		the model service
	 */
	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

}
