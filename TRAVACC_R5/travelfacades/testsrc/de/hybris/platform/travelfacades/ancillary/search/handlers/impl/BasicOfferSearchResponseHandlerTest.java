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

package de.hybris.platform.travelfacades.ancillary.search.handlers.impl;

import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.travel.ItineraryData;
import de.hybris.platform.commercefacades.travel.ancillary.data.OfferRequestData;
import de.hybris.platform.commercefacades.travel.ancillary.data.OfferResponseData;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


/**
 * unit test for Basic offer search response handler.
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class BasicOfferSearchResponseHandlerTest
{

	@Mock
	private OfferRequestData offerRequestData;

	@Mock
	private OfferResponseData offerResponseData;

	private final BasicOfferSearchResponseHandler handler = new BasicOfferSearchResponseHandler();

	@Test
	public void testPopulate()
	{
		offerRequestData = Mockito.mock(OfferRequestData.class);
		offerResponseData = Mockito.mock(OfferResponseData.class);
		final List<ItineraryData> itineraries = new ArrayList<>();
		given(offerRequestData.getItineraries()).willReturn(itineraries);
		handler.handle(offerRequestData, offerResponseData);
		Mockito.verify(offerResponseData).setItineraries(itineraries);
	}

}
