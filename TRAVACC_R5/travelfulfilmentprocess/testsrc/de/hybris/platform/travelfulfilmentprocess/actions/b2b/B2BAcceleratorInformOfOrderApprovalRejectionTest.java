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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;


/**
 * The type B2b accelerator inform of order approval rejection test.
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class B2BAcceleratorInformOfOrderApprovalRejectionTest
{
	@InjectMocks
	B2BAcceleratorInformOfOrderApprovalRejection b2BAcceleratorInformOfOrderApprovalRejection;

	@Test(expected = IllegalStateException.class)
	public void testExecuteActionWithException()
	{
		final B2BApprovalProcessModel b2BApprovalProcessModel = new B2BApprovalProcessModel();
		b2BAcceleratorInformOfOrderApprovalRejection.executeAction(b2BApprovalProcessModel);
	}

}
