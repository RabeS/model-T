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

package de.hybris.platform.travelfacades.facades.packages.handlers.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.accommodation.AccommodationOfferingDayRateData;
import de.hybris.platform.commercefacades.accommodation.AccommodationSearchResponseData;
import de.hybris.platform.commercefacades.accommodation.PropertyData;
import de.hybris.platform.commercefacades.packages.PackageData;
import de.hybris.platform.commercefacades.packages.request.PackageSearchRequestData;
import de.hybris.platform.commercefacades.travel.FareSearchRequestData;
import de.hybris.platform.commercefacades.travel.FareSelectionData;
import de.hybris.platform.travelfacades.fare.search.FareSearchFacade;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


/**
 * unit test for Package fare search response handler.
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class PackageFareSearchResponseHandlerTest
{
	@InjectMocks
	PackageFareSearchResponseHandler packageFareSearchResponseHandler;

	@Mock
	FareSearchFacade packageSearchFareSearchFacade;

	@Test
	public void testHandle()
	{
		final List<AccommodationOfferingDayRateData> accommodationOfferingDayRates = new ArrayList<>();
		final PackageSearchRequestData packageSearchRequestData = new PackageSearchRequestData();
		final AccommodationSearchResponseData accommodationSearchResponse = new AccommodationSearchResponseData();

		final FareSearchRequestData fareSearchRequestData = new FareSearchRequestData();
		packageSearchRequestData.setFareSearchRequestData(fareSearchRequestData);
		final FareSelectionData fareSelectionData = new FareSelectionData();
		final List<PropertyData> properties = new ArrayList<>();
		final PackageData packageData = new PackageData();
		properties.add(packageData);
		accommodationSearchResponse.setProperties(properties);

		Mockito.when(packageSearchFareSearchFacade.doSearch(fareSearchRequestData)).thenReturn(fareSelectionData);

		packageFareSearchResponseHandler.handle(accommodationOfferingDayRates, packageSearchRequestData,
				accommodationSearchResponse);

		Assert.assertEquals(fareSelectionData, packageData.getFareSelectionData());
	}
}
