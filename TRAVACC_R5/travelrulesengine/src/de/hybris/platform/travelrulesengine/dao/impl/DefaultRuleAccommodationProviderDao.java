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

import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;
import de.hybris.platform.travelrulesengine.dao.RuleAccommodationProviderDao;
import de.hybris.platform.travelservices.model.accommodation.AccommodationProviderModel;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;


/**
 * The type Default rule accommodation provider dao.
 */
public class DefaultRuleAccommodationProviderDao extends DefaultGenericDao<AccommodationProviderModel>
		implements RuleAccommodationProviderDao
{
	private static final Logger LOG = Logger.getLogger(DefaultRuleAccommodationProviderDao.class);

	/**
	 * Instantiates a new Default rule accommodation provider dao.
	 *
	 * @param typecode
	 * 		the typecode
	 */
	public DefaultRuleAccommodationProviderDao(final String typecode)
	{
		super(typecode);
	}

	@Override
	public AccommodationProviderModel findAccommodationProvider(final String code)
	{
		validateParameterNotNull(code, "Accommodation Provider code must not be null!");
		final List<AccommodationProviderModel> accommodationProviderModels = find(
				Collections.singletonMap(AccommodationProviderModel.CODE, (Object) code));
		if (CollectionUtils.isEmpty(accommodationProviderModels))
		{
			LOG.info("No result for the given query");
			return null;
		}
		else if (accommodationProviderModels.size() > 1)
		{
			LOG.warn("Found " + accommodationProviderModels.size() + " results for the given query");
			return null;
		}
		return accommodationProviderModels.get(0);
	}
}
