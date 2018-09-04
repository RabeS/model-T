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
import de.hybris.platform.travelrulesengine.rao.AccommodationOfferingRAO;
import de.hybris.platform.travelrulesengine.rao.AccommodationRAO;
import de.hybris.platform.travelrulesengine.rao.AccommodationReservationRAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * The type Rule accommodation rate plan acc res condition translator.
 */
public class RuleAccommodationRatePlanReservationConditionTranslator implements RuleConditionTranslator
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


		final RuleIrGroupCondition irRatePlanCondition = new RuleIrGroupCondition();
		irRatePlanCondition.setOperator(RuleIrGroupOperator.AND);
		irRatePlanCondition.setChildren(new ArrayList<>());

		final String accommodationReservationRAOVariable = context.generateVariable(AccommodationReservationRAO.class);
		final RuleIrTypeCondition irAccommodationReservationCondition = new RuleIrTypeCondition();
		irAccommodationReservationCondition.setVariable(accommodationReservationRAOVariable);

		irRatePlanCondition.getChildren().add(irAccommodationReservationCondition);

		final RuleIrGroupCondition irRatePlanOrWrapperCondition = new RuleIrGroupCondition();
		irRatePlanOrWrapperCondition.setOperator(RuleIrGroupOperator.OR);
		irRatePlanOrWrapperCondition.setChildren(new ArrayList<>());

		for (final String ratePlan : ratePlans)
		{
			if (Objects.nonNull(ratePlan))
			{
				final RuleIrGroupCondition irRatePlanWrapperCondition = new RuleIrGroupCondition();
				irRatePlanWrapperCondition.setOperator(RuleIrGroupOperator.AND);
				irRatePlanWrapperCondition.setChildren(new ArrayList<>());

				final RuleIrAttributeCondition ruleIrAttributeCondition = new RuleIrAttributeCondition();
				ruleIrAttributeCondition.setVariable(accommodationRAOVariable);
				ruleIrAttributeCondition.setAttribute(ACCOMMODATION_RATEPLAN_ATTRIBUTE);
				ruleIrAttributeCondition.setOperator(RuleIrAttributeOperator.CONTAINS);
				ruleIrAttributeCondition.setValue(ratePlan);

				irRatePlanWrapperCondition.getChildren().add(ruleIrAttributeCondition);
				irRatePlanOrWrapperCondition.getChildren().add(irRatePlanWrapperCondition);
			}
		}
		irRatePlanCondition.getChildren().add(irRatePlanOrWrapperCondition);
		return irRatePlanCondition;
	}
}
