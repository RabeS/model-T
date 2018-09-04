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

package de.hybris.platform.travelservices.model.dynamic.attribute;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.servicelayer.model.attribute.AbstractDynamicAttributeHandler;
import de.hybris.platform.travelservices.enums.OrderEntryType;
import de.hybris.platform.travelservices.services.BookingService;

import org.springframework.beans.factory.annotation.Required;


/**
 * Attribute Handler implementation responsible to populate the accommodationOrderStatus property of the
 * {@link AbstractOrderModel}.
 */
public class AccommodationOrderStatusAttributeHandler extends AbstractDynamicAttributeHandler<OrderStatus, AbstractOrderModel>
{
	private BookingService bookingService;

	@Override
	public OrderStatus get(final AbstractOrderModel abstractOrderModel)
	{
		if (!getBookingService().checkIfAnyOrderEntryByType(abstractOrderModel, OrderEntryType.ACCOMMODATION))
		{
			return null;
		}

		return getBookingService().isReservationCancelled(abstractOrderModel, OrderEntryType.ACCOMMODATION) ?
				OrderStatus.CANCELLED :
				abstractOrderModel.getStatus();
	}

	@Override
	public void set(final AbstractOrderModel abstractOrderModel, final OrderStatus orderStatus)
	{
		// Override method to prevent exception while cloning the orderModel
	}

	/**
	 * @return the bookingService
	 */
	protected BookingService getBookingService()
	{
		return bookingService;
	}

	/**
	 * @param bookingService
	 *           the bookingService to set
	 */
	@Required
	public void setBookingService(final BookingService bookingService)
	{
		this.bookingService = bookingService;
	}

}
