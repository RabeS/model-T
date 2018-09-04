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
import de.hybris.platform.travelrulesengine.dao.RuleAccommodationCategoryDao;
import de.hybris.platform.travelservices.model.travel.AccommodationCategoryModel;

import org.springframework.beans.factory.annotation.Required;


/**
 * The type Accommodation category parameter value mapper.
 */
public class AccommodationCategoryParameterValueMapper implements RuleParameterValueMapper<AccommodationCategoryModel>
{
	private RuleAccommodationCategoryDao ruleAccommodationCategoryDao;

	@Override
	public String toString(final AccommodationCategoryModel accommodationCategoryModel)
	{
		ServicesUtil.validateParameterNotNull(accommodationCategoryModel, "Object cannot be null");
		return accommodationCategoryModel.getCode();
	}

	@Override
	public AccommodationCategoryModel fromString(final String code)
	{
		return getRuleAccommodationCategoryDao().findAccommodationCategory(code);
	}

	/**
	 * Gets rule accommodation category dao.
	 *
	 * @return the rule accommodation category dao
	 */
	protected RuleAccommodationCategoryDao getRuleAccommodationCategoryDao()
	{
		return ruleAccommodationCategoryDao;
	}

	/**
	 * Sets rule accommodation category dao.
	 *
	 * @param ruleAccommodationCategoryDao
	 * 		the rule accommodation category dao
	 */
	@Required
	public void setRuleAccommodationCategoryDao(final RuleAccommodationCategoryDao ruleAccommodationCategoryDao)
	{
		this.ruleAccommodationCategoryDao = ruleAccommodationCategoryDao;
	}
}
