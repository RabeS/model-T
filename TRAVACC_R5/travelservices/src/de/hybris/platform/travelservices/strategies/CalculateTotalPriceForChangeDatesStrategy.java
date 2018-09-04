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

package de.hybris.platform.travelservices.strategies;

import de.hybris.platform.commercefacades.accommodation.AccommodationAvailabilityResponseData;

import java.math.BigDecimal;


/**
 * The interface Calculate total price for change dates strategy.
 */
public interface CalculateTotalPriceForChangeDatesStrategy
{
	/**
	 * Retrieves the total price of accommodation booking
	 *
	 * @param accommodationAvailabilityResponse
	 * 		the accommodation availability response
	 * @param orderCode
	 * 		the order code
	 *
	 * @return big decimal
	 */
	BigDecimal calculate(AccommodationAvailabilityResponseData accommodationAvailabilityResponse, String orderCode);

}
