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
import de.hybris.platform.travelservices.model.accommodation.AccommodationProviderModel;

/**
 * AccommodationProviderDao interface which provides functionality to manage Accommodation Provider.
 */
public interface AccommodationProviderDao extends Dao
{
	/**
	 * Returns an object of AccommodationProviderModel for the given accommodationProviderCode
	 *
	 * @param accommodationProviderCode
	 * 		the code of the Accommodation Provider
	 * @return object of AccommodationProviderModel
	 */
	AccommodationProviderModel findAccommodationProviderForProviderCode(String accommodationProviderCode);
}
