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

package de.hybris.platform.travelfacades.reservation.handlers.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.accommodation.AccommodationReservationData;
import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


/**
 * unit test for Accommodation reservation status handler.
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class AccommodationReservationStatusHandlerTest
{

	@InjectMocks
	AccommodationReservationStatusHandler accommodationReservationStatusHandler;

	@Mock
	private EnumerationService enumerationService;
	@Mock
	private I18NService i18NService;
	@Mock
	private BaseStoreService baseStoreService;
	@Mock
	private CustomerAccountService customerAccountService;

	@Test
	public void testPopulateWithoutVersion()
	{
		final OrderModel orderModel = new OrderModel()
		{
			@Override
			public OrderStatus getAccommodationOrderStatus()
			{
				return OrderStatus.ACTIVE;
			}

			@Override
			public String getCode()
			{
				return "0001";
			}

			@Override
			public OrderStatus getStatus()
			{
				return OrderStatus.ACTIVE;
			}

		};

		final Locale locale = Locale.ENGLISH;
		given(i18NService.getCurrentLocale()).willReturn(locale);
		given(enumerationService.getEnumerationName(orderModel.getStatus(), locale)).willReturn("Active");
		final AccommodationReservationData accommodationReservationData = new AccommodationReservationData();
		accommodationReservationStatusHandler.handle(orderModel, accommodationReservationData);
		assertEquals("ACTIVE", accommodationReservationData.getBookingStatusCode());
		assertEquals("Active", accommodationReservationData.getBookingStatusName());
	}

	@Test
	public void testPopulateWithVersion()
	{
		final OrderModel orderModel = new OrderModel()
		{
			@Override
			public OrderStatus getAccommodationOrderStatus()
			{
				return OrderStatus.ACTIVE;
			}

			@Override
			public String getCode()
			{
				return "0001";
			}

			@Override
			public OrderStatus getStatus()
			{
				return OrderStatus.ACTIVE;
			}

			@Override
			public String getVersionID()
			{
				return "1.0";
			}
		};
		orderModel.setAccommodationOrderStatus(orderModel.getStatus());
		final BaseStoreModel baseStore = new BaseStoreModel();
		given(baseStoreService.getCurrentBaseStore()).willReturn(baseStore);
		given(customerAccountService.getOrderForCode(orderModel.getCode(), baseStore)).willReturn(orderModel);
		final Locale locale = Locale.ENGLISH;
		given(i18NService.getCurrentLocale()).willReturn(locale);
		given(enumerationService.getEnumerationName(orderModel.getStatus(), locale)).willReturn("Active");
		final AccommodationReservationData accommodationReservationData = new AccommodationReservationData();
		accommodationReservationStatusHandler.handle(orderModel, accommodationReservationData);
		assertEquals("ACTIVE", accommodationReservationData.getBookingStatusCode());
		assertEquals("Active", accommodationReservationData.getBookingStatusName());
	}

}
