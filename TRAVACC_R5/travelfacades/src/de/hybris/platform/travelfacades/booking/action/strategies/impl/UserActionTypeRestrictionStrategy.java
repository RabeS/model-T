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

package de.hybris.platform.travelfacades.booking.action.strategies.impl;

import de.hybris.platform.commercefacades.travel.BookingActionData;
import de.hybris.platform.commercefacades.travel.reservation.data.ReservationData;
import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.commerceservices.enums.CustomerType;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.travelfacades.booking.action.strategies.BookingActionEnabledEvaluatorStrategy;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Strategy to evaluate the enabled property of the List<BookingActionData>, based on the current user type. If the
 * booker that placed the booking was a registered customer, the enabled property is set to true only if the current
 * customer is the booker, false otherwise. If the booker that placed the booking was a guest customer, the enabled
 * property remains true.
 */
public class UserActionTypeRestrictionStrategy implements BookingActionEnabledEvaluatorStrategy
{
	private CustomerAccountService customerAccountService;
	private BaseStoreService baseStoreService;
	private UserService userService;

	private static final String ALTERNATIVE_MESSAGE = "booking.action.user.type.alternative.message";

	@Override
	public void applyStrategy(final List<BookingActionData> bookingActionDataList, final ReservationData reservationData)
	{
		final List<BookingActionData> enabledBookingActions = bookingActionDataList.stream()
				.filter(bookingActionData -> bookingActionData.isEnabled()).collect(Collectors.toList());
		if (enabledBookingActions.isEmpty())
		{
			return;
		}

		final BaseStoreModel baseStoreModel = getBaseStoreService().getCurrentBaseStore();
		final OrderModel orderModel = getCustomerAccountService().getOrderForCode(reservationData.getCode(), baseStoreModel);
		final UserModel booker = orderModel.getUser();

		boolean enabled = true;
		if (!CustomerType.GUEST.equals(((CustomerModel) booker).getType()))
		{
			//the order has been placed by a registered user
			//check if the current user is the booker
			enabled = booker.equals(getUserService().getCurrentUser());
		}

		if (!enabled)
		{
			for (final BookingActionData bookingActionData : enabledBookingActions)
			{
				bookingActionData.setEnabled(enabled);
				bookingActionData.getAlternativeMessages().add(ALTERNATIVE_MESSAGE);
			}
		}
	}

	/**
	 * @return the customerAccountService
	 */
	protected CustomerAccountService getCustomerAccountService()
	{
		return customerAccountService;
	}

	/**
	 * @param customerAccountService
	 *           the customerAccountService to set
	 */
	public void setCustomerAccountService(final CustomerAccountService customerAccountService)
	{
		this.customerAccountService = customerAccountService;
	}

	/**
	 * @return the baseStoreService
	 */
	protected BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	/**
	 * @param baseStoreService
	 *           the baseStoreService to set
	 */
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	/**
	 * @return the userService
	 */
	protected UserService getUserService()
	{
		return userService;
	}

	/**
	 * @param userService
	 *           the userService to set
	 */
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

}
