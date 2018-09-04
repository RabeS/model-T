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

package de.hybris.platform.travelrulesengine.action.impl;

import de.hybris.platform.ruleengineservices.action.RuleActionStrategy;

import org.springframework.beans.factory.BeanNameAware;


/**
 * Abstract class for travel specific rule action strategy
 *
 */
public abstract class AbstractTravelRuleActionStrategy implements RuleActionStrategy, BeanNameAware
{

	private String beanName;

	@Override
	public void setBeanName(final String beanName)
	{
		this.beanName = beanName;
	}

	@Override
	public String getStrategyId()
	{
		return this.beanName;
	}

}
