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
import de.hybris.platform.ruleengineservices.compiler.RuleIrGroupCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrGroupOperator;
import de.hybris.platform.ruleengineservices.compiler.RuleIrTypeCondition;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import de.hybris.platform.travelrulesengine.rao.AccommodationOfferingRAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * The type Abstract accommodation offering type condition translator.
 */
public abstract class AbstractAccommodationOfferingTypeConditionTranslator implements RuleConditionTranslator
{
	private static final String ACCOMMODATION_OFFERING_TYPE_PARAMETER = "accommodationOfferingType";
	private static final String ACCOMMODATION_TYPE_ATTRIBUTE = "accommodationOfferingTypes";

	/**
	 * Translate rule ir group condition.
	 *
	 * @param context
	 * 		the context
	 * @param condition
	 * 		the condition
	 * @param contextRAOVariable
	 * 		the context rao variable
	 * @return the rule ir group condition
	 */
	protected RuleIrGroupCondition translate(final RuleCompilerContext context, final RuleConditionData condition,
			final String contextRAOVariable)
	{

		final String accommodationOfferingRAO = context.generateVariable(AccommodationOfferingRAO.class);
		final RuleParameterData accommodationOfferingType = condition.getParameters().get(ACCOMMODATION_OFFERING_TYPE_PARAMETER);
		final List<String> types = accommodationOfferingType.getValue();

		final RuleIrGroupCondition irTypeCondition = new RuleIrGroupCondition();
		irTypeCondition.setOperator(RuleIrGroupOperator.AND);
		irTypeCondition.setChildren(new ArrayList<>());

		final RuleIrTypeCondition contextRAOVariableCondition = new RuleIrTypeCondition();
		contextRAOVariableCondition.setVariable(contextRAOVariable);

		createGroupTypeOrCondition(types, ACCOMMODATION_TYPE_ATTRIBUTE, accommodationOfferingRAO, irTypeCondition,
				RuleIrAttributeOperator.CONTAINS);

		irTypeCondition.getChildren().add(contextRAOVariableCondition);

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
