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

package de.hybris.platform.travelfacades.strategies;

import de.hybris.platform.commercefacades.travel.TransportOfferingData;

import java.util.List;
import java.util.Map;


/**
 * Strategy to create the display view of suggestion results.
 */
public interface SuggestionsDisplayStrategy
{

	/**
	 * Suggestion display types
	 */
	enum SuggestionType
	{
		ORIGIN, DESTINATION
	}

	/**
	 * Method to apply the strategy on a collection.
	 *
	 * @param suggestionType            the suggestion type
	 * @param searchText                the search text
	 * @param transportOfferingDataList the transport offering data list
	 * @return map
	 */
	Map<String, Map<String, String>> createStructuredView(SuggestionType suggestionType, String searchText,
			List<TransportOfferingData> transportOfferingDataList);

}
