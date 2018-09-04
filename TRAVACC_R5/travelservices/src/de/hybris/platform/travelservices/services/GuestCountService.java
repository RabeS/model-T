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

package de.hybris.platform.travelservices.services;

import de.hybris.platform.travelservices.model.order.GuestCountModel;


/**
 * Interface that exposes Guest Count specific services
 */
public interface GuestCountService
{

	/**
	 * Return the GuestCountModel corresponding to the given parameters
	 *
	 * @param passengerTypeCode
	 * 		the passenger type code
	 * @param quantity
	 * 		the quantity
	 *
	 * @return the GuestCountModel
	 */
	GuestCountModel getGuestCount(String passengerTypeCode, int quantity);
}
