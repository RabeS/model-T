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

package de.hybris.platform.travelrulesengine.rule.evaluation.actions;

import de.hybris.platform.ruleengineservices.rao.RuleEngineResultRAO;
import de.hybris.platform.ruleengineservices.rule.evaluation.RuleActionContext;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.travelrulesengine.rao.AccommodationAvailabilityRequestRAO;
import de.hybris.platform.travelrulesengine.rao.ShowAccommodationCategoriesActionRAO;

import java.util.ArrayList;
import java.util.Map;


/**
 * The type Default show accommodation category rao action.
 */
public class DefaultShowAccommodationCategoriesRAOAction extends AbstractTravelRuleRAOAction
{

	@Override
	public void performAction(final RuleActionContext ruleContext)
	{
		validateRule(ruleContext);
		final Map<String, Object> parameters = ruleContext.getParameters();
		validateParameters(parameters);
		final ArrayList<String> accommodationCategories = (ArrayList<String>) parameters.get("accommodationCategories");
		showAccommodationCategories(ruleContext, accommodationCategories);

		trackRuleGroupExecutions(ruleContext);
		trackRuleExecution(ruleContext);
	}

	private void showAccommodationCategories(final RuleActionContext ruleContext, final ArrayList<String> accommodationCategories)
	{
		final AccommodationAvailabilityRequestRAO accommodationAvailabilityRequestRAO = ruleContext
				.getValue(AccommodationAvailabilityRequestRAO.class);

		ServicesUtil.validateParameterNotNull(accommodationAvailabilityRequestRAO,
				"Accommodation Availability Request Rao must not be " + "null");
		final RuleEngineResultRAO ruleEngineResultRao = ruleContext.getRuleEngineResultRao();

		final ShowAccommodationCategoriesActionRAO showAccommodationCategoriesActionRAO = new
				ShowAccommodationCategoriesActionRAO();
		showAccommodationCategoriesActionRAO.setAccommodationCategories(accommodationCategories);
		getRaoUtils().addAction(accommodationAvailabilityRequestRAO, showAccommodationCategoriesActionRAO);
		ruleEngineResultRao.getActions().add(showAccommodationCategoriesActionRAO);
		setRAOMetaData(ruleContext, showAccommodationCategoriesActionRAO);
		ruleContext.insertFacts(ruleContext, showAccommodationCategoriesActionRAO);
	}

}
