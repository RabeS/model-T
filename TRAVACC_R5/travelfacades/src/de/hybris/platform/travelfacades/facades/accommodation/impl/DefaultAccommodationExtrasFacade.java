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

package de.hybris.platform.travelfacades.facades.accommodation.impl;

import de.hybris.platform.commercefacades.accommodation.AccommodationReservationData;
import de.hybris.platform.commercefacades.accommodation.AvailableServiceData;
import de.hybris.platform.commercefacades.accommodation.ReservedRoomStayData;
import de.hybris.platform.commercefacades.accommodation.ServiceData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.travelfacades.facades.accommodation.AccommodationExtrasFacade;
import de.hybris.platform.travelfacades.facades.accommodation.manager.AccommodationServicePipelineManager;
import de.hybris.platform.travelrulesengine.services.TravelRulesService;
import de.hybris.platform.travelservices.services.AccommodationExtrasService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.BooleanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of the {@link AccommodationExtrasFacade}
 */
public class DefaultAccommodationExtrasFacade implements AccommodationExtrasFacade
{
	private static final Logger LOGGER = Logger.getLogger(DefaultAccommodationExtrasFacade.class);

	private AccommodationExtrasService accommodationExtrasService;
	private AccommodationServicePipelineManager accommodationServicePipelineManager;
	private TravelRulesService travelRulesService;

	@Override
	public List<AvailableServiceData> getAvailableServices(final AccommodationReservationData reservationData)
	{
		final String accommodationOfferingCode = reservationData.getAccommodationReference().getAccommodationOfferingCode();

		final List<ProductModel> extraServices = getAccommodationExtrasService()
				.getExtrasForAccommodationOffering(accommodationOfferingCode);

		return reservationData.getRoomStays().stream()
				.map(roomStay -> createAvailableServiceData(reservationData, extraServices, roomStay)).collect(Collectors.toList());
	}

	/**
	 * Create available service data available service data.
	 *
	 * @param reservationData
	 * 		the reservation data
	 * @param extraServices
	 * 		the extra services
	 * @param roomStay
	 * 		the room stay
	 * @return the available service data
	 */
	protected AvailableServiceData createAvailableServiceData(final AccommodationReservationData reservationData,
			final List<ProductModel> extraServices, final ReservedRoomStayData roomStay)
	{
		final AvailableServiceData availableService = new AvailableServiceData();
		availableService.setRoomStayRefNumber(roomStay.getRoomStayRefNumber());

		final List<ServiceData> services = new ArrayList<>();
		extraServices.forEach(extraService -> {
			final ServiceData serviceData = getAccommodationServicePipelineManager()
					.executePipeline(extraService, roomStay, reservationData);
			if (serviceData != null)
			{
				services.add(serviceData);
			}
		});

		final List<ReservedRoomStayData> roomStays = reservationData.getRoomStays();
		reservationData.setRoomStays(Collections.singletonList(roomStay));

		showExtraProducts(reservationData, services);
		reservationData.setRoomStays(roomStays);
		availableService.setServices(services);
		return availableService;
	}

	/**
	 * Show extra products.
	 *
	 * @param reservationData
	 * 		the reservation data
	 * @param services
	 * 		the services
	 */
	protected void showExtraProducts(final AccommodationReservationData reservationData, final List<ServiceData> services)
	{
		try
		{
			final List<String> returnedService = getTravelRulesService().showExtraProducts(reservationData);
			discardExtraService(returnedService, services);
		}
		catch (final NullPointerException e)
		{
			LOGGER.debug("No reservation Data: " + reservationData + "provided", e);
		}
	}

	/**
	 * Discard extra service.
	 *
	 * @param returnedExtraService
	 * 		the returned extra products
	 * @param services
	 * 		the services
	 */
	protected void discardExtraService(final List<String> returnedExtraService, final List<ServiceData> services)
	{
		final List<ServiceData> servicesToRemove = services.stream().filter(
				service -> BooleanUtils.isNotTrue(service.getServiceDetails().getProduct().isIgnoreRules()) && !returnedExtraService
						.contains(service.getCode())).collect(Collectors.toList());
		services.removeAll(servicesToRemove);
	}

	/**
	 * Gets accommodation extras service.
	 *
	 * @return the accommodationExtrasService
	 */
	protected AccommodationExtrasService getAccommodationExtrasService()
	{
		return accommodationExtrasService;
	}

	/**
	 * Sets accommodation extras service.
	 *
	 * @param accommodationExtrasService
	 * 		the accommodationExtrasService to set
	 */
	@Required
	public void setAccommodationExtrasService(final AccommodationExtrasService accommodationExtrasService)
	{
		this.accommodationExtrasService = accommodationExtrasService;
	}

	/**
	 * Gets accommodation service pipeline manager.
	 *
	 * @return the accommodationServicePipelineManager
	 */
	protected AccommodationServicePipelineManager getAccommodationServicePipelineManager()
	{
		return accommodationServicePipelineManager;
	}

	/**
	 * Sets accommodation service pipeline manager.
	 *
	 * @param accommodationServicePipelineManager
	 * 		the accommodationServicePipelineManager to set
	 */
	@Required
	public void setAccommodationServicePipelineManager(
			final AccommodationServicePipelineManager accommodationServicePipelineManager)
	{
		this.accommodationServicePipelineManager = accommodationServicePipelineManager;
	}

	/**
	 * Gets travel rules service.
	 *
	 * @return the travel rules service
	 */
	protected TravelRulesService getTravelRulesService()
	{
		return travelRulesService;
	}

	/**
	 * Sets travel rules service.
	 *
	 * @param travelRulesService
	 * 		the travel rules service
	 */
	@Required
	public void setTravelRulesService(final TravelRulesService travelRulesService)
	{
		this.travelRulesService = travelRulesService;
	}
}

