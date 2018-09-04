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

import de.hybris.platform.ruleengineservices.compiler.RuleConditionTranslator;
import de.hybris.platform.ruleengineservices.compiler.RuleIrAttributeCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrAttributeOperator;
import de.hybris.platform.ruleengineservices.compiler.RuleIrGroupCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrGroupOperator;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;

import java.util.ArrayList;
import java.util.List;


public abstract class AbstractDaysOfWeekConditionTranslator implements RuleConditionTranslator
{
	private static final String DAYS_PARAMETER = "days";
	private static final String DAYS_OF_WEEK_ATTRIBUTE = "daysOfWeek";

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
		final RuleParameterData daysParameter = condition.getParameters().get(DAYS_PARAMETER);
		final List<String> days = daysParameter.getValue();

		final RuleIrGroupCondition irDaysOfWeekGroupCondition = new RuleIrGroupCondition();
		irDaysOfWeekGroupCondition.setOperator(RuleIrGroupOperator.AND);
		irDaysOfWeekGroupCondition.setChildren(new ArrayList<>());

		final RuleIrGroupCondition irContainsDayCondition = new RuleIrGroupCondition();
		irContainsDayCondition.setOperator(RuleIrGroupOperator.OR);
		irContainsDayCondition.setChildren(new ArrayList<>());

		for (final String day : days)
		{
			final RuleIrGroupCondition irDayCondition = new RuleIrGroupCondition();
			irDayCondition.setOperator(RuleIrGroupOperator.AND);
			irDayCondition.setChildren(new ArrayList<>());

			final RuleIrAttributeCondition ruleIrAttributeCondition = new RuleIrAttributeCondition();
			ruleIrAttributeCondition.setVariable(contextRAOVariable);
			ruleIrAttributeCondition.setAttribute(DAYS_OF_WEEK_ATTRIBUTE);
			ruleIrAttributeCondition.setOperator(RuleIrAttributeOperator.CONTAINS);
			ruleIrAttributeCondition.setValue(day);

			irDayCondition.getChildren().add(ruleIrAttributeCondition);
			irContainsDayCondition.getChildren().add(irDayCondition);
		}
		irDaysOfWeekGroupCondition.getChildren().add(irContainsDayCondition);
		return irDaysOfWeekGroupCondition;
	}
}
