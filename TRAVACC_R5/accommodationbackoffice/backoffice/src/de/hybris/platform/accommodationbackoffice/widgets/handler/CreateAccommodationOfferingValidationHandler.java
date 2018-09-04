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
import de.hybris.platform.ordersplitting.model.VendorModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.travelbackoffice.constants.TravelbackofficeConstants;
import de.hybris.platform.travelservices.model.warehouse.AccommodationOfferingModel;
import de.hybris.platform.travelservices.vendor.TravelVendorService;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.lang.Strings;
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
 * Custom validation handler for "create Accommodation Offering" wizard . It validates for negative value fields and
 * unique code, also fetches the hotel vendor and sets into the newly created Accommodation Offering
 */
public class CreateAccommodationOfferingValidationHandler extends AbstractAccommodationBackofficeValidationHandler
		implements FlowActionHandler
{
	private static final Logger LOG = Logger.getLogger(CreateAccommodationOfferingValidationHandler.class);
	private static final String ACCOMMODATION_VENDOR_CODE = "accommodation.vendor.code";
	private static final String CODE = "code";
	protected static final String NEW_ACCOMMODATION_OFFERING_CODE = "newAccommodationOffering";

	private ModelService modelService;
	private TravelVendorService travelVendorService;
	private ConfigurationService configurationService;

	@Override
	public void perform(final CustomType customType, final FlowActionHandlerAdapter adapter, final Map<String, String> parameters)
	{
		final AccommodationOfferingModel accommodationOffering = adapter.getWidgetInstanceManager().getModel()
				.getValue(NEW_ACCOMMODATION_OFFERING_CODE, AccommodationOfferingModel.class);

		if (Objects.isNull(accommodationOffering))
		{
			return;
		}
		if (!validateUniqueForAttributes(Collections.singletonMap(CODE, accommodationOffering.getCode()),
				AccommodationOfferingModel._TYPECODE))
		{
			final Object[] args = { accommodationOffering.getCode(), CODE };
			Messagebox.show(Labels.getLabel(AccommodationbackofficeConstants.WIZARD_DUPLICATE_CODE_ERROR_MESSAGE, args),
					Labels.getLabel(AccommodationbackofficeConstants.WIZARD_ERROR_TITLE), Messagebox.OK, Messagebox.EXCLAMATION);
			return;
		}
		if (Objects.nonNull(accommodationOffering.getNumberOfSleepingRooms())
				&& accommodationOffering.getNumberOfSleepingRooms() < 0)
		{
			final Object[] args = { Labels.getLabel(AccommodationbackofficeConstants.NUMBER_OF_SLEEPING_ROOMS) };
			Messagebox.show(Labels.getLabel(AccommodationbackofficeConstants.NEGATIVE_VALUE_NOT_ALLOWED_FOR_FIELD, args),
					Labels.getLabel(TravelbackofficeConstants.MESSAGE_BOX_WARNING_TITLE), new Button[]
							{ Button.OK }, null, null);
			return;
		}
		if (Objects.nonNull(accommodationOffering.getNumberOfNonSleepingRooms())
				&& accommodationOffering.getNumberOfNonSleepingRooms() < 0)
		{
			final Object[] args = { Labels.getLabel(AccommodationbackofficeConstants.NUMBER_OF_NON_SLEEPING_ROOMS) };
			Messagebox.show(Labels.getLabel(AccommodationbackofficeConstants.NEGATIVE_VALUE_NOT_ALLOWED_FOR_FIELD, args),
					Labels.getLabel(TravelbackofficeConstants.MESSAGE_BOX_WARNING_TITLE), new Button[] { Button.OK }, null, null);
			return;
		}
		if (Objects.nonNull(accommodationOffering.getNumberOfFloors()) && accommodationOffering.getNumberOfFloors() < 0)
		{
			final Object[] args = { Labels.getLabel(AccommodationbackofficeConstants.NUMBER_OF_FLOORS) };
			Messagebox.show(Labels.getLabel(AccommodationbackofficeConstants.NEGATIVE_VALUE_NOT_ALLOWED_FOR_FIELD, args),
					Labels.getLabel(TravelbackofficeConstants.MESSAGE_BOX_WARNING_TITLE), new Button[] { Button.OK }, null, null);
			return;
		}
		if (Strings.isEmpty(accommodationOffering.getName()))
		{
			final Object[] args = { Labels.getLabel(AccommodationbackofficeConstants.ACCOMMODATION_OFFERING_NAME) };
			Messagebox.show(Labels.getLabel(AccommodationbackofficeConstants.MANDATORY_FIELD, args),
					Labels.getLabel(TravelbackofficeConstants.MESSAGE_BOX_WARNING_TITLE), new Button[] { Button.OK }, null, null);
			return;
		}
		if (Objects.isNull(accommodationOffering.getLocation()))
		{
			final Object[] args = { Labels.getLabel(AccommodationbackofficeConstants.ACCOMMODATION_OFFERING_LOCATION) };
			Messagebox.show(Labels.getLabel(AccommodationbackofficeConstants.MANDATORY_FIELD, args),
					Labels.getLabel(TravelbackofficeConstants.MESSAGE_BOX_WARNING_TITLE), new Button[] { Button.OK }, null, null);
			return;
		}

		final VendorModel vendor = getTravelVendorService()
				.getVendorByCode(getConfigurationService().getConfiguration().getString(ACCOMMODATION_VENDOR_CODE));
		if (Objects.nonNull(vendor))
		{
			accommodationOffering.setVendor(vendor);
		}
		try
		{
			getModelService().save(accommodationOffering);

			NotificationUtils.notifyUser(NotificationUtils.getWidgetNotificationSource(adapter.getWidgetInstanceManager()),
					TravelbackofficeConstants.TRAVEL_GLOBAL_NOTIFICATION_EVENT_TYPE, NotificationEvent.Level.SUCCESS,
					Labels.getLabel(AccommodationbackofficeConstants.CREATE_ACCOMMODATION_OFFERING_SUCCESS_MESSAGE));

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
	 * @return the modelService
	 */
	protected ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 * 		the modelService to set
	 */
	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}


	/**
	 * Gets travel vendor service.
	 *
	 * @return the travel vendor service
	 */
	protected TravelVendorService getTravelVendorService()
	{
		return travelVendorService;
	}

	/**
	 * Sets travel vendor service.
	 *
	 * @param travelVendorService
	 * 		the travel vendor service
	 */
	@Required
	public void setTravelVendorService(final TravelVendorService travelVendorService)
	{
		this.travelVendorService = travelVendorService;
	}

	/**
	 * @return
	 */
	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	/**
	 * @param configurationService
	 */
	@Required
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}
}
