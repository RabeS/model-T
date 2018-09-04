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
package de.hybris.platform.travelservices.ordercancel.impl.denialstrategies;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.enums.OrderCancelState;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;


/**
 * The type Default travel order cancel state mapping strategy test.
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultTravelOrderCancelStateMappingStrategyTest
{

	@InjectMocks
	private final DefaultTravelOrderCancelStateMappingStrategy strategy = new DefaultTravelOrderCancelStateMappingStrategy();

	@Test
	public void testCancelledOrderState()
	{
		final OrderModel order = new OrderModel();
		order.setStatus(OrderStatus.CANCELLED);

		final OrderCancelState state = strategy.getOrderCancelState(order);

		Assert.assertEquals(OrderCancelState.CANCELIMPOSSIBLE, state);
	}

	@Test
	public void testCancellingOrderState()
	{
		final OrderModel order = new OrderModel();
		order.setStatus(OrderStatus.CANCELLING);

		final OrderCancelState state = strategy.getOrderCancelState(order);

		Assert.assertEquals(OrderCancelState.CANCELIMPOSSIBLE, state);
	}

	@Test
	public void testOtherOrderState()
	{
		final OrderModel order = new OrderModel();
		order.setStatus(OrderStatus.ACTIVE);

		final OrderCancelState state = strategy.getOrderCancelState(order);

		Assert.assertEquals(OrderCancelState.CANCELPOSSIBLE, state);
	}

}
