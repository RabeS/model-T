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
import de.hybris.platform.travelrulesengine.dao.RuleAccommodationCategoryDao;
import de.hybris.platform.travelservices.model.travel.AccommodationCategoryModel;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;


/**
 * The type Default rule accommodation category dao.
 */
public class DefaultRuleAccommodationCategoryDao extends DefaultGenericDao<AccommodationCategoryModel>
		implements RuleAccommodationCategoryDao
{
	private static final Logger LOG = Logger.getLogger(DefaultRuleAccommodationCategoryDao.class);

	/**
	 * Instantiates a new Default rule accommodation category dao.
	 *
	 * @param typecode
	 * 		the typecode
	 */
	public DefaultRuleAccommodationCategoryDao(final String typecode)
	{
		super(typecode);
	}

	@Override
	public AccommodationCategoryModel findAccommodationCategory(final String code)
	{
		validateParameterNotNull(code, "Accommodation Category code must not be null!");
		final List<AccommodationCategoryModel> accommodationCategoryModels = find(
				Collections.singletonMap(AccommodationCategoryModel.CODE, (Object) code));
		if (CollectionUtils.isEmpty(accommodationCategoryModels))
		{
			LOG.info("No result for the given query");
			return null;
		}
		return accommodationCategoryModels.get(0);
	}
}
