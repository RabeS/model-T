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
package de.hybris.platform.travelcommercewebservices.facades.order.impl;

import de.hybris.platform.acceleratorfacades.order.AcceleratorCheckoutFacade;
import de.hybris.platform.b2bacceleratorfacades.checkout.data.PlaceOrderData;
import de.hybris.platform.commercefacades.order.CheckoutFacade;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.travel.reservation.data.ReservationData;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.payment.AdapterException;
import de.hybris.platform.travelcommercewebservices.facades.order.TravelOrderWsFacade;
import de.hybris.platform.travelfacades.facades.BookingFacade;
import de.hybris.platform.travelfacades.facades.customer.TravelCustomerFacade;
import de.hybris.platform.travelfacades.order.TravelB2BCheckoutFacade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


/**
 * The type Default travel order ws facade.
 */
public class DefaultTravelOrderWsFacade implements TravelOrderWsFacade
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultTravelOrderWsFacade.class);

	private TravelCustomerFacade travelCustomerFacade;
	private TravelB2BCheckoutFacade b2bCheckoutFacade;
	private AcceleratorCheckoutFacade acceleratorCheckoutFacade;
	private BookingFacade bookingFacade;

	/**
	 * @param securityCode
	 * @return ReservationData
	 * @throws InvalidCartException
	 */
	@Override
	public ReservationData placeOrder(final String securityCode) throws InvalidCartException
	{
		final ReservationData reservationData = null;

		// authorize, if failure occurs don't allow to place the order
		try
		{
			if (travelCustomerFacade.isCurrentUserB2bCustomer())
			{
				getB2bCheckoutFacade().authorizePayment(securityCode);
			}
			else
			{
				getAcceleratorCheckoutFacade().authorizePayment(securityCode);
			}
		}
		catch ( final AdapterException ae )
		{
			LOG.error(ae.getMessage(), ae);
			return reservationData;
		}
		OrderData orderData = null;
		try
		{
			if (travelCustomerFacade.isCurrentUserB2bCustomer())
			{
				final PlaceOrderData placeOrderData = new PlaceOrderData();
				placeOrderData.setSecurityCode(securityCode);
				placeOrderData.setTermsCheck(true);

				orderData = getB2bCheckoutFacade().placeOrder(placeOrderData);
			}
			else
			{
				orderData = getAcceleratorCheckoutFacade().placeOrder();
			}
		}
		catch (final Exception e)
		{
			LOG.error("Failed to place Order", e);
			return reservationData;
		}

		return getBookingFacade().getBookingByBookingReference(orderData.getCode());
	}

	/**
	 * Gets travel customer facade.
	 *
	 * @return the travelCustomerFacade
	 */
	protected TravelCustomerFacade getTravelCustomerFacade()
	{
		return travelCustomerFacade;
	}

	/**
	 * Sets travel customer facade.
	 *
	 * @param travelCustomerFacade
	 * 		the travelCustomerFacade to set
	 */
	@Required
	public void setTravelCustomerFacade(final TravelCustomerFacade travelCustomerFacade)
	{
		this.travelCustomerFacade = travelCustomerFacade;
	}

	/**
	 * Gets b 2 b checkout facade.
	 *
	 * @return the b2bCheckoutFacade
	 */
	protected TravelB2BCheckoutFacade getB2bCheckoutFacade()
	{
		return b2bCheckoutFacade;
	}

	/**
	 * Sets b 2 b checkout facade.
	 *
	 * @param b2bCheckoutFacade
	 * 		the b2bCheckoutFacade to set
	 */
	@Required
	public void setB2bCheckoutFacade(final TravelB2BCheckoutFacade b2bCheckoutFacade)
	{
		this.b2bCheckoutFacade = b2bCheckoutFacade;
	}

	/**
	 * Gets booking facade.
	 *
	 * @return the bookingFacade
	 */
	protected BookingFacade getBookingFacade()
	{
		return bookingFacade;
	}

	/**
	 * Sets booking facade.
	 *
	 * @param bookingFacade
	 * 		the bookingFacade to set
	 */
	@Required
	public void setBookingFacade(final BookingFacade bookingFacade)
	{
		this.bookingFacade = bookingFacade;
	}

	/**
	 * Gets accelerator checkout facade.
	 *
	 * @return the accelerator checkout facade
	 */
	protected AcceleratorCheckoutFacade getAcceleratorCheckoutFacade()
	{
		return acceleratorCheckoutFacade;
	}

	/**
	 * Sets accelerator checkout facade.
	 *
	 * @param acceleratorCheckoutFacade
	 * 		the accelerator checkout facade
	 */
	@Required
	public void setAcceleratorCheckoutFacade(final AcceleratorCheckoutFacade acceleratorCheckoutFacade)
	{
		this.acceleratorCheckoutFacade = acceleratorCheckoutFacade;
	}
}
