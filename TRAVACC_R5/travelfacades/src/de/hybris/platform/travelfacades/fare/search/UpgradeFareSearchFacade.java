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

package de.hybris.platform.travelfacades.fare.search;

import de.hybris.platform.commercefacades.travel.FareSelectionData;


/**
 * Interface for the Upgrade Fare Search Facade
 */
public interface UpgradeFareSearchFacade
{
	/**
	 * Performs a search for fare selection options based on fare search request
	 *
	 * @return FareSelectionData object with available fare options
	 */
	FareSelectionData doUpgradeSearch();
}
