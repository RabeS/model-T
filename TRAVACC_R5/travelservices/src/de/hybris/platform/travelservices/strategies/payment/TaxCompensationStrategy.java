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

import java.math.BigDecimal;
import java.util.List;


/**
 * Interface exposing methods to calculate tax compensation when order level discounts are applied on a net {@link AbstractOrderModel}
 * and distributed across order entries generating discrepancies on applied taxes
 */
public interface TaxCompensationStrategy
{
	/**
	 * Calculates tax compensation for a given collection of {@link AbstractOrderEntryModel}
	 * @param abstractOrder
	 * @param discountFraction
	 * @param entries
	 * @return
	 */
	BigDecimal calculateTaxCompensationForEntries(AbstractOrderModel abstractOrder, BigDecimal discountFraction, List<AbstractOrderEntryModel> entries);
}
