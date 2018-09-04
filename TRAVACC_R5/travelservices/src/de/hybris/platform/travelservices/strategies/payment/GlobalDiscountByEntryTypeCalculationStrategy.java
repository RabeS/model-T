/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 *
 *
 */
package de.hybris.platform.travelservices.strategies.payment;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;

import java.math.BigDecimal;
import java.util.List;


/**
 * Interface exposing methods to calculate a portion of global discount
 */
public interface GlobalDiscountByEntryTypeCalculationStrategy
{
	/**
	 * Return the portion of global discount relative to entries of a certain type
	 * @param entries
	 * @param abstractOrder
	 * @return
	 */
	BigDecimal getGlobalDiscount(AbstractOrderModel abstractOrder, List<AbstractOrderEntryModel> entries);
}
