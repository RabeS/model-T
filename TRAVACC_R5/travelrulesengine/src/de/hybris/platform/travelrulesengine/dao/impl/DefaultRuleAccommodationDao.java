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
import de.hybris.platform.travelrulesengine.dao.RuleAccommodationDao;
import de.hybris.platform.travelservices.model.product.AccommodationModel;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;


/**
 * The type Default implementation of the {@link RuleAccommodationDao}
 */
public class DefaultRuleAccommodationDao extends DefaultGenericDao<AccommodationModel> implements RuleAccommodationDao
{
	private static final Logger LOG = Logger.getLogger(DefaultRuleAccommodationDao.class);

	/**
	 * Instantiates a new Default rule accommodation dao.
	 *
	 * @param typecode
	 * 		the typecode
	 */
	public DefaultRuleAccommodationDao(final String typecode)
	{
		super(typecode);
	}

	@Override
	public List<AccommodationModel> findAccommodation(final String code)
	{
		validateParameterNotNull(code, "Accommodation Id must not be null!");

		final List<AccommodationModel> accommodationModels = find(Collections.singletonMap(AccommodationModel.CODE, (Object) code));

		if (CollectionUtils.isEmpty(accommodationModels))
		{
			LOG.info("No result for the given query");
			return null;
		}
		return accommodationModels;
	}
}
