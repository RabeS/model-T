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
import de.hybris.platform.travelrulesengine.rao.AccommodationReservationRAO;
import de.hybris.platform.travelrulesengine.rao.RoomStayRAO;
import de.hybris.platform.travelrulesengine.rao.ShowExtraProductsActionRAO;
import de.hybris.platform.travelrulesengine.rao.ShowRatePlansActionRAO;

import java.util.ArrayList;
import java.util.Map;


/**
 * Action to be executed on 'show rate plans based on conditions' rule evaluation
 */
public class DefaultShowRatePlanRAOAction extends AbstractTravelRuleRAOAction
{

	@Override
	public void performAction(final RuleActionContext ruleContext)
	{
		validateRule(ruleContext);
		final Map<String, Object> parameters = ruleContext.getParameters();
		validateParameters(parameters);
		final ArrayList<String> ratePlans = (ArrayList<String>) parameters.get("ratePlans");
		showRatePlans(ruleContext, ratePlans);

		trackRuleGroupExecutions(ruleContext);
		trackRuleExecution(ruleContext);
	}

	protected void showRatePlans(final RuleActionContext ruleContext, final ArrayList<String> ratePlans)
	{
		final AccommodationAvailabilityRequestRAO availabilityRequestRAO = ruleContext.getValue(AccommodationAvailabilityRequestRAO.class);
		final RoomStayRAO roomStayRAO = ruleContext.getValue(RoomStayRAO.class);
		ServicesUtil.validateParameterNotNull(availabilityRequestRAO, "Accommodation Availability Request Rao must not be null");
		ServicesUtil.validateParameterNotNull(roomStayRAO, "Room Stay Rao must not be null");
		final RuleEngineResultRAO ruleEngineResultRao = ruleContext.getRuleEngineResultRao();

		final ShowRatePlansActionRAO showRatePlansActionRAO = new ShowRatePlansActionRAO();
		showRatePlansActionRAO.setRatePlans(ratePlans);
		getRaoUtils().addAction(availabilityRequestRAO, showRatePlansActionRAO);
		getRaoUtils().addAction(roomStayRAO, showRatePlansActionRAO);
		ruleEngineResultRao.getActions().add(showRatePlansActionRAO);
		setRAOMetaData(ruleContext, showRatePlansActionRAO);
		ruleContext.insertFacts(ruleContext, showRatePlansActionRAO);
	}

}