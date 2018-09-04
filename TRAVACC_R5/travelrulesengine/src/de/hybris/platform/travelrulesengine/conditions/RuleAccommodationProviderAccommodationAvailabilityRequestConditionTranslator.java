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
import de.hybris.platform.travelrulesengine.rao.AccommodationProviderRAO;
import de.hybris.platform.travelrulesengine.rao.RoomStayRAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * The type Rule accommodation provider accommodation availability request condition translator.
 */
public class RuleAccommodationProviderAccommodationAvailabilityRequestConditionTranslator implements RuleConditionTranslator
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

		final String accommodationAvailabilityRequestRAOVariable = context
				.generateVariable(AccommodationAvailabilityRequestRAO.class);
		final RuleIrTypeCondition irAccommodationAvailabilityRequestCondition = new RuleIrTypeCondition();
		irAccommodationAvailabilityRequestCondition.setVariable(accommodationAvailabilityRequestRAOVariable);

		final RuleIrGroupCondition irProviderCondition = new RuleIrGroupCondition();
		irProviderCondition.setOperator(RuleIrGroupOperator.AND);
		irProviderCondition.setChildren(new ArrayList<>());

		irProviderCondition.getChildren().add(irAccommodationAvailabilityRequestCondition);

		final String roomStayRAOVariable = context.generateVariable(RoomStayRAO.class);
		final RuleIrTypeCondition irRoomStayRAOCondition = new RuleIrTypeCondition();
		irRoomStayRAOCondition.setVariable(roomStayRAOVariable);

		irProviderCondition.getChildren().add(irRoomStayRAOCondition);

		createGroupProviderOrCondition(providers, ACCOMMODATION_PROVIDER_ATTRIBUTE, accommodationProviderVariable,
				irProviderCondition, RuleIrAttributeOperator.CONTAINS);

		return irProviderCondition;
	}


	/**
	 * Create group provider or condition.
	 *
	 * @param providers
	 * 		the providers
	 * @param attribute
	 * 		the attribute
	 * @param raoVariable
	 * 		the rao variable
	 * @param irProviderCondition
	 * 		the ir provider condition
	 * @param attributeOperator
	 * 		the attribute operator
	 */
	protected void createGroupProviderOrCondition(final List<String> providers, final String attribute, final String raoVariable,
			final RuleIrGroupCondition irProviderCondition, final RuleIrAttributeOperator attributeOperator)
	{
		if (Objects.nonNull(providers))
		{

			final RuleIrGroupCondition irAccommodationProviderWrapperCondition = new RuleIrGroupCondition();
			irAccommodationProviderWrapperCondition.setOperator(RuleIrGroupOperator.OR);
			irAccommodationProviderWrapperCondition.setChildren(new ArrayList<>());

			for (final String provider : providers)
			{
				final RuleIrGroupCondition irAccommodationProviderCondition = new RuleIrGroupCondition();
				irAccommodationProviderCondition.setOperator(RuleIrGroupOperator.AND);
				irAccommodationProviderCondition.setChildren(new ArrayList<>());

				final RuleIrAttributeCondition ruleIrAttributeCondition = new RuleIrAttributeCondition();
				ruleIrAttributeCondition.setVariable(raoVariable);
				ruleIrAttributeCondition.setAttribute(attribute);
				ruleIrAttributeCondition.setOperator(attributeOperator);
				ruleIrAttributeCondition.setValue(provider);

				irAccommodationProviderCondition.getChildren().add(ruleIrAttributeCondition);
				irAccommodationProviderWrapperCondition.getChildren().add(irAccommodationProviderCondition);
			}

			irProviderCondition.getChildren().add(irAccommodationProviderWrapperCondition);
		}
	}
}