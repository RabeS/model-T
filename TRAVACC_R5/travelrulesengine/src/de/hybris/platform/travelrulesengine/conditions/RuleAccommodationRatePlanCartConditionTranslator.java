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
import de.hybris.platform.ruleengineservices.compiler.RuleConditionTranslator;
import de.hybris.platform.ruleengineservices.compiler.RuleIrAttributeCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrAttributeOperator;
import de.hybris.platform.ruleengineservices.compiler.RuleIrCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrGroupCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrGroupOperator;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import de.hybris.platform.travelrulesengine.rao.AccommodationRAO;

import java.util.ArrayList;
import java.util.List;


/**
 * The type Rule accommodation rate plan cart condition translator.
 */
public class RuleAccommodationRatePlanCartConditionTranslator implements RuleConditionTranslator
{
	private static final String ACCOMMODATION_RATEPLAN_PARAMETER = "ratePlans";
	private static final String ACCOMMODATION_RATEPLAN_ATTRIBUTE = "ratePlan";

	@Override
	public RuleIrCondition translate(final RuleCompilerContext context, final RuleConditionData condition,
			final RuleConditionDefinitionData ruleConditionDefinitionData)
	{
		final String accommodationRAOVariable = context.generateVariable(AccommodationRAO.class);

		final RuleParameterData ratePlansParameter = condition.getParameters().get(ACCOMMODATION_RATEPLAN_PARAMETER);

		final List<String> ratePlans = ratePlansParameter.getValue();

		final RuleIrGroupCondition irRatePlanOrWrapperCondition = new RuleIrGroupCondition();
		irRatePlanOrWrapperCondition.setOperator(RuleIrGroupOperator.OR);
		irRatePlanOrWrapperCondition.setChildren(new ArrayList<>());

		for (final String ratePlan : ratePlans)
		{
			final RuleIrGroupCondition irRatePlanWrapperCondition = new RuleIrGroupCondition();
			irRatePlanWrapperCondition.setOperator(RuleIrGroupOperator.AND);
			irRatePlanWrapperCondition.setChildren(new ArrayList<>());

			final RuleIrAttributeCondition irRatePlanCondition = new RuleIrAttributeCondition();
			irRatePlanCondition.setVariable(accommodationRAOVariable);
			irRatePlanCondition.setAttribute(ACCOMMODATION_RATEPLAN_ATTRIBUTE);
			irRatePlanCondition.setOperator(RuleIrAttributeOperator.EQUAL);
			irRatePlanCondition.setValue(ratePlan);

			irRatePlanWrapperCondition.getChildren().add(irRatePlanCondition);
			irRatePlanOrWrapperCondition.getChildren().add(irRatePlanWrapperCondition);
		}
		return irRatePlanOrWrapperCondition;
	}
}
