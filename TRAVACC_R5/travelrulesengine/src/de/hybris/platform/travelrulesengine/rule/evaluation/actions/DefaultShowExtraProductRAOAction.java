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
import de.hybris.platform.travelrulesengine.rao.AccommodationReservationRAO;
import de.hybris.platform.travelrulesengine.rao.ShowExtraProductsActionRAO;

import java.util.ArrayList;
import java.util.Map;


/**
 * The type Default show extra product rao action.
 */
public class DefaultShowExtraProductRAOAction extends AbstractTravelRuleRAOAction
{

	@Override
	public void performAction(final RuleActionContext ruleContext)
	{
		validateRule(ruleContext);
		final Map<String, Object> parameters = ruleContext.getParameters();
		validateParameters(parameters);
		final ArrayList<String> products = (ArrayList<String>) parameters.get("extras");
		showExtraProducts(ruleContext, products);

		trackRuleGroupExecutions(ruleContext);
		trackRuleExecution(ruleContext);
	}

	protected void showExtraProducts(final RuleActionContext ruleContext, final ArrayList<String> products)
	{
		final AccommodationReservationRAO accommodationReservationRAO = ruleContext.getValue(AccommodationReservationRAO.class);
		ServicesUtil.validateParameterNotNull(accommodationReservationRAO, "Accommodation Reservation Rao must not be null");
		final RuleEngineResultRAO ruleEngineResultRao = ruleContext.getRuleEngineResultRao();

		final ShowExtraProductsActionRAO showExtraProductsActionRAO = new ShowExtraProductsActionRAO();
		showExtraProductsActionRAO.setProducts(products);
		getRaoUtils().addAction(accommodationReservationRAO, showExtraProductsActionRAO);
		ruleEngineResultRao.getActions().add(showExtraProductsActionRAO);
		setRAOMetaData(ruleContext, showExtraProductsActionRAO);
		ruleContext.insertFacts(ruleContext, showExtraProductsActionRAO);
	}

}
