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

package de.hybris.platform.travelrulesengine.commerce.impl;


import de.hybris.platform.ruleengineservices.rao.RuleEngineResultRAO;
import de.hybris.platform.ruleengineservices.rule.evaluation.RuleActionContext;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.travelrulesengine.rao.FareProductRAO;
import de.hybris.platform.travelrulesengine.rao.FilterFareRAO;
import de.hybris.platform.travelrulesengine.rule.evaluation.DiscardFareRAOAction;

import org.drools.core.spi.KnowledgeHelper;


/**
 * Implementation of fare filtering action {@link DiscardFareRAOAction}
 */
public class DefaultDiscardFareRAOAction extends AbstractTravelCommerceRAOAction implements DiscardFareRAOAction
{

	@Override
	public FilterFareRAO discardFare(final FareProductRAO fareProductRAO, final RuleEngineResultRAO ruleEngineResultRao,
			final Object ruleContext)
	{
		final KnowledgeHelper context = this.checkAndGetRuleContext((RuleActionContext) ruleContext);
		this.validateRule((RuleActionContext) context);
		ServicesUtil.validateParameterNotNull(fareProductRAO, "Fare product rao must not be null");

		final FilterFareRAO filterFareRAO = new FilterFareRAO();
		filterFareRAO.setValid(Boolean.FALSE);
		filterFareRAO.setFareProductCode(fareProductRAO.getCode());
		getRaoUtils().addAction(fareProductRAO, filterFareRAO);
		ruleEngineResultRao.getActions().add(filterFareRAO);
		this.setRAOMetaData((RuleActionContext)context, filterFareRAO);
		return filterFareRAO;
	}
}
