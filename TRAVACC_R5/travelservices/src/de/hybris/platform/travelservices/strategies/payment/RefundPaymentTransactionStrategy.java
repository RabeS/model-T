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

package de.hybris.platform.travelservices.strategies.payment;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.travelservices.enums.OrderEntryType;

import java.util.List;


/**
 * Strategy interface to handle refund for a specific journey
 */
public interface RefundPaymentTransactionStrategy
{
	/**
	 * This method triggers the creation of refund payment transaction entries according with the specific journey it
	 * refers to
	 *
	 * @param cartModel
	 * @param entryType
	 * @param entries
	 * @param totalToRefund
	 * @return
	 */
	void handleRefund(AbstractOrderModel abstractOrder, OrderEntryType entryType, List<AbstractOrderEntryModel> entries);
}
