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

package de.hybris.platform.travelfacades.accommodation.handlers.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.accommodation.AccommodationAvailabilityRequestData;
import de.hybris.platform.commercefacades.accommodation.AccommodationAvailabilityResponseData;
import de.hybris.platform.commercefacades.accommodation.ReservedRoomStayData;
import de.hybris.platform.travelfacades.facades.accommodation.handlers.impl.ReservedRoomStaysHandler;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;


/**
 * Unit test class for {@link ReservedRoomStaysHandler}
 */

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class ReservedRoomStaysHandlerTest
{
	@InjectMocks
	private ReservedRoomStaysHandler handler;

	@Test
	public void testPopulate()
	{
		final AccommodationAvailabilityRequestData availabilityRequestData = new AccommodationAvailabilityRequestData();
		final ReservedRoomStayData roomStay = new ReservedRoomStayData();
		availabilityRequestData.setReservedRoomStays(Stream.of(roomStay).collect(Collectors.toList()));
		final AccommodationAvailabilityResponseData accommodationAvailabilityResponseData = new AccommodationAvailabilityResponseData();
		handler.handle(availabilityRequestData, accommodationAvailabilityResponseData);
		Assert.assertNotNull(accommodationAvailabilityResponseData.getReservedRoomStays());
	}
}
