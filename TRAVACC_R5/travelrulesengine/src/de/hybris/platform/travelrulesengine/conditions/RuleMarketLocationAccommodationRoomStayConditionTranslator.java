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
import de.hybris.platform.ruleengineservices.compiler.RuleIrCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrGroupCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrTypeCondition;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionData;
import de.hybris.platform.travelrulesengine.rao.AccommodationAvailabilityRequestRAO;
import de.hybris.platform.travelrulesengine.rao.RoomStayRAO;


/**
 * The type Rule market location accommodation room stay condition translator.
 */
public class RuleMarketLocationAccommodationRoomStayConditionTranslator extends AbstractLocationConditionTranslator
{
	private static final String MARKET_LOCATIONS_ATTRIBUTE = "marketLocations";

	@Override
	public RuleIrCondition translate(final RuleCompilerContext context, final RuleConditionData condition,
			final RuleConditionDefinitionData ruleConditionDefinitionData)
	{
		final String accommodationAvailabilityRequestRAOVariable = context
				.generateVariable(AccommodationAvailabilityRequestRAO.class);

		final String roomStayRAOVariable = context.generateVariable(RoomStayRAO.class);

		final RuleIrTypeCondition irRoomStayRAOCondition = new RuleIrTypeCondition();
		irRoomStayRAOCondition.setVariable(roomStayRAOVariable);

		final RuleIrGroupCondition irMarketLocationCondition = (RuleIrGroupCondition) super
				.translateMarketLocationCondition(context, condition, ruleConditionDefinitionData,
						accommodationAvailabilityRequestRAOVariable);

		// USER ADDRESS CONDITION
		createAddressCondition(MARKET_LOCATIONS_ATTRIBUTE, accommodationAvailabilityRequestRAOVariable, irMarketLocationCondition);

		irMarketLocationCondition.getChildren().add(irRoomStayRAOCondition);

		return irMarketLocationCondition;
	}
}
