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

package de.hybris.platform.travelfacades.facades.accommodation.handlers;

import de.hybris.platform.commercefacades.accommodation.AccommodationAvailabilityRequestData;
import de.hybris.platform.commercefacades.accommodation.AccommodationAvailabilityResponseData;


/**
 * Interface for Accommodation Details handlers
 */
public interface AccommodationDetailsHandler
{
	/**
	 * Handle method.
	 *
	 * @param availabilityRequestData
	 * 		the availability request data
	 * @param accommodationAvailabilityResponseData
	 * 		the accommodation availability response data
	 */
	void handle(AccommodationAvailabilityRequestData availabilityRequestData,
			AccommodationAvailabilityResponseData accommodationAvailabilityResponseData);
}
