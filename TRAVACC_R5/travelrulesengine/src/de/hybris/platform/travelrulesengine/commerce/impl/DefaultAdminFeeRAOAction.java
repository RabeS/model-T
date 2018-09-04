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

import de.hybris.platform.ruleengineservices.rao.AbstractRuleActionRAO;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rao.RuleEngineResultRAO;
import de.hybris.platform.ruleengineservices.rule.evaluation.RuleActionContext;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.travelrulesengine.rao.FeeRAO;
import de.hybris.platform.travelrulesengine.rule.evaluation.AdminFeeRAOAction;

import org.drools.core.spi.KnowledgeHelper;


/**
 * Implementation class for Fee RAO action {@link AdminFeeRAOAction}
 */
public class DefaultAdminFeeRAOAction extends AbstractTravelCommerceRAOAction implements AdminFeeRAOAction
{
	@Override
	public FeeRAO addAdminFee(final CartRAO cartRao, final RuleEngineResultRAO ruleEngineResultRao, final Object ruleContext)
	{
		final KnowledgeHelper context = this.checkAndGetRuleContext((RuleActionContext) ruleContext);
		this.validateRule((RuleActionContext) context);
		ServicesUtil.validateParameterNotNull(cartRao, "cart rao must not be null");

		final FeeRAO feeRao = this.getTravelRuleEngineCalculationService().addFee(cartRao);
		if (feeRao == null)
		{
			return null;
		}
		this.setRAOMetaData((RuleActionContext) context, new AbstractRuleActionRAO[]
		{ feeRao });
		
		ruleEngineResultRao.getActions().add(feeRao);
		return feeRao;
	}
}
