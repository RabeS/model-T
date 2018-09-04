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

package de.hybris.platform.travelservices.strategies.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.travelservices.enums.OrderEntryType;
import de.hybris.platform.travelservices.services.BookingService;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


/**
 * The type Refund all action strategy test.
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class RefundAllActionStrategyTest
{
	@InjectMocks
	RefundAllActionStrategy refundAllActionStrategy;

	@Mock
	private BookingService bookingService;

	@Test
	public void test()
	{
		final OrderModel order=new OrderModel();
		Mockito.when(bookingService.getOrderTotalPriceByType(order, OrderEntryType.TRANSPORT)).thenReturn(100d);
		Assert.assertEquals(100d, refundAllActionStrategy.applyStrategy(order, OrderEntryType.TRANSPORT), 0.001);
	}

}
