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
package de.hybris.platform.travelrulesengine.converters.populator;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.ruleengineservices.converters.populator.CartRaoPopulator;
import de.hybris.platform.ruleengineservices.rao.CartRAO;

import java.util.Objects;


/**
 * The type Travel cart rao populator.
 */
public class TravelCartRaoPopulator extends CartRaoPopulator
{
	@Override
	public void populate(final AbstractOrderModel source, final CartRAO target)
	{
		super.populate(source, target);

		if(Objects.nonNull(source.getBookingJourneyType()))
		{
			target.setBookingJourneyType(source.getBookingJourneyType().toString());
		}
	}
}
