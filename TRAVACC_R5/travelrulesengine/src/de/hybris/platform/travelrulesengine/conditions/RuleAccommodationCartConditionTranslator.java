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

import de.hybris.platform.ruledefinitions.conditions.builders.RuleIrAttributeConditionBuilder;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerContext;
import de.hybris.platform.ruleengineservices.compiler.RuleConditionTranslator;
import de.hybris.platform.ruleengineservices.compiler.RuleIrAttributeCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrAttributeOperator;
import de.hybris.platform.ruleengineservices.compiler.RuleIrCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrGroupCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrGroupOperator;
import de.hybris.platform.ruleengineservices.compiler.RuleIrLocalVariablesContainer;
import de.hybris.platform.ruleengineservices.compiler.RuleIrTypeCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrVariable;
import de.hybris.platform.ruleengineservices.compiler.RuleIrVariablesContainer;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import de.hybris.platform.travelrulesengine.rao.AccommodationRAO;


import java.util.ArrayList;
import java.util.List;


/**
 * The type Rule accommodation cart condition translator.
 */
public class RuleAccommodationCartConditionTranslator implements RuleConditionTranslator
{
	private static final String ACCOMMODATIONS_PARAMETER = "accommodations";
	private static final String ACCOMMODATION_ATTRIBUTE = "code";

	@Override
	public RuleIrCondition translate(final RuleCompilerContext context, final RuleConditionData condition,
			final RuleConditionDefinitionData ruleConditionDefinitionData)
	{
		final String cartRAOVariable = context.generateVariable(CartRAO.class);

		final RuleParameterData accommodationsParameter = condition.getParameters().get(ACCOMMODATIONS_PARAMETER);
		final List<String> accommodationCodes = accommodationsParameter.getValue();

		final RuleIrGroupCondition irAccommodationsGroupCondition = new RuleIrGroupCondition();
		irAccommodationsGroupCondition.setOperator(RuleIrGroupOperator.OR);
		irAccommodationsGroupCondition.setChildren(new ArrayList<>());

		final RuleIrGroupCondition irAccommodationsCondition = new RuleIrGroupCondition();
		irAccommodationsCondition.setOperator(RuleIrGroupOperator.AND);
		irAccommodationsCondition.setChildren(new ArrayList<>());

		final boolean isPresent = context.getVariablesGenerator().getCurrentContainer().getVariables().entrySet().stream()
				.anyMatch(entry -> AccommodationRAO.class.equals(entry.getValue().getType()));
		final String containsAccommodationRaoVariable;

		if (isPresent)
		{
			final RuleIrLocalVariablesContainer ruleIrLocalVariablesContainer = context.createLocalContainer();
			containsAccommodationRaoVariable = context.generateLocalVariable(ruleIrLocalVariablesContainer, AccommodationRAO.class);

			final RuleIrVariablesContainer container = context.getVariablesGenerator().getCurrentContainer();
			final RuleIrVariable variable = new RuleIrVariable();
			variable.setName(containsAccommodationRaoVariable);
			variable.setType(AccommodationRAO.class);
			variable.setPath(container.getPath());
			container.getVariables().put(containsAccommodationRaoVariable, variable);
		}
		else
		{
			containsAccommodationRaoVariable = context.generateVariable(AccommodationRAO.class);
		}

		final RuleIrAttributeCondition irContainsAccommodationCodeCondition = RuleIrAttributeConditionBuilder
				.newAttributeConditionFor(containsAccommodationRaoVariable).withAttribute(ACCOMMODATION_ATTRIBUTE)
				.withOperator(RuleIrAttributeOperator.IN).withValue(accommodationCodes).build();

		irAccommodationsCondition.getChildren().add(irContainsAccommodationCodeCondition);

		final RuleIrTypeCondition irSearchParamCondition = new RuleIrTypeCondition();
		irSearchParamCondition.setVariable(cartRAOVariable);

		irAccommodationsGroupCondition.getChildren().add(irAccommodationsCondition);
		irAccommodationsGroupCondition.getChildren().add(irSearchParamCondition);

		return irAccommodationsGroupCondition;
	}
}
