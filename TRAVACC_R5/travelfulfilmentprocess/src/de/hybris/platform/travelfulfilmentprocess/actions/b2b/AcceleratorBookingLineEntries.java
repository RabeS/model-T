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

import de.hybris.platform.b2b.process.approval.actions.SetBookingLineEntries;
import de.hybris.platform.b2b.process.approval.model.B2BApprovalProcessModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;
import de.hybris.platform.task.RetryLaterException;


/**
 * The AcceleratorBookingLineEntries.
 */
public class AcceleratorBookingLineEntries extends SetBookingLineEntries
{
	@Override
	public AbstractSimpleDecisionAction.Transition executeAction(final B2BApprovalProcessModel process) throws RetryLaterException
	{
		final OrderModel order = process.getOrder();
		getModelService().refresh(order);

		if (order.getPaymentInfo() != null)
		{
			return AbstractSimpleDecisionAction.Transition.OK;
		}
		else
		{
			return super.executeAction(process);
		}
	}
}
