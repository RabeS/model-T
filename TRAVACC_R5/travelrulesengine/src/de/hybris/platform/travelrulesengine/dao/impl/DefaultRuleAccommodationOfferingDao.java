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
import de.hybris.platform.travelrulesengine.dao.RuleAccommodationOfferingDao;
import de.hybris.platform.travelservices.model.warehouse.AccommodationOfferingModel;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;


/**
 * The type Default rule accommodation offering dao.
 */
public class DefaultRuleAccommodationOfferingDao extends DefaultGenericDao<AccommodationOfferingModel>
		implements RuleAccommodationOfferingDao
{
	private static final Logger LOG = Logger.getLogger(DefaultRuleAccommodationOfferingDao.class);

	/**
	 * Instantiates a new Default rule accommodation offering dao.
	 *
	 * @param typecode
	 * 		the typecode
	 */
	public DefaultRuleAccommodationOfferingDao(final String typecode)
	{
		super(typecode);
	}

	@Override
	public AccommodationOfferingModel findAccommodationOffering(final String code)
	{
		validateParameterNotNull(code, "Accommodation Offering code must not be null!");
		final List<AccommodationOfferingModel> accommodationOfferings = find(
				Collections.singletonMap(AccommodationOfferingModel.CODE, (Object) code));
		if (CollectionUtils.isEmpty(accommodationOfferings))
		{
			LOG.info("No result for the given query");
			return null;
		}
		else if (accommodationOfferings.size() > 1)
		{
			LOG.warn("Found " + accommodationOfferings.size() + " results for the given query");
			return null;
		}
		return accommodationOfferings.get(0);
	}
}
