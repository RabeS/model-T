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

package de.hybris.platform.travelservices.services;

import de.hybris.platform.travelservices.model.accommodation.AccommodationProviderModel;

/**
 * Accommodation Provider Service interface which provides functionality to manage Accommodation Provider.
 */
public interface AccommodationProviderService {

	/**
	 * Returns AccommodationProviderModel for the given accommodationProviderCode
	 *
	 * @param accommodationProviderCode
	 * 		the codes of the Accommodation Provider
	 * @return the object of AccommodationProviderModel
	 */
	AccommodationProviderModel getAccommodationProviderForProviderCode(String accommodationProviderCode);

}
