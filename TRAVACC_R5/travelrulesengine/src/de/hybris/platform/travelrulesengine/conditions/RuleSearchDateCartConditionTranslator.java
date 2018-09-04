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

import de.hybris.platform.ruledefinitions.AmountOperator;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerContext;
import de.hybris.platform.ruleengineservices.compiler.RuleConditionTranslator;
import de.hybris.platform.ruleengineservices.compiler.RuleIrAttributeCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrAttributeOperator;
import de.hybris.platform.ruleengineservices.compiler.RuleIrCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrGroupCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrGroupOperator;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import de.hybris.platform.travelrulesengine.utils.TravelRuleUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * The type Rule search date cart condition translator.
 */
public class RuleSearchDateCartConditionTranslator implements RuleConditionTranslator
{
	private static final String CART_RAO_SEARCH_DATE_ATTRIBUTE = "searchDate";
	private static final String CART_RAO_BOOKING_JOURNEY_TYPE_ATTRIBUTE = "bookingJourneyType";

	private static final String OPERATOR_PARAMETER = "operator";
	private static final String DATE_PARAMETER = "date";

	private List<String> notAllowedBookingJourneyTypes;

	@Override
	public RuleIrCondition translate(final RuleCompilerContext context, final RuleConditionData condition,
			final RuleConditionDefinitionData ruleConditionDefinitionData)
	{
		final String cartRAOVariable = context.generateVariable(CartRAO.class);
		final RuleParameterData operatorParameter = condition.getParameters().get(OPERATOR_PARAMETER);
		final RuleParameterData operatorDate = condition.getParameters().get(DATE_PARAMETER);

		final AmountOperator operator = operatorParameter.getValue();
		final Date date = TravelRuleUtils.setDateToMidnight(operatorDate.getValue());

		//		Wrapper

		final RuleIrGroupCondition irDateWrapperCondition = new RuleIrGroupCondition();
		irDateWrapperCondition.setOperator(RuleIrGroupOperator.AND);
		irDateWrapperCondition.setChildren(new ArrayList<>());

		//		SearchDate condition

		final RuleIrGroupCondition irDateGroupCondition = new RuleIrGroupCondition();
		irDateGroupCondition.setOperator(RuleIrGroupOperator.AND);
		irDateGroupCondition.setChildren(new ArrayList<>());

		final RuleIrAttributeCondition irDateCondition = new RuleIrAttributeCondition();
		irDateCondition.setVariable(cartRAOVariable);
		irDateCondition.setAttribute(CART_RAO_SEARCH_DATE_ATTRIBUTE);
		irDateCondition.setOperator(RuleIrAttributeOperator.valueOf(operator.name()));
		irDateCondition.setValue(date);

		irDateGroupCondition.getChildren().add(irDateCondition);

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

		//		Wrapper

		irDateWrapperCondition.getChildren().add(irBookingJourneyTypesGroupCondition);
		irDateWrapperCondition.getChildren().add(irDateGroupCondition);

		return irDateWrapperCondition;
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
