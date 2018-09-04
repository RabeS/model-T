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
import de.hybris.platform.travelservices.model.accommodation.AbstractFinancialInfoModel;

import java.util.Collections;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Messagebox;

import com.hybris.cockpitng.config.jaxb.wizard.CustomType;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandler;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandlerAdapter;


/**
 * Abstract validation handler for AbstractFinancialInfo wizard.
 */
public abstract class AbstractFinancialInfoValidationHandler extends AbstractAccommodationBackofficeValidationHandler
		implements FlowActionHandler
{
	private static final Logger LOG = Logger.getLogger(AbstractFinancialInfoValidationHandler.class);

	protected static final String CREATE_ABSTRACT_FINANCIAL_INFO_WIZARD_ERROR_TITLE =
			"create.abstractfinancialinfo.wizard.step.error.title";
	protected static final String CREATE_ABSTRACT_FINANCIAL_INFO_WIZARD_DUPLICATE_CODE_ERROR_MESSAGE =
			"create.abstractfinancialinfo.wizard.duplicate.code.error.message";
	protected static final String ABSTRACT_FINANCIAL_INFO_CODE = "create.abstractfinancialinfo.wizard.code.name";
	protected static final String NEW_ABSTRACT_FINANCIAL_CODE = "newAbstractFinancialInfo";

	private ModelService modelService;

	@Override
	public void perform(final CustomType customType, final FlowActionHandlerAdapter adapter, final Map<String, String> parameters)
	{
		final AbstractFinancialInfoModel abstractFinancialInfoModel = adapter.getWidgetInstanceManager().getModel().getValue
				(NEW_ABSTRACT_FINANCIAL_CODE, AbstractFinancialInfoModel.class);

		if (StringUtils.length(abstractFinancialInfoModel.getCode()) > AccommodationbackofficeConstants.CODE_MAXIMUM_CHARS_ALLOWED)
		{
			showMessage(ABSTRACT_FINANCIAL_INFO_CODE);
			return;
		}

		if (!validateUniqueForAttributes(
				Collections.singletonMap(AbstractFinancialInfoModel.CODE, abstractFinancialInfoModel.getCode()),
				AbstractFinancialInfoModel._TYPECODE))
		{
			Messagebox.show(Labels.getLabel(CREATE_ABSTRACT_FINANCIAL_INFO_WIZARD_DUPLICATE_CODE_ERROR_MESSAGE),
					Labels.getLabel(CREATE_ABSTRACT_FINANCIAL_INFO_WIZARD_ERROR_TITLE), Messagebox.OK, Messagebox.EXCLAMATION);
			return;
		}

		getModelService().save(abstractFinancialInfoModel);
		adapter.done();
	}

	/**
	 * Based on the field name, it would show message for appropriate field.
	 *
	 * @param fieldName
	 * 		the name of the field in wizard
	 */
	protected void showMessage(final String fieldName)
	{
		final Object[] args = { Labels.getLabel(fieldName) };
		Messagebox
				.show(Labels.getLabel(AccommodationbackofficeConstants.EXCEEDED_MAXIMUM_CHARACTERS_ALLOWED_ERROR_MESSAGE, args),
						Labels.getLabel(CREATE_ABSTRACT_FINANCIAL_INFO_WIZARD_ERROR_TITLE), new Messagebox.Button[]
								{ Messagebox.Button.OK }, null, null);
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