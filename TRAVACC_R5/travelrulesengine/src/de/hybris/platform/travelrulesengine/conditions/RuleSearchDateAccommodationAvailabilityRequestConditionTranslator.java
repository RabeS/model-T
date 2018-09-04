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
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import de.hybris.platform.travelrulesengine.rao.AccommodationAvailabilityRequestRAO;
import de.hybris.platform.travelrulesengine.utils.TravelRuleUtils;

import java.util.ArrayList;
import java.util.Date;


/**
 * The type Rule search date accommodation availability request condition translator.
 */
public class RuleSearchDateAccommodationAvailabilityRequestConditionTranslator implements RuleConditionTranslator
{
	private static final String SEARCH_DATE_ATTRIBUTE = "searchDate";
	private static final String OPERATOR_PARAMETER = "operator";
	private static final String DATE_PARAMETER = "date";

	@Override
	public RuleIrCondition translate(final RuleCompilerContext context, final RuleConditionData condition,
			final RuleConditionDefinitionData ruleConditionDefinitionData)
	{

		final String accommodationAvailabilityRequestRAOVariable = context
				.generateVariable(AccommodationAvailabilityRequestRAO.class);
		final RuleParameterData operatorParameter = condition.getParameters().get(OPERATOR_PARAMETER);
		final RuleParameterData operatorDate = condition.getParameters().get(DATE_PARAMETER);

		final AmountOperator operator = operatorParameter.getValue();
		final Date date = TravelRuleUtils.setDateToMidnight(operatorDate.getValue());

		final RuleIrGroupCondition irDateGroupCondition = new RuleIrGroupCondition();
		irDateGroupCondition.setOperator(RuleIrGroupOperator.AND);
		irDateGroupCondition.setChildren(new ArrayList<>());

		final RuleIrAttributeCondition irDateCondition = new RuleIrAttributeCondition();
		irDateCondition.setVariable(accommodationAvailabilityRequestRAOVariable);
		irDateCondition.setAttribute(SEARCH_DATE_ATTRIBUTE);
		irDateCondition.setOperator(RuleIrAttributeOperator.valueOf(operator.name()));
		irDateCondition.setValue(date);

		irDateGroupCondition.getChildren().add(irDateCondition);

		return irDateGroupCondition;
	}
}
