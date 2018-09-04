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
package de.hybris.platform.travelfacades.facades.accommodation.search.strategies.impl;


import de.hybris.platform.commercefacades.accommodation.AccommodationOfferingDayRateData;
import de.hybris.platform.commercefacades.accommodation.search.CriterionData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.travelfacades.facades.accommodation.search.strategies.UpdateSearchCriterionStrategy;
import de.hybris.platform.travelservices.search.facetdata.AccommodationOfferingSearchPageData;


/**
 * This strategy will update the {@link CriterionData} with the query string during an accommodation search.
 */
public class UpdateQueryStrategy implements UpdateSearchCriterionStrategy
{
	@Override
	public void applyStrategy(final CriterionData criterion, final AccommodationOfferingSearchPageData<SearchStateData,
			AccommodationOfferingDayRateData> searchPageData)
	{
		criterion.setQuery(searchPageData.getCurrentQuery().getQuery().getValue());
	}
}
