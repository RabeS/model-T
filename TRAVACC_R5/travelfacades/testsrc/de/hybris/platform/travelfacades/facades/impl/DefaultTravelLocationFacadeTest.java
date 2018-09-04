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

package de.hybris.platform.travelfacades.facades.impl;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.travel.LocationData;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.travelservices.model.travel.LocationModel;
import de.hybris.platform.travelservices.services.TravelLocationService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * unit test for Default travel location facade.
 */
@UnitTest
public class DefaultTravelLocationFacadeTest
{
	private DefaultTravelLocationFacade travelLocationFacade;

	@Mock
	private TravelLocationService travelLocationService;

	@Mock
	private Converter<LocationModel, LocationData> locationConverter;

	@Mock
	private LocationModel locationModel;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		travelLocationFacade = new DefaultTravelLocationFacade();
		travelLocationFacade.setTravelLocationService(travelLocationService);
		travelLocationFacade.setLocationConverter(locationConverter);
	}

	@Test
	public void getLocation()
	{
		when(travelLocationFacade.getTravelLocationService().getLocation(Matchers.anyString())).thenReturn(locationModel);
		travelLocationFacade.getLocation(Matchers.anyString());
		verify(travelLocationFacade.getLocationConverter(), times(1)).convert(locationModel);
	}

}
