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

package de.hybris.platform.travelservices.storelocator;

import de.hybris.platform.commercefacades.storelocator.data.TimeZoneResponseData;
import de.hybris.platform.storelocator.GeoWebServiceWrapper;
import de.hybris.platform.storelocator.exception.GeoServiceWrapperException;
import de.hybris.platform.storelocator.location.Location;


/**
 * The interface Travel geo web service wrapper.
 */
public interface TravelGeoWebServiceWrapper extends GeoWebServiceWrapper
{
	/**
	 * Time zone offset time zone response data.
	 *
	 * @param address
	 * 		the address
	 * @return the time zone response data
	 * @throws GeoServiceWrapperException
	 * 		the geo service wrapper exception
	 */
	TimeZoneResponseData timeZoneOffset(final Location address) throws GeoServiceWrapperException;
}
