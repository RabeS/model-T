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
import de.hybris.platform.travelrulesengine.dao.RuleAccommodationProviderDao;
import de.hybris.platform.travelservices.model.accommodation.AccommodationProviderModel;

import org.springframework.beans.factory.annotation.Required;


/**
 * The type Accommodation provider parameter value mapper.
 */
public class AccommodationProviderParameterValueMapper implements RuleParameterValueMapper<AccommodationProviderModel>
{

	private RuleAccommodationProviderDao ruleAccommodationProviderDao;

	@Override
	public String toString(final AccommodationProviderModel accommodationProviderModel)
	{
		ServicesUtil.validateParameterNotNull(accommodationProviderModel, "Object cannot be null");
		return accommodationProviderModel.getCode();
	}

	@Override
	public AccommodationProviderModel fromString(final String code)
	{
		return getRuleAccommodationProviderDao().findAccommodationProvider(code);
	}

	/**
	 * Gets rule accommodation provider dao.
	 *
	 * @return the rule accommodation provider dao
	 */
	protected RuleAccommodationProviderDao getRuleAccommodationProviderDao()
	{
		return ruleAccommodationProviderDao;
	}

	/**
	 * Sets rule accommodation provider dao.
	 *
	 * @param ruleAccommodationProviderDao
	 * 		the rule accommodation provider dao
	 */
	@Required
	public void setRuleAccommodationProviderDao(final RuleAccommodationProviderDao ruleAccommodationProviderDao)
	{
		this.ruleAccommodationProviderDao = ruleAccommodationProviderDao;
	}
}
