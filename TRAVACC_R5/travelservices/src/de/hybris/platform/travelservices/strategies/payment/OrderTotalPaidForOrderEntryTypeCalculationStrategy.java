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

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.travelservices.enums.OrderEntryType;

import java.math.BigDecimal;


/**
 * Interface for strategies calculating the total amount paid given an order and an OrderEntryType.
 */
public interface OrderTotalPaidForOrderEntryTypeCalculationStrategy
{

	/**
	 * Calculates the total paid according with the transactions against the given order and an orderEntryType
	 *
	 * @param abstractOrder
	 * @param orderEntryType
	 * @return
	 */
	BigDecimal calculate(AbstractOrderModel abstractOrder, OrderEntryType orderEntryType);
}
