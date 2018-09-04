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
import de.hybris.platform.travelrulesengine.model.ShowAccommodationActionResultModel;
import de.hybris.platform.travelrulesengine.rao.ShowAccommodationsActionRAO;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;


/**
 * The type Default show accommodations action strategy.
 */
public class DefaultShowAccommodationsActionStrategy extends AbstractTravelRuleActionStrategy
{
	private static final Logger LOG = Logger.getLogger(DefaultShowAccommodationsActionStrategy.class);

	@Override
	public List<ShowAccommodationActionResultModel> apply(final AbstractRuleActionRAO actionRAO)
	{
		if (!(actionRAO instanceof ShowAccommodationsActionRAO))
		{
			LOG.debug(String.format("cannot apply %s, action is not of type ShowAccommodationsActionRAO",
					this.getClass().getSimpleName()));
			return Collections.emptyList();
		}
		final ShowAccommodationsActionRAO showAccommodationsActionRAO = (ShowAccommodationsActionRAO) actionRAO;
		final ShowAccommodationActionResultModel showAccommodationActionResult = new ShowAccommodationActionResultModel();
		showAccommodationActionResult.setAccommodations(showAccommodationsActionRAO.getAccommodations());

		return Collections.singletonList(showAccommodationActionResult);
	}

	@Override
	public void undo(final ItemModel itemModel)
	{
		// do nothing
	}
}
