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
package de.hybris.platform.travelservices.strategies.payment.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.travelservices.strategies.payment.TaxCompensationStrategy;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.TaxValue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;


/**
 * Default implementation of {@link TaxCompensationStrategy}. It returns a fraction of the tax difference proportional with the
 * fraction of global discounts passed as an argument when the Net flag against {@link AbstractOrderModel} is true. It performs
 * static calculation, not relying on the passed list of {@link AbstractOrderEntryModel}.
 */
public class DefaultTaxCompensationStrategy implements TaxCompensationStrategy
{
	@Override
	public BigDecimal calculateTaxCompensationForEntries(final AbstractOrderModel abstractOrder, final BigDecimal discountFraction,
			final List<AbstractOrderEntryModel> entries)
	{
		if (!abstractOrder.getNet())
		{
			return BigDecimal.ZERO;
		}
		final Double taxOnEntries = abstractOrder.getEntries().stream().filter(AbstractOrderEntryModel::getActive)
				.flatMap(entry -> entry.getTaxValues().stream()).mapToDouble(
						TaxValue::getAppliedValue).sum();
		final Double totalTaxes = abstractOrder.getTotalTax();
		final BigDecimal taxToCompensate = BigDecimal.valueOf(Double.sum(taxOnEntries, -totalTaxes));
		if (taxToCompensate.compareTo(BigDecimal.ZERO) == 0)
		{
			return BigDecimal.ZERO;
		}
		final BigDecimal totalGlobalDiscount = BigDecimal
				.valueOf(abstractOrder.getGlobalDiscountValues().stream().mapToDouble(DiscountValue::getAppliedValue).sum());
		if (totalGlobalDiscount.compareTo(BigDecimal.ZERO) == 0)
		{
			return BigDecimal.ZERO;
		}
		final BigDecimal taxCompensation = discountFraction.divide(totalGlobalDiscount, 2, RoundingMode.HALF_UP)
				.multiply(taxToCompensate);

		return taxCompensation;
	}
}
