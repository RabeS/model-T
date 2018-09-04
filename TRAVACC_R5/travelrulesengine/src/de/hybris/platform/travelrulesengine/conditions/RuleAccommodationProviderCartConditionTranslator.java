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
import de.hybris.platform.travelrulesengine.rao.AccommodationProviderRAO;

import java.util.ArrayList;
import java.util.List;


/**
 * The type Rule brand cart condition translator.
 */
public class RuleAccommodationProviderCartConditionTranslator implements RuleConditionTranslator
{
	private static final String ACCOMMODATION_PROVIDERS_PARAMETER = "accommodationProviders";

	private static final String ACCOMMODATION_PROVIDER_ATTRIBUTE = "code";

	@Override
	public RuleIrCondition translate(final RuleCompilerContext context, final RuleConditionData condition,
			final RuleConditionDefinitionData ruleConditionDefinitionData)
	{
		final String accommodationProviderVariable = context.generateVariable(AccommodationProviderRAO.class);

		final RuleParameterData providerParameter = condition.getParameters().get(ACCOMMODATION_PROVIDERS_PARAMETER);

		final List<String> providers = providerParameter.getValue();

		final RuleIrGroupCondition irAccommodationProviderOrWrapperCondition = new RuleIrGroupCondition();
		irAccommodationProviderOrWrapperCondition.setOperator(RuleIrGroupOperator.OR);
		irAccommodationProviderOrWrapperCondition.setChildren(new ArrayList<>());

		for (final String provider : providers)
		{
			final RuleIrGroupCondition irAccommodationProviderWrapperCondition = new RuleIrGroupCondition();
			irAccommodationProviderWrapperCondition.setOperator(RuleIrGroupOperator.AND);
			irAccommodationProviderWrapperCondition.setChildren(new ArrayList<>());

			final RuleIrAttributeCondition irAccommodationProviderCondition = new RuleIrAttributeCondition();
			irAccommodationProviderCondition.setVariable(accommodationProviderVariable);
			irAccommodationProviderCondition.setAttribute(ACCOMMODATION_PROVIDER_ATTRIBUTE);
			irAccommodationProviderCondition.setOperator(RuleIrAttributeOperator.EQUAL);
			irAccommodationProviderCondition.setValue(provider);

			irAccommodationProviderWrapperCondition.getChildren().add(irAccommodationProviderCondition);
			irAccommodationProviderOrWrapperCondition.getChildren().add(irAccommodationProviderWrapperCondition);
		}

		return irAccommodationProviderOrWrapperCondition;
	}
}
