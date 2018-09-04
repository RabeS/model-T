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

import de.hybris.platform.commercefacades.travel.ancillary.accommodation.data.ConfiguredAccommodationData;


/**
 * Facade which provides methods relevant to Configured Accommodation
 */
public interface ConfiguredAccommodationFacade
{

	/**
	 * Retrieves a ConfiguredAccommodation DTO for given accommodation number
	 *
	 * @param accommodationUid
	 * 		the accommodation uid
	 * @return ConfiguredAccommodataion DTO
	 */
	ConfiguredAccommodationData getAccommodation(String accommodationUid);
}
