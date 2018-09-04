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

import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.cronjob.enums.DayOfWeek;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterValueMapper;

import org.springframework.beans.factory.annotation.Required;


/**
 * The type Sales application parameter value mapper.
 */
public class DayOfWeekParameterValueMapper implements RuleParameterValueMapper<Object>
{
	private EnumerationService enumerationService;

	@Override
	public String toString(final Object dayOfWeek)
	{
		if (dayOfWeek instanceof DayOfWeek)
		{
			return ((DayOfWeek) dayOfWeek).getCode();
		}
		return ((EnumerationValueModel) dayOfWeek).getCode();
	}

	@Override
	public Object fromString(final String code)
	{
		return getEnumerationService().getEnumerationValue(DayOfWeek.class, code);
	}

	/**
	 * Gets enumeration service.
	 *
	 * @return the enumeration service
	 */
	protected EnumerationService getEnumerationService()
	{
		return enumerationService;
	}

	/**
	 * Sets enumeration service.
	 *
	 * @param enumerationService
	 * 		the enumeration service
	 */
	@Required
	public void setEnumerationService(final EnumerationService enumerationService)
	{
		this.enumerationService = enumerationService;
	}
}
