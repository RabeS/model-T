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
import de.hybris.platform.ruleengineservices.compiler.RuleIrAttributeCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrAttributeOperator;
import de.hybris.platform.ruleengineservices.compiler.RuleIrCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrGroupCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrGroupOperator;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import de.hybris.platform.travelrulesengine.rao.AccommodationReservationRAO;

import java.util.ArrayList;
import java.util.List;


public class RuleMarketLocationAccommodationReservationConditionTranslator extends AbstractLocationConditionTranslator
{
	private static final String INCLUDED_MARKET_LOCATION_PARAMETER = "inclMarketLocations";
	private static final String EXCLUDED_MARKET_LOCATION_PARAMETER = "excMarketLocations";
	private static final String ACCOMMODATION_MARKET_LOCATIONS_ATTRIBUTE = "marketLocations";

	@Override
	public RuleIrCondition translate(final RuleCompilerContext context, final RuleConditionData condition,
			final RuleConditionDefinitionData ruleConditionDefinitionData)
	{
		final String accommodationReservationRAOVariable = context.generateVariable(AccommodationReservationRAO.class);

		final RuleParameterData inclMarketLocationParameter = condition.getParameters().get(INCLUDED_MARKET_LOCATION_PARAMETER);
		final RuleParameterData exclMarketLocationParameter = condition.getParameters().get(EXCLUDED_MARKET_LOCATION_PARAMETER);

		final List<String> inclMarLocations = inclMarketLocationParameter.getValue();
		final List<String> exclMarLocations = exclMarketLocationParameter.getValue();

		final RuleIrGroupCondition irLocationCondition = new RuleIrGroupCondition();
		irLocationCondition.setOperator(RuleIrGroupOperator.AND);
		irLocationCondition.setChildren(new ArrayList<>());

		// INCLUDED ACCOMMODATION LOCATIONS
		createGroupLocationOrCondition(inclMarLocations, ACCOMMODATION_MARKET_LOCATIONS_ATTRIBUTE,
				accommodationReservationRAOVariable, irLocationCondition, RuleIrAttributeOperator.CONTAINS);

		// EXCLUDED ACCOMMODATION LOCATIONS
		createGroupLocationOrCondition(exclMarLocations, ACCOMMODATION_MARKET_LOCATIONS_ATTRIBUTE,
				accommodationReservationRAOVariable, irLocationCondition, RuleIrAttributeOperator.NOT_CONTAINS);

		// USER ADDRESS CONDITION
		createAddressCondition(ACCOMMODATION_MARKET_LOCATIONS_ATTRIBUTE, accommodationReservationRAOVariable, irLocationCondition);

		return irLocationCondition;
	}

}
