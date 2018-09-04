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
import de.hybris.platform.travelrulesengine.rao.AccommodationSearchResponseRAO;
import de.hybris.platform.travelrulesengine.rao.ShowAccommodationOfferingsActionRAO;

import java.util.ArrayList;
import java.util.Map;


/**
 * The type Default show accommodation offering rao action.
 */
public class DefaultShowAccommodationOfferingRAOAction extends AbstractTravelRuleRAOAction
{
	@Override
	public void performAction(final RuleActionContext ruleContext)
	{
		validateRule(ruleContext);
		final Map<String, Object> parameters = ruleContext.getParameters();
		validateParameters(parameters);
		final ArrayList<String> accommodationOfferings = (ArrayList<String>) parameters.get("accommodationOfferings");
		showAccommodationOfferings(ruleContext, accommodationOfferings);

		trackRuleGroupExecutions(ruleContext);
		trackRuleExecution(ruleContext);
	}

	/**
	 * Show accommodation offerings.
	 *
	 * @param ruleContext
	 * 		the rule context
	 * @param accommodationOfferings
	 * 		the accommodation offerings
	 */
	protected void showAccommodationOfferings(final RuleActionContext ruleContext,
			final ArrayList<String> accommodationOfferings)
	{
		final AccommodationSearchResponseRAO accommodationSearchResponseRAO = ruleContext
				.getValue(AccommodationSearchResponseRAO.class);
		ServicesUtil.validateParameterNotNull(accommodationSearchResponseRAO, "Accommodation Search Response Rao must not be "
				+ "null");
		final RuleEngineResultRAO ruleEngineResultRao = ruleContext.getRuleEngineResultRao();

		final ShowAccommodationOfferingsActionRAO showAccommodationOfferingsActionRAO = new ShowAccommodationOfferingsActionRAO();
		showAccommodationOfferingsActionRAO.setAccommodationOfferings(accommodationOfferings);
		getRaoUtils().addAction(accommodationSearchResponseRAO, showAccommodationOfferingsActionRAO);
		ruleEngineResultRao.getActions().add(showAccommodationOfferingsActionRAO);
		setRAOMetaData(ruleContext, showAccommodationOfferingsActionRAO);
		ruleContext.insertFacts(ruleContext, showAccommodationOfferingsActionRAO);
	}
}
