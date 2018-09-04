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

import de.hybris.platform.travelservices.model.travel.AccommodationCategoryModel;


/**
 * The interface Rule accommodation category dao.
 */
public interface RuleAccommodationCategoryDao
{

	/**
	 * Find accommodation category accommodation category model.
	 *
	 * @param code
	 * 		the code
	 * @return the accommodation category model
	 */
	AccommodationCategoryModel findAccommodationCategory(String code);
}
