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
import de.hybris.platform.ruleengineservices.compiler.RuleIrTypeCondition;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import de.hybris.platform.travelrulesengine.rao.AccommodationAvailabilityRequestRAO;
import de.hybris.platform.travelrulesengine.rao.RoomStayRAO;

import java.util.ArrayList;
import java.util.List;


/**
 * The type Rule rate plan accommodation room stay condition translator.
 */
public class RuleRatePlanAccommodationRoomStayConditionTranslator implements RuleConditionTranslator
{
	private static final String ACCOMMODATION_RATEPLAN_PARAMETER = "ratePlans";
	private static final String ACCOMMODATION_RATEPLAN_ATTRIBUTE = "ratePlans";

	@Override
	public RuleIrCondition translate(final RuleCompilerContext context, final RuleConditionData condition,
			final RuleConditionDefinitionData ruleConditionDefinitionData)
	{
		final String roomStayRAOVariable = context.generateVariable(RoomStayRAO.class);

		final RuleParameterData ratePlansParameter = condition.getParameters().get(ACCOMMODATION_RATEPLAN_PARAMETER);
		final List<String> ratePlans = ratePlansParameter.getValue();

		final RuleIrGroupCondition irRatePlanOrWrapperCondition = new RuleIrGroupCondition();
		irRatePlanOrWrapperCondition.setOperator(RuleIrGroupOperator.AND);
		irRatePlanOrWrapperCondition.setChildren(new ArrayList<>());

		final String accommodationAvailabilityRequestRAOVariable = context
				.generateVariable(AccommodationAvailabilityRequestRAO.class);
		final RuleIrTypeCondition iraccommodationAvailabilityRequestRAOCondition = new RuleIrTypeCondition();
		iraccommodationAvailabilityRequestRAOCondition.setVariable(accommodationAvailabilityRequestRAOVariable);

		irRatePlanOrWrapperCondition.getChildren().add(iraccommodationAvailabilityRequestRAOCondition);

		final RuleIrGroupCondition irContainsRatePlanCondition = new RuleIrGroupCondition();
		irContainsRatePlanCondition.setOperator(RuleIrGroupOperator.OR);
		irContainsRatePlanCondition.setChildren(new ArrayList<>());

		for (final String ratePlan : ratePlans)
		{
			final RuleIrGroupCondition irRatePlanWrapperCondition = new RuleIrGroupCondition();
			irRatePlanWrapperCondition.setOperator(RuleIrGroupOperator.AND);
			irRatePlanWrapperCondition.setChildren(new ArrayList<>());

			final RuleIrAttributeCondition ruleIrAttributeCondition = new RuleIrAttributeCondition();
			ruleIrAttributeCondition.setVariable(roomStayRAOVariable);
			ruleIrAttributeCondition.setAttribute(ACCOMMODATION_RATEPLAN_ATTRIBUTE);
			ruleIrAttributeCondition.setOperator(RuleIrAttributeOperator.CONTAINS);
			ruleIrAttributeCondition.setValue(ratePlan);

			irRatePlanWrapperCondition.getChildren().add(ruleIrAttributeCondition);
			irContainsRatePlanCondition.getChildren().add(irRatePlanWrapperCondition);
		}
		irRatePlanOrWrapperCondition.getChildren().add(irContainsRatePlanCondition);
		return irRatePlanOrWrapperCondition;
	}
}
