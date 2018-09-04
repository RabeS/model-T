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
import de.hybris.platform.travelrulesengine.dao.RuleAccommodationDao;
import de.hybris.platform.travelservices.model.product.AccommodationModel;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * The type Accommodation parameter value mapper.
 */
public class AccommodationParameterValueMapper implements RuleParameterValueMapper<AccommodationModel>
{
	private RuleAccommodationDao ruleAccommodationDao;

	@Override
	public String toString(final AccommodationModel accommodationModel)
	{
		ServicesUtil.validateParameterNotNull(accommodationModel, "Object cannot be null");
		return accommodationModel.getCode();
	}

	@Override
	public AccommodationModel fromString(final String code)
	{
		ServicesUtil.validateParameterNotNull(code, "String value cannot be null");
		final List<AccommodationModel> accommodationModels = getRuleAccommodationDao().findAccommodation(code);

		if (CollectionUtils.isEmpty(accommodationModels))
		{
			return null;
		}

		return accommodationModels.get(0);
	}

	/**
	 * Gets rule accommodation dao.
	 *
	 * @return the rule accommodation dao
	 */
	protected RuleAccommodationDao getRuleAccommodationDao()
	{
		return ruleAccommodationDao;
	}

	/**
	 * Sets rule accommodation dao.
	 *
	 * @param ruleAccommodationDao
	 * 		the rule accommodation dao
	 */
	@Required
	public void setRuleAccommodationDao(final RuleAccommodationDao ruleAccommodationDao)
	{
		this.ruleAccommodationDao = ruleAccommodationDao;
	}
}
