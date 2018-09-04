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

package de.hybris.platform.travelfacades.facades.accommodation.search.handlers;

import de.hybris.platform.commercefacades.accommodation.AccommodationOfferingDayRateData;
import de.hybris.platform.commercefacades.accommodation.AccommodationSearchRequestData;
import de.hybris.platform.commercefacades.accommodation.AccommodationSearchResponseData;

import java.util.List;


/**
 * Interface for handlers classes that will be updating the {@link AccommodationSearchResponseData} based on the list of
 * {@link AccommodationOfferingDayRateData} and {@link AccommodationSearchRequestData}
 */
public interface AccommodationSearchHandler
{
	/**
	 * Handle method.
	 *
	 * @param accommodationOfferingDayRates
	 * 		the accommodation offering day rates
	 * @param accommodationSearchRequest
	 * 		the accommodation search request
	 * @param accommodationSearchResponse
	 * 		the accommodation search response
	 */
	void handle(List<AccommodationOfferingDayRateData> accommodationOfferingDayRates,
			AccommodationSearchRequestData accommodationSearchRequest, AccommodationSearchResponseData accommodationSearchResponse);
}
