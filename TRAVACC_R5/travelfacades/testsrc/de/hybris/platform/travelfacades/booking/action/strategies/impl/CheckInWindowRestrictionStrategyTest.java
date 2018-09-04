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

package de.hybris.platform.travelfacades.booking.action.strategies.impl;

import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.travel.BookingActionData;
import de.hybris.platform.commercefacades.travel.ItineraryData;
import de.hybris.platform.commercefacades.travel.OriginDestinationOptionData;
import de.hybris.platform.commercefacades.travel.TransportOfferingData;
import de.hybris.platform.commercefacades.travel.reservation.data.ReservationData;
import de.hybris.platform.commercefacades.travel.reservation.data.ReservationItemData;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.travelfacades.constants.TravelfacadesConstants;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


/**
 * Unit Test for the implementation of {@link CheckInWindowRestrictionStrategy}.
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CheckInWindowRestrictionStrategyTest
{
	private static final String ALTERNATIVE_MESSAGE_BEFORE = "booking.action.check.in.window.alternative.message.before";
	private static final String ALTERNATIVE_MESSAGE_AFTER = "booking.action.check.in.window.alternative.message.after";

	@InjectMocks
	CheckInWindowRestrictionStrategy checkInWindowRestrictionStrategy;
	@Mock
	private TimeService timeService;
	@Mock
	private ConfigurationService configurationService;
	@Mock
	private Configuration configuration;

	@Before
	public void setUp() throws Exception
	{
		given(configurationService.getConfiguration()).willReturn(configuration);
	}

	@Test
	public void testMinCheckinTimeRestriction()
	{
		final TransportOfferingData transportOfferingData1 = new TransportOfferingData();
		transportOfferingData1.setDepartureTime(DateUtils.addDays(new Date(), 4));
		transportOfferingData1.setDepartureTimeZoneId(ZoneId.systemDefault());
		transportOfferingData1.setArrivalTime(DateUtils.addDays(new Date(), 5));
		transportOfferingData1.setArrivalTimeZoneId(ZoneId.systemDefault());

		final TransportOfferingData transportOfferingData2 = new TransportOfferingData();
		transportOfferingData2.setDepartureTime(DateUtils.addDays(new Date(), 7));
		transportOfferingData2.setDepartureTimeZoneId(ZoneId.systemDefault());
		transportOfferingData2.setArrivalTime(DateUtils.addDays(new Date(), 8));
		transportOfferingData2.setArrivalTimeZoneId(ZoneId.systemDefault());

		final OriginDestinationOptionData originDestinationOptionData = new OriginDestinationOptionData();
		originDestinationOptionData.setTransportOfferings(Stream.of(transportOfferingData1, transportOfferingData2).collect(
				Collectors.toList()));

		final ItineraryData itineraryData = new ItineraryData();
		itineraryData.setOriginDestinationOptions(Stream.of(originDestinationOptionData).collect(Collectors.toList()));

		final ReservationItemData reservationItemData = new ReservationItemData();
		reservationItemData.setOriginDestinationRefNumber(0);
		reservationItemData.setReservationItinerary(itineraryData);

		final ReservationData reservationData = new ReservationData();
		reservationData.setCode("BOOK0004");
		reservationData.setReservationItems(Stream.of(reservationItemData).collect(Collectors.toList()));

		given(configuration.getInt(TravelfacadesConstants.MIN_CHECKIN_TIME_PROPERTY)).willReturn(24);
		given(configuration.getInt(TravelfacadesConstants.MAX_CHECKIN_TIME_PROPERTY)).willReturn(1);
		given(timeService.getCurrentTime()).willReturn(new Date());

		final BookingActionData bookingActionData = new BookingActionData();
		bookingActionData.setEnabled(true);
		bookingActionData.setOriginDestinationRefNumber(0);
		bookingActionData.setAlternativeMessages(new ArrayList<String>());

		final List<BookingActionData> bookingActionDataList = new ArrayList<BookingActionData>();
		bookingActionDataList.add(bookingActionData);

		checkInWindowRestrictionStrategy.applyStrategy(bookingActionDataList, reservationData);
		Assert.assertFalse(bookingActionDataList.get(0).isEnabled());
		Assert.assertEquals(ALTERNATIVE_MESSAGE_BEFORE, bookingActionDataList.get(0).getAlternativeMessages().get(0));
	}

	@Test
	public void testMaxCheckinTimeRestriction()
	{
		final TransportOfferingData transportOfferingData1 = new TransportOfferingData();
		transportOfferingData1.setDepartureTime(DateUtils.addHours(new Date(), 0));
		transportOfferingData1.setDepartureTimeZoneId(ZoneId.systemDefault());
		transportOfferingData1.setArrivalTime(DateUtils.addHours(new Date(), 2));
		transportOfferingData1.setArrivalTimeZoneId(ZoneId.systemDefault());

		final TransportOfferingData transportOfferingData2 = new TransportOfferingData();
		transportOfferingData2.setDepartureTime(DateUtils.addDays(new Date(), 1));
		transportOfferingData2.setDepartureTimeZoneId(ZoneId.systemDefault());
		transportOfferingData2.setArrivalTime(DateUtils.addDays(new Date(), 2));
		transportOfferingData2.setArrivalTimeZoneId(ZoneId.systemDefault());

		final OriginDestinationOptionData originDestinationOptionData = new OriginDestinationOptionData();
		originDestinationOptionData.setTransportOfferings(Stream.of(transportOfferingData1, transportOfferingData2).collect(
				Collectors.toList()));

		final ItineraryData itineraryData = new ItineraryData();
		itineraryData.setOriginDestinationOptions(Stream.of(originDestinationOptionData).collect(Collectors.toList()));

		final ReservationItemData reservationItemData = new ReservationItemData();
		reservationItemData.setOriginDestinationRefNumber(0);
		reservationItemData.setReservationItinerary(itineraryData);

		final ReservationData reservationData = new ReservationData();
		reservationData.setCode("BOOK0004");
		reservationData.setReservationItems(Stream.of(reservationItemData).collect(Collectors.toList()));

		given(configuration.getInt(TravelfacadesConstants.MIN_CHECKIN_TIME_PROPERTY)).willReturn(24);
		given(configuration.getInt(TravelfacadesConstants.MAX_CHECKIN_TIME_PROPERTY)).willReturn(1);
		given(timeService.getCurrentTime()).willReturn(new Date());

		final BookingActionData bookingActionData = new BookingActionData();
		bookingActionData.setEnabled(true);
		bookingActionData.setOriginDestinationRefNumber(0);
		bookingActionData.setAlternativeMessages(new ArrayList<String>());

		final List<BookingActionData> bookingActionDataList = new ArrayList<BookingActionData>();
		bookingActionDataList.add(bookingActionData);

		checkInWindowRestrictionStrategy.applyStrategy(bookingActionDataList, reservationData);
		Assert.assertFalse(bookingActionDataList.get(0).isEnabled());
		Assert.assertEquals(ALTERNATIVE_MESSAGE_AFTER, bookingActionDataList.get(0).getAlternativeMessages().get(0));
	}
}
