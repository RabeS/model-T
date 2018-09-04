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

package de.hybris.platform.travelfacades.fare.search.strategies;

import java.util.List;


/**
 * Interface for strategies returning a list of location codes according to the location passed as parameter
 */
public interface LocationCodesResolvingStrategy
{
	/**
	 * Returns a list of location codes to be used during fare search
	 * 
	 * @param locationParameter
	 * @return
	 */
	List<String> getLocationCodes(String locationParameter);
}
