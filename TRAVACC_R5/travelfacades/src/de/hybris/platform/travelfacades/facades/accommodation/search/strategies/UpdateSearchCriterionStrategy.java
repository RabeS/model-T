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
package de.hybris.platform.travelfacades.facades.accommodation.search.strategies;


import de.hybris.platform.commercefacades.accommodation.AccommodationOfferingDayRateData;
import de.hybris.platform.commercefacades.accommodation.search.CriterionData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.travelfacades.facades.accommodation.search.AccommodationSearchFacade;
import de.hybris.platform.travelservices.search.facetdata.AccommodationOfferingSearchPageData;


/**
 * This strategy will be used by the {@link AccommodationSearchFacade} to update the {@link CriterionData} with the search
 * related values during an accommodation search.
 */
public interface UpdateSearchCriterionStrategy
{
	/**
	 * Apply strategy.
	 *
	 * @param criterion
	 * 		the criterion
	 * @param searchPageData
	 * 		the search page data
	 */
	void applyStrategy(CriterionData criterion, AccommodationOfferingSearchPageData<SearchStateData,
			AccommodationOfferingDayRateData> searchPageData);
}
