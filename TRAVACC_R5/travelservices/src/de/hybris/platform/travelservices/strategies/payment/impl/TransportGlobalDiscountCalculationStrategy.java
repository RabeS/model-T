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
package de.hybris.platform.travelservices.strategies.payment.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.promotionengineservices.model.PromotionSourceRuleModel;
import de.hybris.platform.travelrulesengine.model.TransportPromotionSourceRuleModel;
import de.hybris.platform.travelservices.strategies.payment.GlobalDiscountByEntryTypeCalculationStrategy;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;


/**
 * Concrete implementation of {@link GlobalDiscountByEntryTypeCalculationStrategy}
 */
public class TransportGlobalDiscountCalculationStrategy extends AbstractGlobalDiscountCalculationStrategy
		implements GlobalDiscountByEntryTypeCalculationStrategy
{
	@Override
	public BigDecimal getGlobalDiscount(final AbstractOrderModel abstractOrder, final List<AbstractOrderEntryModel> entries)
	{
		BigDecimal discount = BigDecimal.ZERO;
		if (CollectionUtils.isEmpty(abstractOrder.getAllPromotionResults()))
		{
			return discount;
		}
		discount = discount.add(getGlobalDiscountForPromotionType(abstractOrder, TransportPromotionSourceRuleModel.class));
		discount = discount.add(getGlobalDiscountForPromotionType(abstractOrder,PromotionSourceRuleModel.class));
		return discount;
	}


}
