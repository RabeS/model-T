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

import de.hybris.platform.orderprocessing.events.PickupConfirmationEvent;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.travelfulfilmentprocess.constants.TravelfulfilmentprocessConstants;

import org.springframework.beans.factory.annotation.Required;


/**
 * Listener for pickup confirmation events.
 */
public class PickupConfirmationEventListener extends AbstractEventListener<PickupConfirmationEvent>
{

	private BusinessProcessService businessProcessService;

	@Override
	protected void onEvent(final PickupConfirmationEvent pickupConfirmationEvent)
	{
		final ConsignmentModel consignmentModel = pickupConfirmationEvent.getProcess().getConsignment();
		for (final ConsignmentProcessModel process : consignmentModel.getConsignmentProcesses())
		{
			getBusinessProcessService().triggerEvent(
					process.getCode() + "_" + TravelfulfilmentprocessConstants.CONSIGNMENT_PICKUP);
		}
	}

	/**
	 * Gets business process service.
	 *
	 * @return the business process service
	 */
	protected BusinessProcessService getBusinessProcessService()
	{
		return businessProcessService;
	}

	/**
	 * Sets business process service.
	 *
	 * @param businessProcessService
	 * 		the business process service
	 */
	@Required
	public void setBusinessProcessService(final BusinessProcessService businessProcessService)
	{
		this.businessProcessService = businessProcessService;
	}
}
