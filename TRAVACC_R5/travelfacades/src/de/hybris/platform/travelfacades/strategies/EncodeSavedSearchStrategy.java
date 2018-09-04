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

import de.hybris.platform.commercefacades.travel.search.data.SavedSearchData;


/**
 * Strategy responsible for encoding Data Object containing the values of the Search criteria into String representing
 * Search to be Saved.
 */
public interface EncodeSavedSearchStrategy
{
	/**
	 * Converts SavedSearchData to Fare Selection Search URL.
	 *
	 * @param source
	 * 		Object of SavedSearchData
	 * @return encodedSearch encoded search
	 */
	String getEncodedSearch(SavedSearchData source);

}
