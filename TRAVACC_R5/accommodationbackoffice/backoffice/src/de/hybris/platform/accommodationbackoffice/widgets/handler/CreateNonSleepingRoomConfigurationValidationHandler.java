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
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.travelservices.model.accommodation.NonSleepingRoomConfigurationModel;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Messagebox;

import com.hybris.cockpitng.config.jaxb.wizard.CustomType;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandler;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandlerAdapter;


/**
 * Custom validation handler for Non Sleeping Room Setup wizard.
 */
public class CreateNonSleepingRoomConfigurationValidationHandler extends AbstractAccommodationBackofficeValidationHandler implements FlowActionHandler
{
	private static final Logger LOG = Logger.getLogger(CreateNonSleepingRoomConfigurationValidationHandler.class);

	protected static final String NONSLEEPINGROOM_SETUP_CODE = "create.nonsleepingroomconfiguration.wizard.code.name";
	protected static final String NONSLEEPINGROOM_SETUP_NAME = "create.nonsleepingroomconfiguration.wizard.name.name";

	protected static final String NEW_NONSLEEPINGROOM_SETUP_CODE = "newNonSleepingRoomConfiguration";

	private ModelService modelService;

	@Override
	public void perform(final CustomType customType, final FlowActionHandlerAdapter adapter, final Map<String, String> parameters)
	{
		final NonSleepingRoomConfigurationModel nonSleepingRoomConfigurationModel = adapter.getWidgetInstanceManager().getModel()
				.getValue(NEW_NONSLEEPINGROOM_SETUP_CODE, NonSleepingRoomConfigurationModel.class);

		if (StringUtils.length(nonSleepingRoomConfigurationModel.getCode()) > AccommodationbackofficeConstants.CODE_MAXIMUM_CHARS_ALLOWED)
		{
			final Object[] args = { Labels.getLabel(NONSLEEPINGROOM_SETUP_CODE) };
			Messagebox
					.show(Labels.getLabel(AccommodationbackofficeConstants.EXCEEDED_MAXIMUM_CHARACTERS_ALLOWED_ERROR_MESSAGE, args),
							Labels.getLabel(AccommodationbackofficeConstants.WIZARD_ERROR_TITLE), new Messagebox.Button[]
									{ Messagebox.Button.OK }, null, null);
			return;
		}

		if (StringUtils.length(nonSleepingRoomConfigurationModel.getName()) > AccommodationbackofficeConstants.CODE_MAXIMUM_CHARS_ALLOWED)
		{
			final Object[] args = { Labels.getLabel(NONSLEEPINGROOM_SETUP_NAME) };
			Messagebox
					.show(Labels.getLabel(AccommodationbackofficeConstants.EXCEEDED_MAXIMUM_CHARACTERS_ALLOWED_ERROR_MESSAGE, args),
							Labels.getLabel(AccommodationbackofficeConstants.WIZARD_ERROR_TITLE), new Messagebox.Button[]
									{ Messagebox.Button.OK }, null, null);
			return;
		}

		final Map<String, Object> attributesMap = new HashMap<>();
		attributesMap.put(NonSleepingRoomConfigurationModel.CODE, nonSleepingRoomConfigurationModel.getCode());

		if (!validateUniqueForAttributes(attributesMap, NonSleepingRoomConfigurationModel._TYPECODE))
		{
			final Object[] args = { nonSleepingRoomConfigurationModel.getCode(), NonSleepingRoomConfigurationModel.CODE };
			Messagebox.show(Labels.getLabel(AccommodationbackofficeConstants.WIZARD_DUPLICATE_CODE_ERROR_MESSAGE, args),
					Labels.getLabel(AccommodationbackofficeConstants.WIZARD_ERROR_TITLE), Messagebox.OK, Messagebox.EXCLAMATION);
			return;
		}

		getModelService().save(nonSleepingRoomConfigurationModel);
		adapter.done();
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
