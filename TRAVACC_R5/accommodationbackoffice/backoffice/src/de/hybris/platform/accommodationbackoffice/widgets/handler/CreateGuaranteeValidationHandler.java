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
import de.hybris.platform.travelservices.model.accommodation.GuaranteeModel;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.hybris.cockpitng.config.jaxb.wizard.CustomType;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandlerAdapter;


/**
 * Custom validation handler for Guarantee wizard.
 */
public class CreateGuaranteeValidationHandler extends AbstractFinancialInfoValidationHandler
{
	protected static final String GUARANTEE_SHORT_DESCRIPTION = "create.guarantee.wizard.shortdescription.name";
	protected static final String GUARANTEE_LONG_DESCRIPTION = "create.guarantee.wizard.longdescription.name";

	@Override
	public void perform(final CustomType customType, final FlowActionHandlerAdapter adapter, final Map<String, String> parameters)
	{
		final GuaranteeModel guaranteeModel = adapter.getWidgetInstanceManager().getModel().getValue
				(NEW_ABSTRACT_FINANCIAL_CODE, GuaranteeModel.class);

		if (StringUtils.length(guaranteeModel.getShortDescription()) > AccommodationbackofficeConstants.CODE_MAXIMUM_CHARS_ALLOWED)
		{
			showMessage(GUARANTEE_SHORT_DESCRIPTION);
			return;
		}

		if (StringUtils.length(guaranteeModel.getLongDescription()) > AccommodationbackofficeConstants.CODE_MAXIMUM_CHARS_ALLOWED)
		{
			showMessage(GUARANTEE_LONG_DESCRIPTION);
			return;
		}

		super.perform(customType, adapter, parameters);
	}
}
