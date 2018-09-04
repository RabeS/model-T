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

package de.hybris.platform.travelfulfilmentprocess.listeners;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.orderprocessing.events.PickupConfirmationEvent;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.travelfulfilmentprocess.constants.TravelfulfilmentprocessConstants;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


/**
 * The type Pickup confirmation event listener test.
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class PickupConfirmationEventListenerTest
{
	@InjectMocks
	private PickupConfirmationEventListener listener;
	@Mock
	private PickupConfirmationEvent pickupConfirmationEvent;
	@Mock
	private ConsignmentProcessModel process;
	@Mock
	private ConsignmentModel consignmentModel;
	@Mock
	private BusinessProcessService businessProcessService;

	@Test
	public void testOnEvent()
	{
		Mockito.when(pickupConfirmationEvent.getProcess()).thenReturn(process);
		Mockito.when(process.getConsignment()).thenReturn(consignmentModel);
		Mockito.when(consignmentModel.getConsignmentProcesses()).thenReturn(Stream.of(process).collect(Collectors.toList()));
		Mockito.when(process.getCode()).thenReturn("process1");
		Mockito.doNothing().when(businessProcessService)
				.triggerEvent("process1" + "_" + TravelfulfilmentprocessConstants.CONSIGNMENT_PICKUP);
		listener.onEvent(pickupConfirmationEvent);
	}
}
