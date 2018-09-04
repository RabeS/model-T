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
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.travelservices.model.product.NonSleepingRoomModel;

import java.util.HashMap;
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
 * Custom validation handler for the NonSleepingRoom Create Wizard
 */
public class NonSleepingRoomValidationHandler extends AbstractAccommodationBackofficeValidationHandler
		implements FlowActionHandler
{
	private static final Logger LOG = Logger.getLogger(NonSleepingRoomValidationHandler.class);
	private static final String CATALOG_VERSION = "catalogVersion";
	protected static final String NEW_NON_SLEEPING_ROOM = "newProduct";

	private ModelService modelService;

	@Override
	public void perform(final CustomType customType, final FlowActionHandlerAdapter adapter, final Map<String, String> parameters)
	{
		final NonSleepingRoomModel nonSleepingRoomModel = adapter.getWidgetInstanceManager().getModel()
				.getValue(NEW_NON_SLEEPING_ROOM, NonSleepingRoomModel.class);

		final Map<String, Object> attributesMap = new HashMap<>();
		attributesMap.put(NonSleepingRoomModel.CODE, nonSleepingRoomModel.getCode());
		attributesMap.put(CATALOG_VERSION, nonSleepingRoomModel.getCatalogVersion());
		if (!validateUniqueForAttributes(attributesMap, ProductModel._TYPECODE))
		{
			final Object[] args = { nonSleepingRoomModel.getCode(), NonSleepingRoomModel.CODE };

			Messagebox.show(Labels.getLabel(AccommodationbackofficeConstants.WIZARD_DUPLICATE_CODE_ERROR_MESSAGE, args),
					Labels.getLabel(AccommodationbackofficeConstants.WIZARD_ERROR_TITLE), Messagebox.OK, Messagebox.EXCLAMATION);
			return;
		}
		try
		{
			getModelService().save(nonSleepingRoomModel);
			NotificationUtils.notifyUser(NotificationUtils.getWidgetNotificationSource(adapter.getWidgetInstanceManager()),
					NotificationEventTypes.EVENT_TYPE_OBJECT_CREATION, NotificationEvent.Level.SUCCESS, nonSleepingRoomModel);

			adapter.done();
		}
		catch (final Exception e)
		{
			LOG.debug(e);
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
