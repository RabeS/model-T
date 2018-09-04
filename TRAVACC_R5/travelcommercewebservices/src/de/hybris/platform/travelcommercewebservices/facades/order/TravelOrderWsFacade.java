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
package de.hybris.platform.travelcommercewebservices.facades.order;

import de.hybris.platform.commercefacades.travel.reservation.data.ReservationData;
import de.hybris.platform.order.InvalidCartException;


/**
 * Facade that exposes order functionality
 */
public interface TravelOrderWsFacade
{
	/**
	 * Place the order.
	 *
	 * @param securityCode
	 * 		the security code for the credit/debit card used.
	 * @return {@link ReservationData}
	 * @throws InvalidCartException
	 */
	ReservationData placeOrder(String securityCode) throws InvalidCartException;
}