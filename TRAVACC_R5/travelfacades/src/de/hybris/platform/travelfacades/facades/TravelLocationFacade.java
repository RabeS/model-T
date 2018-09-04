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

package de.hybris.platform.travelfacades.facades;

import de.hybris.platform.commercefacades.travel.LocationData;


/**
 * Facade which provides functionality related to {@link LocationData} such as retrieving location by its code.
 */
public interface TravelLocationFacade
{
	/**
	 * Retrieves a location by its code.
	 *
	 * @param locationCode
	 * @return LocationData
	 */
	LocationData getLocation(String locationCode);
}
