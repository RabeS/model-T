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
import de.hybris.platform.travelrulesengine.model.ShowExtraProductActionResultModel;
import de.hybris.platform.travelrulesengine.rao.ShowExtraProductsActionRAO;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;


/**
 * The type Default show extra products action strategy.
 */
public class DefaultShowExtraProductsActionStrategy extends AbstractTravelRuleActionStrategy
{
	private static final Logger LOG = Logger.getLogger(DefaultShowExtraProductsActionStrategy.class);

	@Override
	public List<ShowExtraProductActionResultModel> apply(final AbstractRuleActionRAO actionRAO)
	{
		if (!(actionRAO instanceof ShowExtraProductsActionRAO))
		{
			LOG.debug(String.format("cannot apply %s, action is not of type ShowExtraProductsActionRAO",
					this.getClass().getSimpleName()));
			return Collections.emptyList();
		}
		final ShowExtraProductsActionRAO showExtraProductsActionRAO = (ShowExtraProductsActionRAO) actionRAO;
		final ShowExtraProductActionResultModel showExtraProductActionResultModel = new ShowExtraProductActionResultModel();
		showExtraProductActionResultModel.setProducts(showExtraProductsActionRAO.getProducts());

		return Collections.singletonList(showExtraProductActionResultModel);
	}

	@Override
	public void undo(final ItemModel itemModel)
	{
		// do nothing
	}
}
