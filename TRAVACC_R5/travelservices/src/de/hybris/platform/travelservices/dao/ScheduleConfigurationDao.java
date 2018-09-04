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
package de.hybris.platform.travelservices.dao;

import de.hybris.platform.servicelayer.internal.dao.Dao;
import de.hybris.platform.travelservices.model.travel.ScheduleConfigurationModel;

import java.util.List;


/**
 * ScheduleConfiguration Dao interface which provides functionality to manage Schedule Configuration.
 */
public interface ScheduleConfigurationDao extends Dao
{
	/**
	 * Return list of Schedule Configuration configured in the system.
	 *
	 * @return a list of ScheduleConfigurationModel
	 */
	List<ScheduleConfigurationModel> findAllScheduleConfigurations();
}
