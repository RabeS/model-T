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
import de.hybris.platform.travelrulesengine.dao.RuleAccommodationOfferingDao;
import de.hybris.platform.travelservices.model.warehouse.AccommodationOfferingModel;

import org.springframework.beans.factory.annotation.Required;


/**
 * The type Accommodation offering parameter value mapper.
 */
public class AccommodationOfferingParameterValueMapper implements RuleParameterValueMapper<AccommodationOfferingModel>
{

	private RuleAccommodationOfferingDao ruleAccommodationOfferingDao;

	@Override
	public String toString(final AccommodationOfferingModel accommodationOfferingModel)
	{
		ServicesUtil.validateParameterNotNull(accommodationOfferingModel, "Object cannot be null");
		return accommodationOfferingModel.getCode();
	}

	@Override
	public AccommodationOfferingModel fromString(final String code)
	{
		return getRuleAccommodationOfferingDao().findAccommodationOffering(code);
	}


	/**
	 * Gets rule accommodation offering dao.
	 *
	 * @return the rule accommodation offering dao
	 */
	protected RuleAccommodationOfferingDao getRuleAccommodationOfferingDao()
	{
		return ruleAccommodationOfferingDao;
	}

	/**
	 * Sets rule accommodation offering dao.
	 *
	 * @param ruleAccommodationOfferingDao
	 * 		the rule accommodation offering dao
	 */
	@Required
	public void setRuleAccommodationOfferingDao(final RuleAccommodationOfferingDao ruleAccommodationOfferingDao)
	{
		this.ruleAccommodationOfferingDao = ruleAccommodationOfferingDao;
	}
}
