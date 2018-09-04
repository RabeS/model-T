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

package de.hybris.platform.travelfacades.fare.search.strategies.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.travelservices.model.travel.LocationModel;
import de.hybris.platform.travelservices.model.travel.TransportFacilityModel;
import de.hybris.platform.travelservices.services.TravelLocationService;

import java.util.Collection;
import java.util.Collections;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


/**
 * unit test for City location codes resolving strategy.
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CityLocationCodesResolvingStrategyTest
{
	@InjectMocks
	CityLocationCodesResolvingStrategy cityLocationCodesResolvingStrategy;

	@Mock
	TravelLocationService travelLocationService;

	@Test
	public void testGetLocationCodes()
	{

		final String locationParameter = "London";
		final LocationModel locationModel = new LocationModel();
		Mockito.when(travelLocationService.getLocation(locationParameter)).thenReturn(locationModel);

		Assert.assertTrue(CollectionUtils.isEmpty(cityLocationCodesResolvingStrategy.getLocationCodes(locationParameter)));


		final TransportFacilityModel transportfacility = new TransportFacilityModel();
		transportfacility.setCode("LTN");
		final Collection<TransportFacilityModel> transportFacilities = Collections.singletonList(transportfacility);
		locationModel.setTransportFacility(transportFacilities);

		Assert.assertTrue(CollectionUtils.isNotEmpty(cityLocationCodesResolvingStrategy.getLocationCodes(locationParameter)));
	}

}
