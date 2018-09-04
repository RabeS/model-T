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
import de.hybris.platform.travelrulesengine.rao.FareSearchRequestRAO;
import de.hybris.platform.travelrulesengine.rao.FilterTransportOfferingRAO;
import de.hybris.platform.travelrulesengine.rao.TransportOfferingRAO;
import de.hybris.platform.travelrulesengine.rule.evaluation.DiscardTransportOfferingRAOAction;

import org.drools.core.spi.KnowledgeHelper;


/**
 * Implementation of transport offering filtering action {@link DiscardTransportOfferingRAOAction}
 */
public class DefaultDiscardTransportOfferingRAOAction extends AbstractTravelCommerceRAOAction
		implements DiscardTransportOfferingRAOAction
{

	@Override
	public FilterTransportOfferingRAO discardTransportOffering(final FareSearchRequestRAO fareSearchRequestRAO,
			final TransportOfferingRAO transportOfferingRAO,
			final RuleEngineResultRAO ruleEngineResultRao, final Object ruleContext)
	{
		final KnowledgeHelper context = this.checkAndGetRuleContext((RuleActionContext) ruleContext);
		this.validateRule((RuleActionContext) context);
		ServicesUtil.validateParameterNotNull(transportOfferingRAO, "TransportOffering rao must not be null");

		final FilterTransportOfferingRAO filterTransportOfferingRAO = new FilterTransportOfferingRAO();
		filterTransportOfferingRAO.setValid(Boolean.FALSE);
		filterTransportOfferingRAO.setTransportOfferingCode(transportOfferingRAO.getTransportOfferingCode());
		getRaoUtils().addAction(fareSearchRequestRAO, filterTransportOfferingRAO);
		ruleEngineResultRao.getActions().add(filterTransportOfferingRAO);
		this.setRAOMetaData((RuleActionContext)context, filterTransportOfferingRAO);
		
		return filterTransportOfferingRAO;
	}

}
