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

package de.hybris.platform.travelfacades.fare.search.handlers.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.travel.FareSearchRequestData;
import de.hybris.platform.commercefacades.travel.FareSelectionData;
import de.hybris.platform.commercefacades.travel.ScheduledRouteData;
import de.hybris.platform.travelfacades.fare.search.handlers.impl.BasicResponseHandler;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;


/**
 * unit test for Basic response handler.
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class BasicResponseHandlerTest
{
	private final BasicResponseHandler handler = new BasicResponseHandler();

	@Test
	public void populateTargetTest()
	{
		final List<ScheduledRouteData> scheduledRoutes = new ArrayList<>(1);
		scheduledRoutes.add(new ScheduledRouteData());

		final FareSearchRequestData fareSearchRequestData = new FareSearchRequestData();
		final FareSelectionData fareSelectionData = new FareSelectionData();

		handler.handle(scheduledRoutes, fareSearchRequestData, fareSelectionData);

		Assert.assertNotNull(fareSelectionData.getPricedItineraries());
		Assert.assertTrue(fareSelectionData.getPricedItineraries().isEmpty());
	}
}
