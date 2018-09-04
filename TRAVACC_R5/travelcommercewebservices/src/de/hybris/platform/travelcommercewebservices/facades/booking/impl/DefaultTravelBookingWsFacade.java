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

package de.hybris.platform.travelcommercewebservices.facades.booking.impl;

import de.hybris.platform.commercefacades.travel.order.PaymentTransactionData;
import de.hybris.platform.commercefacades.travel.reservation.data.ReservationData;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.travelcommercewebservices.constants.TravelcommercewebservicesConstants;
import de.hybris.platform.travelcommercewebservices.facades.booking.TravelBookingWsFacade;
import de.hybris.platform.travelcommercewebservices.services.impl.DefaultTravelBookingWsService;
import de.hybris.platform.travelfacades.facades.impl.DefaultBookingFacade;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


/**
 * Facade for operations on order {@link TravelBookingWsFacade}.
 */
public class DefaultTravelBookingWsFacade extends DefaultBookingFacade implements TravelBookingWsFacade
{
	private DefaultTravelBookingWsService travelBookingWsService;

	@Override
	public ReservationData getBookingByBookingReference(final String bookingReference)
	{
		//Retrieve order with bookingReferenceNumber
		final OrderModel orderModel = getTravelBookingWsService().getOrder(bookingReference);
		//Convert orderModel to ReservationData
		return convertOrderModelToReservationData(orderModel);
	}

	@Override
	public void addPaymentTransactionsToSession(final List<PaymentTransactionData> paymentTransactions)
	{
		getSessionService().setAttribute(TravelcommercewebservicesConstants.PAYMENT_TRANSACTIONS, paymentTransactions);
	}

	/**
	 * Gets the travel booking ws service
	 *
	 * @return {@travelBookingWsService}
	 */
	protected DefaultTravelBookingWsService getTravelBookingWsService()
	{
		return travelBookingWsService;
	}

	/**
	 * Sets the enumeration service.
	 *
	 * @param travelBookingWsService
	 * 		the travel booking ws service
	 */
	@Required
	public void setTravelBookingWsService(final DefaultTravelBookingWsService travelBookingWsService)
	{
		this.travelBookingWsService = travelBookingWsService;
	}
}
