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

package de.hybris.platform.travelfacades.facades.accommodation.search.manager;

import de.hybris.platform.commercefacades.accommodation.AccommodationOfferingDayRateData;
import de.hybris.platform.commercefacades.accommodation.AccommodationSearchRequestData;
import de.hybris.platform.commercefacades.accommodation.AccommodationSearchResponseData;

import java.util.List;


/**
 * Pipeline Manager class that will build a {@link AccommodationSearchResponseData} after executing a list of handlers
 * on the list of {@link AccommodationOfferingDayRateData} and {@link AccommodationSearchRequestData} given as inputs
 */
public interface AccommodationOfferingSearchPipelineManager
{
	/**
	 * Execute pipeline accommodation search response data.
	 *
	 * @param accommodationOfferingDayRates
	 * 		the accommodation offering day rates
	 * @param accommodationSearchRequest
	 * 		the accommodation search request
	 * @return the accommodation search response data
	 */
	AccommodationSearchResponseData executePipeline(List<AccommodationOfferingDayRateData> accommodationOfferingDayRates,
			AccommodationSearchRequestData accommodationSearchRequest);
}
