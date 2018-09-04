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

import de.hybris.platform.travelservices.model.warehouse.AccommodationOfferingModel;


/**
 * The interface Rule accommodation offering dao.
 */
public interface RuleAccommodationOfferingDao
{

	/**
	 * Find accommodation offering model.
	 *
	 * @param code
	 * 		the code
	 * @return the accommodation offering model
	 */
	AccommodationOfferingModel findAccommodationOffering(String code);
}
