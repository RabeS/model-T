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

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.promotionengineservices.model.PromotionSourceRuleModel;
import de.hybris.platform.promotionengineservices.model.RuleBasedOrderAdjustTotalActionModel;
import de.hybris.platform.promotionengineservices.model.RuleBasedPromotionModel;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;


public abstract class AbstractGlobalDiscountCalculationStrategy<T extends PromotionSourceRuleModel>
{
	/**
	 * Calculates global discounts for a specific type of promotion
	 *
	 * @param abstractOrder
	 * @return
	 */
	protected BigDecimal getGlobalDiscountForPromotionType(final AbstractOrderModel abstractOrder, final Class<T> promotionType)
	{
		final BigDecimal discount = BigDecimal.ZERO;
		final List<PromotionResultModel> resultsForTypePromo = abstractOrder.getAllPromotionResults().stream()
				.filter(result -> RuleBasedPromotionModel.class.isInstance(result.getPromotion()))
				.filter(result -> isValidSourceRuleTypePromotionType(result.getPromotion(),promotionType))
				.collect(Collectors.toList());
		if (CollectionUtils.isEmpty(resultsForTypePromo))
		{
			return discount;
		}

		final List<RuleBasedOrderAdjustTotalActionModel> cartDiscountActions = resultsForTypePromo.stream()
				.flatMap(result -> result.getAllPromotionActions().stream())
				.filter(RuleBasedOrderAdjustTotalActionModel.class::isInstance)
				.map(RuleBasedOrderAdjustTotalActionModel.class::cast).collect(Collectors.toList());

		return discount.add(cartDiscountActions.stream()
				.map(action -> abstractOrder.getGlobalDiscountValues().stream()
						.filter(dv -> StringUtils.equals(dv.getCode(), action.getGuid())).findFirst().get())
				.map(dv -> BigDecimal.valueOf(dv.getAppliedValue()))
				.reduce(BigDecimal.ZERO, BigDecimal::add));
	}

	protected boolean isValidSourceRuleTypePromotionType(final AbstractPromotionModel promotion, final Class<T> promotionType)
	{
		final RuleBasedPromotionModel ruleBasedPromotion = RuleBasedPromotionModel.class.cast(promotion);
		final AbstractRuleModel sourceRule = ruleBasedPromotion.getRule().getSourceRule();
		return Objects.nonNull(sourceRule) && promotionType.equals(sourceRule.getClass());
	}
}
