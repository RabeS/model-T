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
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import de.hybris.platform.travelrulesengine.rao.AccommodationOfferingRAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * The type Rule accommodation category cart condition translator.
 */
public class RuleAccommodationCategoryCartConditionTranslator implements RuleConditionTranslator
{
	private static final String ACCOMMODATION_CATEGORIES_PARAMETER = "accommodationCategory";

	private static final String ACCOMMODATION_CATEGORIES_ATTRIBUTE = "accommodationCategories";

	@Override
	public RuleIrCondition translate(final RuleCompilerContext context, final RuleConditionData condition,
			final RuleConditionDefinitionData ruleConditionDefinitionData)
	{
		final String accommodationOfferingRAO = context.generateVariable(AccommodationOfferingRAO.class);

		final RuleParameterData categoryParameter = condition.getParameters().get(ACCOMMODATION_CATEGORIES_PARAMETER);
		final List<String> categories = categoryParameter.getValue();

		final String cartRAOVariable = context.generateVariable(CartRAO.class);

		final RuleIrTypeCondition irAccommodationReservationCondition = new RuleIrTypeCondition();
		irAccommodationReservationCondition.setVariable(cartRAOVariable);

		final RuleIrGroupCondition irAccommodationCategoryCondition = new RuleIrGroupCondition();
		irAccommodationCategoryCondition.setOperator(RuleIrGroupOperator.AND);
		irAccommodationCategoryCondition.setChildren(new ArrayList<>());

		irAccommodationCategoryCondition.getChildren().add(irAccommodationReservationCondition);
		addCategoryOrGroupCondition(categories, ACCOMMODATION_CATEGORIES_ATTRIBUTE, accommodationOfferingRAO,
				irAccommodationCategoryCondition, RuleIrAttributeOperator.CONTAINS);

		return irAccommodationCategoryCondition;
	}

	/**
	 * Create group category or condition.
	 *
	 * @param categories
	 * 		the category types
	 * @param attribute
	 * 		the attribute
	 * @param raoVariable
	 * 		the rao variable
	 * @param irAccommodationCategoryCondition
	 * 		the ir accommodation category condition
	 * @param attributeOperator
	 * 		the attribute operator
	 */
	protected void addCategoryOrGroupCondition(final List<String> categories, final String attribute, final String raoVariable,
			final RuleIrGroupCondition irAccommodationCategoryCondition, final RuleIrAttributeOperator attributeOperator)
	{
		if (Objects.nonNull(categories))
		{
			final RuleIrGroupCondition irAccommodationCategoryWrapperCondition = new RuleIrGroupCondition();
			irAccommodationCategoryWrapperCondition.setOperator(RuleIrGroupOperator.OR);
			irAccommodationCategoryWrapperCondition.setChildren(new ArrayList<>());

			for (final String category : categories)
			{
				final RuleIrGroupCondition irCategoryCondition = new RuleIrGroupCondition();
				irCategoryCondition.setOperator(RuleIrGroupOperator.AND);
				irCategoryCondition.setChildren(new ArrayList<>());

				final RuleIrAttributeCondition ruleIrAttributeCondition = new RuleIrAttributeCondition();
				ruleIrAttributeCondition.setVariable(raoVariable);
				ruleIrAttributeCondition.setAttribute(attribute);
				ruleIrAttributeCondition.setOperator(attributeOperator);
				ruleIrAttributeCondition.setValue(category);

				irCategoryCondition.getChildren().add(ruleIrAttributeCondition);
				irAccommodationCategoryWrapperCondition.getChildren().add(irCategoryCondition);
			}
			irAccommodationCategoryCondition.getChildren().add(irAccommodationCategoryWrapperCondition);
		}
	}
}
