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

import java.util.ArrayList;


/**
 * The type Abstract stay duration condition translator.
 */
public abstract class AbstractStayDurationConditionTranslator implements RuleConditionTranslator
{
	private static final String DAYS_PARAMETER = "days";
	private static final String OPERATOR_PARAMETER = "operator";

	private static final String STAY_DURATION_ATTRIBUTE = "stayDuration";

	/**
	 * Translate rule ir condition.
	 *
	 * @param condition
	 * 		the condition
	 * @param contextRAOVariable
	 * 		the context rao variable
	 * @return the rule ir condition
	 */
	protected RuleIrGroupCondition translate(final RuleConditionData condition,  final String contextRAOVariable)
	{
		final RuleParameterData daysParameter = condition.getParameters().get(DAYS_PARAMETER);
		final RuleParameterData operatorParameter = condition.getParameters().get(OPERATOR_PARAMETER);

		final Integer days = daysParameter.getValue();
		final AmountOperator operator = operatorParameter.getValue();

		final RuleIrGroupCondition irStayDurationGroupCondition = new RuleIrGroupCondition();
		irStayDurationGroupCondition.setOperator(RuleIrGroupOperator.AND);
		irStayDurationGroupCondition.setChildren(new ArrayList<>());

		final RuleIrAttributeCondition irStayDurationCondition = new RuleIrAttributeCondition();
		irStayDurationCondition.setVariable(contextRAOVariable);
		irStayDurationCondition.setAttribute(STAY_DURATION_ATTRIBUTE);
		irStayDurationCondition.setOperator(RuleIrAttributeOperator.valueOf(operator.name()));
		irStayDurationCondition.setValue(days);

		irStayDurationGroupCondition.getChildren().add(irStayDurationCondition);

		return irStayDurationGroupCondition;
	}
}
