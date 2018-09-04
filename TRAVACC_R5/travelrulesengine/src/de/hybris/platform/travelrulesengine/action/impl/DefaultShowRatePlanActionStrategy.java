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
package de.hybris.platform.travelrulesengine.action.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.ruleengineservices.rao.AbstractRuleActionRAO;
import de.hybris.platform.travelrulesengine.model.ShowRatePlanActionResultModel;
import de.hybris.platform.travelrulesengine.rao.ShowRatePlansActionRAO;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;


/**
 * Strategy responsible for showing rate plans based on rules evaluation result
 */
public class DefaultShowRatePlanActionStrategy extends AbstractTravelRuleActionStrategy
{
	private static final Logger LOG = Logger.getLogger(DefaultShowRatePlanActionStrategy.class);

	@Override
	public List<ShowRatePlanActionResultModel> apply(final AbstractRuleActionRAO actionRAO)
	{
		if (!(actionRAO instanceof ShowRatePlansActionRAO))
		{
			LOG.debug(String.format("cannot apply %s, action is not of type ShowRatePlansActionRAO", this.getClass().getSimpleName()));
			return Collections.emptyList();
		}
		final ShowRatePlansActionRAO showRatePlansActionRAO = (ShowRatePlansActionRAO) actionRAO;
		final ShowRatePlanActionResultModel showRatePlanActionResultModel = new ShowRatePlanActionResultModel();
		showRatePlanActionResultModel.setRatePlans(showRatePlansActionRAO.getRatePlans());

		return Collections.singletonList(showRatePlanActionResultModel);
	}

	@Override
	public void undo(final ItemModel itemModel)
	{
		// do nothing
	}

}
