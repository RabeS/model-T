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
import de.hybris.platform.travelrulesengine.rao.RoomStayRAO;
import de.hybris.platform.travelrulesengine.rao.ShowAccommodationsActionRAO;

import java.util.ArrayList;
import java.util.Map;


/**
 * The type Default show accommodation rao action.
 */
public class DefaultShowAccommodationRAOAction extends AbstractTravelRuleRAOAction
{
	@Override
	public void performAction(final RuleActionContext ruleContext)
	{
		validateRule(ruleContext);
		final Map<String, Object> parameters = ruleContext.getParameters();
		validateParameters(parameters);
		final ArrayList<String> accommodations = (ArrayList<String>) parameters.get("accommodations");
		showAccommodations(ruleContext, accommodations);

		trackRuleGroupExecutions(ruleContext);
		trackRuleExecution(ruleContext);
	}

	/**
	 * Show accommodations.
	 *
	 * @param ruleContext
	 * 		the rule context
	 * @param accommodations
	 * 		the accommodations
	 */
	protected void showAccommodations(final RuleActionContext ruleContext, final ArrayList<String> accommodations)
	{
		final AccommodationAvailabilityRequestRAO availabilityRequestRAO = ruleContext
				.getValue(AccommodationAvailabilityRequestRAO.class);
		final RoomStayRAO roomStayRAO = ruleContext.getValue(RoomStayRAO.class);
		ServicesUtil.validateParameterNotNull(availabilityRequestRAO, "Accommodation Availability Request Rao must not be null");
		ServicesUtil.validateParameterNotNull(roomStayRAO, "Room Stay Rao must not be null");
		final RuleEngineResultRAO ruleEngineResultRao = ruleContext.getRuleEngineResultRao();

		final ShowAccommodationsActionRAO showAccommodationsActionRAO = new ShowAccommodationsActionRAO();
		showAccommodationsActionRAO.setAccommodations(accommodations);
		getRaoUtils().addAction(availabilityRequestRAO, showAccommodationsActionRAO);
		getRaoUtils().addAction(roomStayRAO, showAccommodationsActionRAO);
		ruleEngineResultRao.getActions().add(showAccommodationsActionRAO);
		setRAOMetaData(ruleContext, showAccommodationsActionRAO);
		ruleContext.insertFacts(ruleContext, showAccommodationsActionRAO);
	}

}
