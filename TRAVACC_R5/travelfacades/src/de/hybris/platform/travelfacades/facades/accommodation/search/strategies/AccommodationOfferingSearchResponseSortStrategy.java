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

import de.hybris.platform.commercefacades.accommodation.AccommodationSearchResponseData;


/**
 * Abstract strategy to sort the accommodationSearchResponseData
 */
public interface AccommodationOfferingSearchResponseSortStrategy
{

	/**
	 * Method to sort the AccommodationSearchResponseData
	 *
	 * @param accommodationSearchResponseData
	 * 		as the accommodationSearchResponseData to be sorted
	 */
	void sort(AccommodationSearchResponseData accommodationSearchResponseData);

}
