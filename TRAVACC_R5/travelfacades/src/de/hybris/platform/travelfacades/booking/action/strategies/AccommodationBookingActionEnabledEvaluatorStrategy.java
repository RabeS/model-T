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

package de.hybris.platform.travelfacades.booking.action.strategies;

import de.hybris.platform.commercefacades.accommodation.AccommodationReservationData;
import de.hybris.platform.commercefacades.travel.AccommodationBookingActionData;

import java.util.List;


/**
 * Strategy that expose the method to evaluate the value of the enabled property for the List<BookingActionData>
 */
public interface AccommodationBookingActionEnabledEvaluatorStrategy
{
	/**
	 * Applies the strategy for List<BookingActionData>
	 *
	 * @param bookingActionDataList
	 * 		the booking action data list
	 * @param accommodationReservationData
	 * 		the accommodation reservation data
	 */
	void applyStrategy(List<AccommodationBookingActionData> bookingActionDataList,
			AccommodationReservationData accommodationReservationData);

}
