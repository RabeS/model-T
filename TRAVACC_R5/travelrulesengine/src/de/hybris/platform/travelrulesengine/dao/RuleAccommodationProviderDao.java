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
package de.hybris.platform.travelrulesengine.dao;

import de.hybris.platform.travelservices.model.accommodation.AccommodationProviderModel;


/**
 * The interface Rule accommodation provider dao.
 */
public interface RuleAccommodationProviderDao
{
	/**
	 * Find accommodation provider accommodation provider model.
	 *
	 * @param code
	 * 		the code
	 * @return the accommodation provider model
	 */
	AccommodationProviderModel findAccommodationProvider(String code);
}
