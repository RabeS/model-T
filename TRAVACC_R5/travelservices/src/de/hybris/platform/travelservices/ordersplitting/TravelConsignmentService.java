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

package de.hybris.platform.travelservices.ordersplitting;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.travelservices.model.user.TravellerModel;


/**
 * The interface Travel consignment service.
 */
public interface TravelConsignmentService
{
	/**
	 * Gets consignment.
	 *
	 * @param warehouse
	 * 		the warehouse
	 * @param order
	 * 		the order
	 * @param traveller
	 * 		the traveller
	 * @return the consignment
	 */
	ConsignmentModel getConsignment(WarehouseModel warehouse, OrderModel order, TravellerModel traveller);
}
