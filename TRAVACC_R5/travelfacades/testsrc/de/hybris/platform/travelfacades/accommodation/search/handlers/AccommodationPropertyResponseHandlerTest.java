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

package de.hybris.platform.travelfacades.accommodation.search.handlers;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.accommodation.AccommodationSearchResponseData;
import de.hybris.platform.travelfacades.accommodation.search.mock.request.MockAccommodationSearchRequestData;
import de.hybris.platform.travelfacades.accommodation.search.mock.response.MockAccommodationOfferingDayRateDataList;
import de.hybris.platform.travelfacades.facades.accommodation.search.handlers.impl.AccommodationPropertyResponseHandler;
import de.hybris.platform.travelfacades.facades.accommodation.search.manager.PropertyPipelineManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import reactor.util.Assert;


/**
 * unit test for Accommodation property response handler.
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class AccommodationPropertyResponseHandlerTest
{
	MockAccommodationSearchRequestData requestBuilder = new MockAccommodationSearchRequestData();
	MockAccommodationOfferingDayRateDataList dayRatesBuilder = new MockAccommodationOfferingDayRateDataList();

	@Mock
	PropertyPipelineManager propertyPipelineManager;

	AccommodationPropertyResponseHandler handler = new AccommodationPropertyResponseHandler();

	AccommodationSearchResponseData response = new AccommodationSearchResponseData();

	@Test
	public void testHandler()
	{
		handler.setPropertyPipelineManager(propertyPipelineManager);
		handler.handle(dayRatesBuilder.buildDayRateDataList(), requestBuilder.buildRequestData(), response);
		Assert.notEmpty(response.getProperties());
	}

}
