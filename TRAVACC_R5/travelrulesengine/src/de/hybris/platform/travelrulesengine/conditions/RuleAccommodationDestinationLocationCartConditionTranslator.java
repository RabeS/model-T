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
import de.hybris.platform.ruleengineservices.compiler.RuleIrAttributeOperator;
import de.hybris.platform.ruleengineservices.compiler.RuleIrCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrGroupCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrGroupOperator;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import de.hybris.platform.travelrulesengine.rao.AccommodationOfferingRAO;

import java.util.ArrayList;
import java.util.List;


/**
 * The type Rule accommodation destination location cart condition translator.
 */
public class RuleAccommodationDestinationLocationCartConditionTranslator extends AbstractLocationConditionTranslator
{
	private static final String INCLUDED_LOCATION_PARAMETER = "inclLocations";
	private static final String EXCLUDED_LOCATION_PARAMETER = "excLocations";
	private static final String DESTINATION_LOCATIONS_ATTRIBUTE = "locations";

	@Override
	public RuleIrCondition translate(final RuleCompilerContext context, final RuleConditionData condition,
			final RuleConditionDefinitionData ruleConditionDefinitionData)
	{
		final String accommodationOfferingRAOVariable = context.generateVariable(AccommodationOfferingRAO.class);
		final RuleParameterData inclLocationParameter = condition.getParameters().get(INCLUDED_LOCATION_PARAMETER);
		final RuleParameterData exclLocationParameter = condition.getParameters().get(EXCLUDED_LOCATION_PARAMETER);

		final List<String> inclLocations = inclLocationParameter.getValue();
		final List<String> excLocations = exclLocationParameter.getValue();

		final RuleIrGroupCondition irLocationCondition = new RuleIrGroupCondition();
		irLocationCondition.setOperator(RuleIrGroupOperator.AND);
		irLocationCondition.setChildren(new ArrayList<>());

		// INCLUDED LOCATIONS
		createGroupLocationOrCondition(inclLocations, DESTINATION_LOCATIONS_ATTRIBUTE,
				accommodationOfferingRAOVariable, irLocationCondition, RuleIrAttributeOperator.CONTAINS);

		// EXCLUDED LOCATIONS
		createGroupLocationOrCondition(excLocations, DESTINATION_LOCATIONS_ATTRIBUTE,
				accommodationOfferingRAOVariable, irLocationCondition, RuleIrAttributeOperator.NOT_CONTAINS);

		return irLocationCondition;
	}
}
