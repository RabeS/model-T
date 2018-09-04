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
package de.hybris.platform.travelrulesengine.dao.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.travelrulesengine.dao.RuleRatePlanDao;
import de.hybris.platform.travelservices.model.accommodation.RatePlanModel;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;


/**
 * The type Default rule rate plan dao.
 */
public class DefaultRuleRatePlanDao extends DefaultGenericDao<RatePlanModel> implements RuleRatePlanDao
{

	private static final Logger LOG = Logger.getLogger(DefaultRuleRatePlanDao.class);

	/**
	 * Instantiates a new Default rule rate plan dao.
	 *
	 * @param typecode
	 * 		the typecode
	 */
	public DefaultRuleRatePlanDao(final String typecode)
	{
		super(typecode);
	}

	@Override
	public RatePlanModel findRatePlan(final String code)
	{
		validateParameterNotNull(code, "Rate Plan code must not be null!");
		final List<RatePlanModel> ratePlanModels = find(Collections.singletonMap(RatePlanModel.CODE, (Object) code));
		if (CollectionUtils.isEmpty(ratePlanModels))
		{
			LOG.info("No result for the given query");
			return null;
		}
		return ratePlanModels.get(0);
	}
}
