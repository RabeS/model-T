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

package de.hybris.platform.travelfulfilmentprocess.actions.b2b;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.orderprocessing.events.OrderPlacedEvent;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.servicelayer.event.EventService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


/**
 * The type Send order placed notification action test.
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class SendOrderPlacedNotificationActionTest
{
	@InjectMocks
	SendOrderPlacedNotificationAction sendOrderPlacedNotificationAction;

	@Mock
	EventService eventService;

	@Test
	public void testExecuteAction()
	{
		final OrderProcessModel process = new OrderProcessModel();
		Mockito.doNothing().when(eventService).publishEvent(Mockito.any(OrderPlacedEvent.class));
		sendOrderPlacedNotificationAction.executeAction(process);
	}

}
