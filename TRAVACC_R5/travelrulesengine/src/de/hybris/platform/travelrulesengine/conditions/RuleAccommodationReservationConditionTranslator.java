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
import de.hybris.platform.travelrulesengine.rao.AccommodationRAO;
import de.hybris.platform.travelrulesengine.rao.AccommodationReservationRAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * The type Rule accommodation reservation condition translator.
 */
public class RuleAccommodationReservationConditionTranslator implements RuleConditionTranslator
{
	private static final String ACCOMMODATIONS_PARAMETER = "accommodations";
	private static final String ACCOMMODATION_ATTRIBUTE = "code";

	@Override
	public RuleIrCondition translate(final RuleCompilerContext context, final RuleConditionData condition,
			final RuleConditionDefinitionData ruleConditionDefinitionData)
	{
		final String accommodationRAOVariable = context.generateVariable(AccommodationRAO.class);
		final RuleParameterData roomType = condition.getParameters().get(ACCOMMODATIONS_PARAMETER);
		final List<String> types = roomType.getValue();

		final RuleIrGroupCondition irTypeCondition = new RuleIrGroupCondition();
		irTypeCondition.setOperator(RuleIrGroupOperator.AND);
		irTypeCondition.setChildren(new ArrayList<>());

		final String accommodationReservationRAOVariable = context.generateVariable(AccommodationReservationRAO.class);
		final RuleIrTypeCondition irAccommodationReservationCondition = new RuleIrTypeCondition();
		irAccommodationReservationCondition.setVariable(accommodationReservationRAOVariable);

		irTypeCondition.getChildren().add(irAccommodationReservationCondition);
		createGroupTypeOrCondition(types, ACCOMMODATION_ATTRIBUTE, accommodationRAOVariable, irTypeCondition,
				RuleIrAttributeOperator.CONTAINS);

		return irTypeCondition;
	}

	/**
	 * Create group type or condition.
	 *
	 * @param types
	 * 		the types
	 * @param attribute
	 * 		the attribute
	 * @param raoVariable
	 * 		the rao variable
	 * @param irTypeCondition
	 * 		the ir type condition
	 * @param attributeOperator
	 * 		the attribute operator
	 */
	protected void createGroupTypeOrCondition(final List<String> types, final String attribute, final String raoVariable,
			final RuleIrGroupCondition irTypeCondition, final RuleIrAttributeOperator attributeOperator)
	{
		if (Objects.nonNull(types))
		{
			final RuleIrGroupCondition irAccommodationOfferingTypeWrapperCondition = new RuleIrGroupCondition();
			irAccommodationOfferingTypeWrapperCondition.setOperator(RuleIrGroupOperator.OR);
			irAccommodationOfferingTypeWrapperCondition.setChildren(new ArrayList<>());

			for (final String type : types)
			{
				final RuleIrGroupCondition irAccommodationOfferingTypeCondition = new RuleIrGroupCondition();
				irAccommodationOfferingTypeCondition.setOperator(RuleIrGroupOperator.AND);
				irAccommodationOfferingTypeCondition.setChildren(new ArrayList<>());

				final RuleIrAttributeCondition ruleIrAttributeCondition = new RuleIrAttributeCondition();
				ruleIrAttributeCondition.setVariable(raoVariable);
				ruleIrAttributeCondition.setAttribute(attribute);
				ruleIrAttributeCondition.setOperator(attributeOperator);
				ruleIrAttributeCondition.setValue(type);

				irAccommodationOfferingTypeCondition.getChildren().add(ruleIrAttributeCondition);
				irAccommodationOfferingTypeWrapperCondition.getChildren().add(irAccommodationOfferingTypeCondition);
			}
			irTypeCondition.getChildren().add(irAccommodationOfferingTypeWrapperCondition);
		}
	}
}
