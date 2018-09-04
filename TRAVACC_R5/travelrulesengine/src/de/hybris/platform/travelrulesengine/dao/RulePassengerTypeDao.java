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

import de.hybris.platform.travelservices.model.user.PassengerTypeModel;


/**
 * The interface Rule passenger type dao.
 */
public interface RulePassengerTypeDao
{

	/**
	 * Find passenger type by code passenger type model.
	 *
	 * @param code
	 * 		the code
	 * @return the passenger type model
	 */
	PassengerTypeModel findPassengerTypeByCode(String code);
}
