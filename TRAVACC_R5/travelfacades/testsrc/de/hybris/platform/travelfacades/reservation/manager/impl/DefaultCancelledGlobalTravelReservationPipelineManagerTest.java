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

package de.hybris.platform.travelfacades.reservation.manager.impl;

import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.travel.GlobalTravelReservationData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.travelfacades.reservation.handlers.GlobalTravelReservationHandler;
import de.hybris.platform.travelfacades.reservation.handlers.impl.GlobalTravelReservationBasicHandler;
import de.hybris.platform.travelfacades.reservation.manager.AccommodationReservationPipelineManager;
import de.hybris.platform.travelfacades.reservation.manager.ReservationPipelineManager;
import de.hybris.platform.travelservices.enums.OrderEntryType;
import de.hybris.platform.travelservices.services.BookingService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;


/**
 * unit test for Default cancelled global travel reservation pipeline manager.
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultCancelledGlobalTravelReservationPipelineManagerTest
{
	@InjectMocks
	DefaultCancelledGlobalTravelReservationPipelineManager defaultCancelledGlobalTravelReservationPipelineManager;

	@Mock
	ReservationPipelineManager reservationPipelineManager;

	@Mock
	AccommodationReservationPipelineManager accommodationReservationPipelineManager;

	@Mock
	List<GlobalTravelReservationHandler> handlers;

	@Mock
	BookingService bookingService;

	DefaultAccommodationReservationPipelineManager defaultAccommodationReservationPipelineManager;

	DefaultReservationPipelineManager defaultReservationPipelineManager;

	@Mock
	AbstractOrderModel abstractOrderModel;

	@Mock
	AbstractOrderEntryModel abstractOrderEntryModel;

	@Mock
	AbstractOrderEntryModel transportAbstractOrderEntryModel;

	@Mock
	AbstractOrderEntryModel accommodationAbstractOrderEntryModel;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		defaultReservationPipelineManager = new DefaultReservationPipelineManager();
		defaultReservationPipelineManager.setHandlers(Collections.EMPTY_LIST);
		defaultAccommodationReservationPipelineManager = new DefaultAccommodationReservationPipelineManager();
		defaultAccommodationReservationPipelineManager.setHandlers(Collections.EMPTY_LIST);
		defaultCancelledGlobalTravelReservationPipelineManager.setBookingService(bookingService);
		defaultCancelledGlobalTravelReservationPipelineManager.setHandlers(Collections.EMPTY_LIST);
		defaultCancelledGlobalTravelReservationPipelineManager.setReservationPipelineManager(defaultReservationPipelineManager);
		defaultCancelledGlobalTravelReservationPipelineManager
				.setAccommodationReservationPipelineManager(defaultAccommodationReservationPipelineManager);
	}

	@Test
	public void testExecutePipelineWithEmptyOrderEntries()
	{
		defaultCancelledGlobalTravelReservationPipelineManager.executePipeline(abstractOrderModel);
		given(abstractOrderModel.getEntries()).willReturn(Collections.emptyList());
		given(bookingService.checkIfAnyOrderEntryByType(Matchers.any(), Matchers.any())).willReturn(false);
		final GlobalTravelReservationData globalTravelReservationData = new GlobalTravelReservationData();
		Assert.assertNull(globalTravelReservationData.getReservationData());
		Assert.assertNull(globalTravelReservationData.getAccommodationReservationData());
	}

	@Test
	public void testExecutePipelineWithTransportOrderEntries()
	{
		given(abstractOrderModel.getEntries()).willReturn(Stream.of(abstractOrderEntryModel).collect(Collectors.toList()));
		given(bookingService.checkIfAnyOrderEntryByType(abstractOrderModel, OrderEntryType.TRANSPORT)).willReturn(true);
		given(bookingService.checkIfAnyOrderEntryByType(abstractOrderModel, OrderEntryType.ACCOMMODATION)).willReturn(false);
		given(abstractOrderModel.getTransportationOrderStatus()).willReturn(OrderStatus.ACTIVE);
		given(abstractOrderEntryModel.getType()).willReturn(OrderEntryType.TRANSPORT);
		final GlobalTravelReservationData globalTravelReservationData = defaultCancelledGlobalTravelReservationPipelineManager
				.executePipeline(abstractOrderModel);
		Assert.assertNotNull(globalTravelReservationData.getReservationData());
		Assert.assertNull(globalTravelReservationData.getAccommodationReservationData());
	}

	@Test
	public void testExecutePipelineWithAccommodationOrderEntries()
	{
		given(abstractOrderModel.getEntries()).willReturn(Stream.of(abstractOrderEntryModel).collect(Collectors.toList()));
		given(bookingService.checkIfAnyOrderEntryByType(abstractOrderModel, OrderEntryType.TRANSPORT)).willReturn(false);
		given(bookingService.checkIfAnyOrderEntryByType(abstractOrderModel, OrderEntryType.ACCOMMODATION)).willReturn(true);
		given(abstractOrderModel.getAccommodationOrderStatus()).willReturn(OrderStatus.ACTIVE);
		given(abstractOrderEntryModel.getType()).willReturn(OrderEntryType.ACCOMMODATION);
		final GlobalTravelReservationData globalTravelReservationData = defaultCancelledGlobalTravelReservationPipelineManager
				.executePipeline(abstractOrderModel);
		Assert.assertNull(globalTravelReservationData.getReservationData());
		Assert.assertNotNull(globalTravelReservationData.getAccommodationReservationData());
	}

	@Test
	public void testExecutePipelineWithBothEntries()
	{
		given(abstractOrderModel.getEntries())
				.willReturn(Arrays.asList(transportAbstractOrderEntryModel, accommodationAbstractOrderEntryModel));
		given(bookingService.checkIfAnyOrderEntryByType(abstractOrderModel, OrderEntryType.TRANSPORT)).willReturn(true);
		given(bookingService.checkIfAnyOrderEntryByType(abstractOrderModel, OrderEntryType.ACCOMMODATION)).willReturn(true);
		given(abstractOrderModel.getTransportationOrderStatus()).willReturn(OrderStatus.ACTIVE);
		given(abstractOrderModel.getAccommodationOrderStatus()).willReturn(OrderStatus.ACTIVE);
		given(transportAbstractOrderEntryModel.getType()).willReturn(OrderEntryType.TRANSPORT);
		given(accommodationAbstractOrderEntryModel.getType()).willReturn(OrderEntryType.ACCOMMODATION);
		final GlobalTravelReservationData globalTravelReservationData = defaultCancelledGlobalTravelReservationPipelineManager
				.executePipeline(abstractOrderModel);
		Assert.assertNotNull(globalTravelReservationData.getReservationData());
		Assert.assertNotNull(globalTravelReservationData.getAccommodationReservationData());
	}

	@Test
	public void testExecutePipelineWithCancelledOrder()
	{
		given(abstractOrderModel.getEntries())
				.willReturn(Arrays.asList(transportAbstractOrderEntryModel, accommodationAbstractOrderEntryModel));
		given(bookingService.checkIfAnyOrderEntryByType(abstractOrderModel, OrderEntryType.TRANSPORT)).willReturn(true);
		given(bookingService.checkIfAnyOrderEntryByType(abstractOrderModel, OrderEntryType.ACCOMMODATION)).willReturn(true);
		given(abstractOrderModel.getTransportationOrderStatus()).willReturn(OrderStatus.CANCELLED);
		given(abstractOrderModel.getAccommodationOrderStatus()).willReturn(OrderStatus.CANCELLED);
		given(transportAbstractOrderEntryModel.getType()).willReturn(OrderEntryType.TRANSPORT);
		given(accommodationAbstractOrderEntryModel.getType()).willReturn(OrderEntryType.ACCOMMODATION);

		given(bookingService.getLastActiveOrderForType(abstractOrderModel, OrderEntryType.TRANSPORT))
				.willReturn(abstractOrderModel);
		given(bookingService.getLastActiveOrderForType(abstractOrderModel, OrderEntryType.ACCOMMODATION))
				.willReturn(abstractOrderModel);

		final GlobalTravelReservationData globalTravelReservationData = defaultCancelledGlobalTravelReservationPipelineManager
				.executePipeline(abstractOrderModel);
		Assert.assertNull(globalTravelReservationData.getReservationData());
		Assert.assertNull(globalTravelReservationData.getAccommodationReservationData());
	}

	@Test
	public void testExecutePipelineWithHandlers()
	{
		given(abstractOrderModel.getEntries())
				.willReturn(Arrays.asList(transportAbstractOrderEntryModel, accommodationAbstractOrderEntryModel));
		given(bookingService.checkIfAnyOrderEntryByType(abstractOrderModel, OrderEntryType.TRANSPORT)).willReturn(true);
		given(bookingService.checkIfAnyOrderEntryByType(abstractOrderModel, OrderEntryType.ACCOMMODATION)).willReturn(true);
		given(abstractOrderModel.getTransportationOrderStatus()).willReturn(OrderStatus.ACTIVE);
		given(abstractOrderModel.getAccommodationOrderStatus()).willReturn(OrderStatus.ACTIVE);
		given(transportAbstractOrderEntryModel.getType()).willReturn(OrderEntryType.TRANSPORT);
		given(accommodationAbstractOrderEntryModel.getType()).willReturn(OrderEntryType.ACCOMMODATION);

		final GlobalTravelReservationBasicHandler globalTravelReservationBasicHandler = new GlobalTravelReservationBasicHandler()
		{
			@Override
			public void handle(final AbstractOrderModel abstractOrderModel,
					final GlobalTravelReservationData globalTravelReservationData)
			{
				return;
			}
		};

		defaultCancelledGlobalTravelReservationPipelineManager
				.setHandlers(Stream.of(globalTravelReservationBasicHandler).collect(Collectors.toList()));
		final GlobalTravelReservationData globalTravelReservationData = defaultCancelledGlobalTravelReservationPipelineManager
				.executePipeline(abstractOrderModel);

		Assert.assertNotNull(globalTravelReservationData.getReservationData());
		Assert.assertNotNull(globalTravelReservationData.getAccommodationReservationData());
	}

	@Test
	public void testExecutePipelineWithReservationData()
	{
		given(abstractOrderModel.getEntries())
				.willReturn(Arrays.asList(transportAbstractOrderEntryModel, accommodationAbstractOrderEntryModel));
		given(bookingService.checkIfAnyOrderEntryByType(abstractOrderModel, OrderEntryType.TRANSPORT)).willReturn(true);
		given(bookingService.checkIfAnyOrderEntryByType(abstractOrderModel, OrderEntryType.ACCOMMODATION)).willReturn(true);
		given(abstractOrderModel.getTransportationOrderStatus()).willReturn(OrderStatus.ACTIVE);
		given(abstractOrderModel.getAccommodationOrderStatus()).willReturn(OrderStatus.ACTIVE);
		given(transportAbstractOrderEntryModel.getType()).willReturn(OrderEntryType.TRANSPORT);
		given(accommodationAbstractOrderEntryModel.getType()).willReturn(OrderEntryType.ACCOMMODATION);

		final GlobalTravelReservationBasicHandler globalTravelReservationBasicHandler = new GlobalTravelReservationBasicHandler()
		{
			@Override
			public void handle(final AbstractOrderModel abstractOrderModel,
					final GlobalTravelReservationData globalTravelReservationData)
			{
				return;
			}
		};
		defaultCancelledGlobalTravelReservationPipelineManager
				.setHandlers(Stream.of(globalTravelReservationBasicHandler).collect(Collectors.toList()));
		final GlobalTravelReservationData globalTravelReservationData = new GlobalTravelReservationData();
		defaultCancelledGlobalTravelReservationPipelineManager.executePipeline(abstractOrderModel, globalTravelReservationData);

		Assert.assertNotNull(globalTravelReservationData.getReservationData());
		Assert.assertNotNull(globalTravelReservationData.getAccommodationReservationData());
	}

}
