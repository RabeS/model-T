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
import de.hybris.platform.b2b.process.approval.model.B2BApprovalProcessModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;
import de.hybris.platform.servicelayer.model.ModelService;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


/**
 * The type Accelerator booking line entries test.
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class AcceleratorBookingLineEntriesTest
{
	@InjectMocks
	AcceleratorBookingLineEntries acceleratorBookingLineEntries;

	@Mock
	ModelService modelService;

	@Test
	public void testExecuteAction()
	{
		final B2BApprovalProcessModel b2BApprovalProcessModel = new B2BApprovalProcessModel();
		final OrderModel order = new OrderModel();
		b2BApprovalProcessModel.setOrder(order);

		Mockito.doNothing().when(modelService).refresh(order);


		Assert.assertEquals(acceleratorBookingLineEntries.executeAction(b2BApprovalProcessModel),
				AbstractSimpleDecisionAction.Transition.NOK);

		final PaymentInfoModel paymentInfo = new PaymentInfoModel();
		order.setPaymentInfo(paymentInfo);
		Assert.assertEquals(acceleratorBookingLineEntries.executeAction(b2BApprovalProcessModel),
				AbstractSimpleDecisionAction.Transition.OK);

	}
}
