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

import de.hybris.platform.ruledefinitions.CollectionOperator;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerContext;
import de.hybris.platform.ruleengineservices.compiler.RuleIrCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrGroupCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrTypeCondition;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionData;
import de.hybris.platform.travelrulesengine.rao.AccommodationAvailabilityRequestRAO;
import de.hybris.platform.travelrulesengine.rao.RoomStayRAO;

import java.util.List;

/**
 * This is a condition translator to translate day of the week condition for accommodationAvailabilityRequestRAOVariable and
 * roomStayRAOVariable
 */
public class RuleCustomersAccommodationAvailabilityConditionTranslator extends AbstractRuleCustomersConditionTranslator
{
	@Override
	public RuleIrCondition translate(final RuleCompilerContext context, final RuleConditionData condition,
			final RuleConditionDefinitionData conditionDefinition)
	{
		final RuleIrGroupCondition ruleIrGroupCondition = (RuleIrGroupCondition)
				super.translate(context, condition, conditionDefinition);

		final String roomStayRAOVariable = context.generateVariable(RoomStayRAO.class);
		final RuleIrTypeCondition irRoomStayRAOCondition = new RuleIrTypeCondition();
		irRoomStayRAOCondition.setVariable(roomStayRAOVariable);

		ruleIrGroupCondition.getChildren().add(irRoomStayRAOCondition);

		return ruleIrGroupCondition;
	}

	@Override
	protected void addTargetCustomersConditions(final RuleCompilerContext context, final CollectionOperator
			customerGroupsOperator,
			final List<String> customerGroups, final List<String> customers, final RuleIrGroupCondition irTargetCustomersCondition)
	{
		final String accommodationAvailabilityRequestRAOVariable = context
				.generateVariable(AccommodationAvailabilityRequestRAO.class);

		super.addTargetCustomersConditions(context, customerGroupsOperator, customerGroups, customers, irTargetCustomersCondition,
				accommodationAvailabilityRequestRAOVariable);
	}

}

