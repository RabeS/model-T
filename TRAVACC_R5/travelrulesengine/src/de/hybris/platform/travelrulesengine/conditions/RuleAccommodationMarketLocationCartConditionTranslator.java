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
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * The type Rule accommodation market location cart condition translator.
 */
public class RuleAccommodationMarketLocationCartConditionTranslator extends AbstractLocationConditionTranslator
{

	private static final String INCLUDED_MARKET_LOCATION_PARAMETER = "inclMarketLocations";
	private static final String EXCLUDED_MARKET_LOCATION_PARAMETER = "excMarketLocations";
	private static final String ACCOMMODATION_MARKET_LOCATIONS_ATTRIBUTE = "marketLocations";
	private static final String CART_RAO_BOOKING_JOURNEY_TYPE_ATTRIBUTE = "bookingJourneyType";
	private List<String> notAllowedBookingJourneyTypes;

	@Override
	public RuleIrCondition translate(final RuleCompilerContext context, final RuleConditionData condition,
			final RuleConditionDefinitionData ruleConditionDefinitionData)
	{
		final String cartRAOVariable = context.generateVariable(CartRAO.class);

		final RuleParameterData inclMarketLocationParameter = condition.getParameters().get(INCLUDED_MARKET_LOCATION_PARAMETER);
		final RuleParameterData exclMarketLocationParameter = condition.getParameters().get(EXCLUDED_MARKET_LOCATION_PARAMETER);

		final List<String> inclMarLocations = inclMarketLocationParameter.getValue();
		final List<String> exclMarLocations = exclMarketLocationParameter.getValue();

		//		Wrapper
		final RuleIrGroupCondition irMarketLocationWrapperCondition = new RuleIrGroupCondition();
		irMarketLocationWrapperCondition.setOperator(RuleIrGroupOperator.AND);
		irMarketLocationWrapperCondition.setChildren(new ArrayList<>());


		final RuleIrGroupCondition irLocationCondition = new RuleIrGroupCondition();
		irLocationCondition.setOperator(RuleIrGroupOperator.AND);
		irLocationCondition.setChildren(new ArrayList<>());

		// INCLUDED ACCOMMODATION LOCATIONS
		createGroupLocationOrCondition(inclMarLocations, ACCOMMODATION_MARKET_LOCATIONS_ATTRIBUTE, cartRAOVariable,
				irLocationCondition, RuleIrAttributeOperator.CONTAINS);

		// EXCLUDED ACCOMMODATION LOCATIONS
		createGroupLocationOrCondition(exclMarLocations, ACCOMMODATION_MARKET_LOCATIONS_ATTRIBUTE, cartRAOVariable,
				irLocationCondition, RuleIrAttributeOperator.NOT_CONTAINS);

		// USER ADDRESS CONDITION
		createAddressCondition(ACCOMMODATION_MARKET_LOCATIONS_ATTRIBUTE, cartRAOVariable, irLocationCondition);

		//		BookingJourneyTypes condition
		final RuleIrGroupCondition irBookingJourneyTypesGroupCondition = new RuleIrGroupCondition();
		irBookingJourneyTypesGroupCondition.setOperator(RuleIrGroupOperator.AND);
		irBookingJourneyTypesGroupCondition.setChildren(new ArrayList<>());

		final RuleIrAttributeCondition irBookingJourneyTypesCondition = new RuleIrAttributeCondition();
		irBookingJourneyTypesCondition.setVariable(cartRAOVariable);
		irBookingJourneyTypesCondition.setAttribute(CART_RAO_BOOKING_JOURNEY_TYPE_ATTRIBUTE);
		irBookingJourneyTypesCondition.setOperator(RuleIrAttributeOperator.NOT_IN);
		irBookingJourneyTypesCondition.setValue(getNotAllowedBookingJourneyTypes());

		irBookingJourneyTypesGroupCondition.getChildren().add(irBookingJourneyTypesCondition);

		irMarketLocationWrapperCondition.getChildren().add(irBookingJourneyTypesGroupCondition);
		irMarketLocationWrapperCondition.getChildren().add(irLocationCondition);

		return irMarketLocationWrapperCondition;
	}


	/**
	 * Gets not allowed booking journey types.
	 *
	 * @return the not allowed booking journey types
	 */
	protected List<String> getNotAllowedBookingJourneyTypes()
	{
		return notAllowedBookingJourneyTypes;
	}

	/**
	 * Sets not allowed booking journey types.
	 *
	 * @param notAllowedBookingJourneyTypes
	 * 		the not allowed booking journey types
	 */
	@Required
	public void setNotAllowedBookingJourneyTypes(final List<String> notAllowedBookingJourneyTypes)
	{
		this.notAllowedBookingJourneyTypes = notAllowedBookingJourneyTypes;
	}

}