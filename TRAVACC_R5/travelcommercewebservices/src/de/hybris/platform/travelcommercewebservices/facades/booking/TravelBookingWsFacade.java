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

package de.hybris.platform.travelcommercewebservices.facades.booking;

import de.hybris.platform.commercefacades.travel.order.PaymentTransactionData;
import de.hybris.platform.travelfacades.facades.BookingFacade;

import java.util.List;


/**
 * The interface Travel booking ws facade.
 */
public interface TravelBookingWsFacade extends BookingFacade
{

	/**
	 * Add payment transactions to session.
	 *
	 * @param paymentTransactions
	 * 		the payment transactions
	 */
	void addPaymentTransactionsToSession(List<PaymentTransactionData> paymentTransactions);
}
