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

import de.hybris.platform.commercefacades.travel.ancillary.data.OfferGroupData;

import java.util.List;


/**
 * Strategy responsible for sorting offer widgets on ancillary page so that they are in order specified by OfferSort
 * enum
 */
public interface OfferSortStrategy
{

	/**
	 * Applies the sorting strategy for OfferGroups based on OfferSort enum
	 *
	 * @param offerGroups
	 * 		the offer groups
	 * @return sorted list of Offer Groups
	 */
	List<OfferGroupData> applyStrategy(List<OfferGroupData> offerGroups);

}
