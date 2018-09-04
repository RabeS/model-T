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
package de.hybris.platform.travelb2bfacades.reservation.handlers;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.travelb2bfacades.data.B2BReservationData;


/**
 * The interface for B2B Reservation Handler.
 */
public interface B2BReservationHandler
{
	/**
	 * Handle method
	 *
	 * @param abstractOrderModel
	 * 		the abstract order model
	 * @param b2bReservationData
	 * 		the B2B reservation data
	 */
	void handle(AbstractOrderModel abstractOrderModel, B2BReservationData b2bReservationData);
}
