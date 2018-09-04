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
package de.hybris.platform.travelservices.dao.impl;

import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.travelservices.dao.ScheduleConfigurationDao;
import de.hybris.platform.travelservices.model.travel.ScheduleConfigurationModel;

import java.util.List;


/**
 * Implementation of the DAO on Schedule Configuration model objects. Default implementation of the
 * {@link de.hybris.platform.travelservices.dao.ScheduleConfigurationDao} interface.
 */
public class DefaultScheduleConfigurationDao extends DefaultGenericDao<ScheduleConfigurationModel> implements ScheduleConfigurationDao
{
	/**
	 * @param typecode
	 */
	public DefaultScheduleConfigurationDao(final String typecode)
	{
		super(typecode);
	}

	/**
	 * This method to retrieve all ScheduleConfigurations.
	 */
	@Override
	public List<ScheduleConfigurationModel> findAllScheduleConfigurations()
	{
		return find();
	}

}
