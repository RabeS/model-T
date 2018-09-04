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
import de.hybris.platform.travelservices.model.accommodation.CancelPenaltyModel;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.hybris.cockpitng.config.jaxb.wizard.CustomType;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandlerAdapter;


/**
 * Custom validation handler for Cancel Penalty wizard.
 */
public class CreateCancelPenaltyValidationHandler extends AbstractFinancialInfoValidationHandler
{
	protected static final String CANCEL_PENALTY_DESCRIPTION = "create.cancel.penalty.wizard.description.name";

	@Override
	public void perform(final CustomType customType, final FlowActionHandlerAdapter adapter, final Map<String, String> parameters)
	{
		final CancelPenaltyModel cancelPenaltyModel = adapter.getWidgetInstanceManager().getModel().getValue
				(NEW_ABSTRACT_FINANCIAL_CODE, CancelPenaltyModel.class);

		if (StringUtils.length(cancelPenaltyModel.getDescription()) > AccommodationbackofficeConstants.CODE_MAXIMUM_CHARS_ALLOWED)
		{
			showMessage(CANCEL_PENALTY_DESCRIPTION);
			return;
		}

		super.perform(customType, adapter, parameters);
	}

}
