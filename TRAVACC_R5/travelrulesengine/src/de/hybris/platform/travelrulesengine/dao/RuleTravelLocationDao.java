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
package de.hybris.platform.travelrulesengine.dao;

import de.hybris.platform.travelservices.model.travel.LocationModel;

import java.util.List;


/**
 * The interface Rule travel location dao.
 */
public interface RuleTravelLocationDao
{

	/**
	 * Find location location model.
	 *
	 * @param code
	 * 		the code
	 * @return the location model
	 */
	LocationModel findLocation(String code);

	/**
	 * Find locations by name list.
	 *
	 * @param name
	 * 		the name
	 * @return the list
	 */
	List<LocationModel> findLocationsByName(String name);
}