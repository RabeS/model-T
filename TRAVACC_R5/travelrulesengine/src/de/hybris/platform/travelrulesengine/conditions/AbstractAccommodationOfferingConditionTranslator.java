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
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import de.hybris.platform.travelrulesengine.rao.AccommodationOfferingRAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * The type Abstract accommodation offering condition translator.
 */
public abstract class AbstractAccommodationOfferingConditionTranslator implements RuleConditionTranslator
{
	private static final String ACCOMMODATION_OFFERINGS_PARAMETER = "accommodationOfferings";
	private static final String ACCOMMODATION_OFFERING_ATTRIBUTE = "code";

	/**
	 * Translate rule ir condition.
	 *
	 * @param context
	 * 		the context
	 * @param condition
	 * 		the condition
	 * @param contextRAOVariable
	 * 		the context rao variable
	 * @return the rule ir condition
	 */
	protected RuleIrCondition translate(final RuleCompilerContext context, final RuleConditionData condition,
			final String contextRAOVariable)
	{
		final String accommodationOfferingRAOVariable = context.generateVariable(AccommodationOfferingRAO.class);

		final RuleParameterData accommodationOfferingsParameter = condition.getParameters().get(ACCOMMODATION_OFFERINGS_PARAMETER);
		final List<String> accommodationOfferings = accommodationOfferingsParameter.getValue();


		final RuleIrGroupCondition irAccommodationOfferingCondition = new RuleIrGroupCondition();
		irAccommodationOfferingCondition.setOperator(RuleIrGroupOperator.AND);
		irAccommodationOfferingCondition.setChildren(new ArrayList<>());

		final RuleIrTypeCondition irAccommodationReservationCondition = new RuleIrTypeCondition();
		irAccommodationReservationCondition.setVariable(contextRAOVariable);

		irAccommodationOfferingCondition.getChildren().add(irAccommodationReservationCondition);

		//ACCOMMODATION OFFERINGS
		createGroupAccommodationOfferingOrCondition(accommodationOfferings, ACCOMMODATION_OFFERING_ATTRIBUTE,
				accommodationOfferingRAOVariable, irAccommodationOfferingCondition, RuleIrAttributeOperator.CONTAINS);

		return irAccommodationOfferingCondition;
	}


	/**
	 * Create group accommodation offering or condition.
	 *
	 * @param accommodationOfferings
	 * 		the accommodation offerings
	 * @param attribute
	 * 		the attribute
	 * @param raoVariable
	 * 		the rao variable
	 * @param irAccommodationOfferingCondition
	 * 		the ir accommodation offering condition
	 * @param attributeOperator
	 * 		the attribute operator
	 */
	protected void createGroupAccommodationOfferingOrCondition(final List<String> accommodationOfferings, final String attribute,
			final String raoVariable, final RuleIrGroupCondition irAccommodationOfferingCondition,
			final RuleIrAttributeOperator attributeOperator)
	{

		if (Objects.nonNull(accommodationOfferings))
		{
			final RuleIrGroupCondition irAccommodationOfferingWrapperCondition = new RuleIrGroupCondition();
			irAccommodationOfferingWrapperCondition.setOperator(RuleIrGroupOperator.OR);
			irAccommodationOfferingWrapperCondition.setChildren(new ArrayList<>());

			for (final String accommodationOffering : accommodationOfferings)
			{
				final RuleIrGroupCondition irGroupCondition = new RuleIrGroupCondition();
				irGroupCondition.setOperator(RuleIrGroupOperator.AND);
				irGroupCondition.setChildren(new ArrayList<>());

				final RuleIrAttributeCondition ruleIrAttributeCondition = new RuleIrAttributeCondition();
				ruleIrAttributeCondition.setVariable(raoVariable);
				ruleIrAttributeCondition.setAttribute(attribute);
				ruleIrAttributeCondition.setOperator(attributeOperator);
				ruleIrAttributeCondition.setValue(accommodationOffering);

				irGroupCondition.getChildren().add(ruleIrAttributeCondition);
				irAccommodationOfferingWrapperCondition.getChildren().add(irGroupCondition);
			}
			irAccommodationOfferingCondition.getChildren().add(irAccommodationOfferingWrapperCondition);
		}
	}
}
