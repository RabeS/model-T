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

import java.util.List;

import de.hybris.platform.ruledefinitions.CollectionOperator;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerContext;
import de.hybris.platform.ruleengineservices.compiler.RuleIrCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrGroupCondition;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionData;
import de.hybris.platform.travelrulesengine.rao.SearchParamsRAO;


/**
 * The type Rule Customers condition translator.
 */
public class RuleCustomersConditionTranslator extends AbstractRuleCustomersConditionTranslator
{
	@Override
	public RuleIrCondition translate(final RuleCompilerContext context, final RuleConditionData condition,
			final RuleConditionDefinitionData conditionDefinition)
	{
		return super.translate(context, condition, conditionDefinition);
	}

	@Override
	protected void addTargetCustomersConditions(final RuleCompilerContext context, final CollectionOperator customerGroupsOperator,
			final List<String> customerGroups, final List<String> customers, final RuleIrGroupCondition irTargetCustomersCondition)
	{
		final String searchParamsVariable = context.generateVariable(SearchParamsRAO.class);
		super.addTargetCustomersConditions(context, customerGroupsOperator, customerGroups, customers, irTargetCustomersCondition, searchParamsVariable);
	}
}
