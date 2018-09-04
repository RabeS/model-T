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
import de.hybris.platform.travelrulesengine.model.ShowAccommodationCategoryActionResultModel;
import de.hybris.platform.travelrulesengine.rao.ShowAccommodationCategoriesActionRAO;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;


/**
 * The type Default show accommodation categories action strategy.
 */
public class DefaultShowAccommodationCategoriesActionStrategy extends AbstractTravelRuleActionStrategy
{

	private static final Logger LOG = Logger.getLogger(DefaultShowAccommodationCategoriesActionStrategy.class);

	@Override
	public List<ShowAccommodationCategoryActionResultModel> apply(final AbstractRuleActionRAO actionRAO)
	{
		if (!(actionRAO instanceof ShowAccommodationCategoriesActionRAO))
		{
			LOG.debug(String.format("cannot apply %s, action is not of type ShowAccommodationCategoriesActionRAO",
					this.getClass().getSimpleName()));
			return Collections.emptyList();
		}
		final ShowAccommodationCategoriesActionRAO showAccommodationCategoriesActionRAO = (ShowAccommodationCategoriesActionRAO)
				actionRAO;
		final ShowAccommodationCategoryActionResultModel showAccommodationCategoryActionResult = new
				ShowAccommodationCategoryActionResultModel();
		showAccommodationCategoryActionResult.setCategories(showAccommodationCategoriesActionRAO.getAccommodationCategories());

		return Collections.singletonList(showAccommodationCategoryActionResult);
	}

	@Override
	public void undo(final ItemModel itemModel)
	{
		//do nothing
	}
}
