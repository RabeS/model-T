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

package de.hybris.platform.travelrulesengine.commerce.impl;

import de.hybris.platform.ruleengineservices.rule.evaluation.actions.AbstractRuleExecutableSupport;
import de.hybris.platform.travelrulesengine.calculation.TravelRuleEngineCalculationService;


/**
 * Abstract Drools ROA action class to support travel specific calculation service
 * 
 * @deprecated Deprecated since version 3.0. Use {@link AbstractRuleExecutableSupport}.
 */
@Deprecated
public abstract class AbstractTravelCommerceRAOAction extends AbstractRuleExecutableSupport
{
	private TravelRuleEngineCalculationService travelRuleEngineCalculationService;

	/**
	 * @return the travelRuleEngineCalculationService
	 */
	public TravelRuleEngineCalculationService getTravelRuleEngineCalculationService()
	{
		return travelRuleEngineCalculationService;
	}

	/**
	 * @param travelRuleEngineCalculationService
	 *           the travelRuleEngineCalculationService to set
	 */
	public void setTravelRuleEngineCalculationService(final TravelRuleEngineCalculationService travelRuleEngineCalculationService)
	{
		this.travelRuleEngineCalculationService = travelRuleEngineCalculationService;
	}


}
