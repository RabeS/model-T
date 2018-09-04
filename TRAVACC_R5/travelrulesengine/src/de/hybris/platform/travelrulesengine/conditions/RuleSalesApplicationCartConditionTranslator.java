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

package de.hybris.platform.travelrulesengine.conditions;

import de.hybris.platform.ruleengineservices.compiler.RuleCompilerContext;
import de.hybris.platform.ruleengineservices.compiler.RuleIrAttributeCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrAttributeOperator;
import de.hybris.platform.ruleengineservices.compiler.RuleIrCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrGroupCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrGroupOperator;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionData;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * Below class add sales application condition to the Group Condition
 */
public class RuleSalesApplicationCartConditionTranslator extends AbstractSalesApplicationConditionTranslator
{
	private static final String CART_RAO_BOOKING_JOURNEY_TYPE_ATTRIBUTE = "bookingJourneyType";
	private List<String> notAllowedBookingJourneyTypes;

	@Override
	public RuleIrCondition translate(final RuleCompilerContext context, final RuleConditionData condition,
			final RuleConditionDefinitionData paramRuleConditionDefinitionData)
	{
		final String cartRAOVariable = context.generateVariable(CartRAO.class);

		//		Wrapper
		final RuleIrGroupCondition irSalesApplicationWrapperCondition = new RuleIrGroupCondition();
		irSalesApplicationWrapperCondition.setOperator(RuleIrGroupOperator.AND);
		irSalesApplicationWrapperCondition.setChildren(new ArrayList<>());

		final RuleIrGroupCondition irSalesApplicationCondition = (RuleIrGroupCondition) super
				.translate(context, condition, paramRuleConditionDefinitionData, cartRAOVariable);

		//		BookingJourneyTypes condition
		final RuleIrGroupCondition irBookingJourneyTypesGroupCondition = new RuleIrGroupCondition();
		irBookingJourneyTypesGroupCondition.setOperator(RuleIrGroupOperator.AND);
		irBookingJourneyTypesGroupCondition.setChildren(new ArrayList<>());

		final RuleIrAttributeCondition irBookingJourneyTypesCondition = new RuleIrAttributeCondition();
		irBookingJourneyTypesCondition.setVariable(cartRAOVariable);
		irBookingJourneyTypesCondition.setAttribute(CART_RAO_BOOKING_JOURNEY_TYPE_ATTRIBUTE);
		irBookingJourneyTypesCondition.setOperator(RuleIrAttributeOperator.NOT_IN);
		irBookingJourneyTypesCondition.setValue(getNotAllowedBookingJourneyTypes());

		irBookingJourneyTypesGroupCondition.getChildren().add(irBookingJourneyTypesCondition);

		irSalesApplicationWrapperCondition.getChildren().add(irBookingJourneyTypesGroupCondition);
		irSalesApplicationWrapperCondition.getChildren().add(irSalesApplicationCondition);

		return irSalesApplicationWrapperCondition;
	}


	/**
	 * Gets not allowed booking journey types.
	 *
	 * @return the not allowed booking journey types
	 */
	protected List<String> getNotAllowedBookingJourneyTypes()
	{
		return notAllowedBookingJourneyTypes;
	}

	/**
	 * Sets not allowed booking journey types.
	 *
	 * @param notAllowedBookingJourneyTypes
	 * 		the not allowed booking journey types
	 */
	@Required
	public void setNotAllowedBookingJourneyTypes(final List<String> notAllowedBookingJourneyTypes)
	{
		this.notAllowedBookingJourneyTypes = notAllowedBookingJourneyTypes;
	}

}
