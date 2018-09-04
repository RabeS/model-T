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

package de.hybris.platform.travelfulfilmentprocess.actions.consignment;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.model.ModelService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


/**
 * The type Subprocess end action test.
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class SubprocessEndActionTest
{
	@InjectMocks
	SubprocessEndAction subprocessEndAction;

	@Mock
	BusinessProcessService businessProcessService;

	@Mock
	ModelService modelservice;

	@Test
	public void testExecuteAction()
	{
		final ConsignmentProcessModel process = new ConsignmentProcessModel();
		final OrderProcessModel orderProcess = new OrderProcessModel();
		process.setParentProcess(orderProcess);
		subprocessEndAction.executeAction(process);
	}

}
