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
package de.hybris.platform.travelrulesengine.rule.strategies.impl.mappers;

import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterValueMapper;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.travelrulesengine.dao.RuleRatePlanDao;
import de.hybris.platform.travelservices.model.accommodation.RatePlanModel;

import org.springframework.beans.factory.annotation.Required;


/**
 * The type Rate plan parameter value mapper.
 */
public class RatePlanParameterValueMapper implements RuleParameterValueMapper<RatePlanModel>
{
	private RuleRatePlanDao RuleRatePlanDao;

	@Override
	public String toString(final RatePlanModel ratePlanModel)
	{
		ServicesUtil.validateParameterNotNull(ratePlanModel, "Object cannot be null");
		return ratePlanModel.getCode();
	}

	@Override
	public RatePlanModel fromString(final String code)
	{
		return getRuleRatePlanDao().findRatePlan(code);
	}

	/**
	 * Gets rule rate plan dao.
	 *
	 * @return the rule rate plan dao
	 */
	protected de.hybris.platform.travelrulesengine.dao.RuleRatePlanDao getRuleRatePlanDao()
	{
		return RuleRatePlanDao;
	}

	/**
	 * Sets rule rate plan dao.
	 *
	 * @param ruleRatePlanDao
	 * 		the rule rate plan dao
	 */
	@Required
	public void setRuleRatePlanDao(final de.hybris.platform.travelrulesengine.dao.RuleRatePlanDao ruleRatePlanDao)
	{
		RuleRatePlanDao = ruleRatePlanDao;
	}
}
