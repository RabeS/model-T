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
import de.hybris.platform.ruleengineservices.compiler.RuleConditionTranslator;
import de.hybris.platform.ruleengineservices.compiler.RuleIrAttributeCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrAttributeOperator;
import de.hybris.platform.ruleengineservices.compiler.RuleIrGroupCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrGroupOperator;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import de.hybris.platform.travelrulesengine.utils.TravelRuleUtils;

import java.util.ArrayList;
import java.util.Date;


/**
 * The type Abstract stay dates condition translator.
 */
public abstract class AbstractStayDatesConditionTranslator implements RuleConditionTranslator
{
	private static final String CHECK_IN_DATE_ATTRIBUTE = "checkInDate";
	private static final String CHECK_OUT_DATE_ATTRIBUTE = "checkOutDate";

	private static final String OPERATOR_PARAMETER = "operator";
	private static final String STAY_DATE_PARAMETER = "date";

	/**
	 * Translate rule ir condition.
	 *
	 * @param condition
	 * 		the condition
	 * @param contextRAOVariable
	 * 		the context rao variable
	 * @return the rule ir condition
	 */
	protected RuleIrGroupCondition translate(final RuleConditionData condition, final String contextRAOVariable)
	{
		final RuleParameterData operatorParameter = condition.getParameters().get(OPERATOR_PARAMETER);
		final RuleParameterData operatorDate = condition.getParameters().get(STAY_DATE_PARAMETER);

		final AmountOperator operator = operatorParameter.getValue();
		final Date date = TravelRuleUtils.setDateToMidnight(operatorDate.getValue());

		// STAY DATES GROUP CONDITION
		final RuleIrGroupCondition irStayDatesGroupCondition = new RuleIrGroupCondition();
		irStayDatesGroupCondition.setOperator(RuleIrGroupOperator.AND);
		irStayDatesGroupCondition.setChildren(new ArrayList<>());

		// ACCOMMODATION CHECKIN DATE
		final RuleIrAttributeCondition irCheckInDateCondition = new RuleIrAttributeCondition();
		irCheckInDateCondition.setVariable(contextRAOVariable);
		irCheckInDateCondition.setAttribute(CHECK_IN_DATE_ATTRIBUTE);
		irCheckInDateCondition.setOperator(RuleIrAttributeOperator.valueOf(operator.name()));
		irCheckInDateCondition.setValue(date);

		// ACCOMMODATION CHECKOUT DATE
		final RuleIrAttributeCondition irCheckOutDateCondition = new RuleIrAttributeCondition();
		irCheckOutDateCondition.setVariable(contextRAOVariable);
		irCheckOutDateCondition.setAttribute(CHECK_OUT_DATE_ATTRIBUTE);
		irCheckOutDateCondition.setOperator(RuleIrAttributeOperator.valueOf(operator.name()));
		irCheckOutDateCondition.setValue(date);

		irStayDatesGroupCondition.getChildren().add(irCheckInDateCondition);
		irStayDatesGroupCondition.getChildren().add(irCheckOutDateCondition);

		return irStayDatesGroupCondition;
	}

}
