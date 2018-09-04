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
import de.hybris.platform.travelrulesengine.model.ShowAccommodationOfferingActionResultModel;
import de.hybris.platform.travelrulesengine.rao.ShowAccommodationOfferingsActionRAO;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;


public class DefaultShowAccommodationOfferingsActionStrategy extends AbstractTravelRuleActionStrategy
{
	private static final Logger LOG = Logger.getLogger(DefaultShowAccommodationOfferingsActionStrategy.class);

	@Override
	public List<ShowAccommodationOfferingActionResultModel> apply(final AbstractRuleActionRAO actionRAO)
	{
		if (!(actionRAO instanceof ShowAccommodationOfferingsActionRAO))
		{
			LOG.debug(String.format("cannot apply %s, action is not of type ShowAccommodationOfferingsActionRAO",
					this.getClass().getSimpleName()));
			return Collections.emptyList();
		}
		final ShowAccommodationOfferingsActionRAO showAccommodationOfferingsActionRAO = (ShowAccommodationOfferingsActionRAO)
				actionRAO;
		final ShowAccommodationOfferingActionResultModel showAccommodationOfferingActionResultModel = new
				ShowAccommodationOfferingActionResultModel();
		showAccommodationOfferingActionResultModel
				.setAccommodationOfferings(showAccommodationOfferingsActionRAO.getAccommodationOfferings());

		return Collections.singletonList(showAccommodationOfferingActionResultModel);
	}

	@Override
	public void undo(final ItemModel itemModel)
	{
		// do nothing
	}
}
