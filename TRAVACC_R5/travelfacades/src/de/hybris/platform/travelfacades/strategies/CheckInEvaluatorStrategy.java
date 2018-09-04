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


import de.hybris.platform.commercefacades.travel.reservation.data.ReservationData;


/**
 * The interface Check in evaluator strategy.
 */
public interface CheckInEvaluatorStrategy
{

	/**
	 * Is check in possible boolean.
	 *
	 * @param reservation
	 * 		the reservation
	 * @param originDestinationRefNumber
	 * 		the origin destination ref number
	 * @return the boolean
	 */
	boolean isCheckInPossible(ReservationData reservation, int originDestinationRefNumber);
}
