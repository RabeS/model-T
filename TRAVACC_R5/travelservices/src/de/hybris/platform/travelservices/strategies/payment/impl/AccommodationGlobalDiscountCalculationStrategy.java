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
import de.hybris.platform.travelrulesengine.model.AccommodationPromotionSourceRuleModel;
import de.hybris.platform.travelservices.enums.OrderEntryType;
import de.hybris.platform.travelservices.model.AbstractOrderEntryGroupModel;
import de.hybris.platform.travelservices.model.order.AccommodationOrderEntryGroupModel;
import de.hybris.platform.travelservices.strategies.payment.GlobalDiscountByEntryTypeCalculationStrategy;

import java.math.BigDecimal;
import java.util.List;


/**
 * Concrete implementation of {@link GlobalDiscountByEntryTypeCalculationStrategy}
 */
public class AccommodationGlobalDiscountCalculationStrategy extends AbstractGlobalDiscountCalculationStrategy
		implements GlobalDiscountByEntryTypeCalculationStrategy
{
	@Override
	public BigDecimal getGlobalDiscount(final AbstractOrderModel abstractOrder, final List<AbstractOrderEntryModel> entries)
	{
		BigDecimal discount = BigDecimal.ZERO;
		final AbstractOrderEntryGroupModel entryGroup = entries.stream().findFirst().get().getEntryGroup();
		if (!AccommodationOrderEntryGroupModel.class.isInstance(entryGroup))
		{
			return discount;
		}
		if (AccommodationOrderEntryGroupModel.class.cast(entryGroup).getRoomStayRefNumber() == 0)
		{
			discount = discount.add(getGlobalDiscountForPromotionType(abstractOrder, AccommodationPromotionSourceRuleModel.class));
			if (abstractOrder.getEntries().stream().allMatch(entry -> OrderEntryType.ACCOMMODATION.equals(entry.getType())))
			{
				discount = discount.add(getGlobalDiscountForPromotionType(abstractOrder, PromotionSourceRuleModel.class));
			}
		}
		return discount;
	}
}
