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

package de.hybris.platform.travelfacades.strategies.impl;

import de.hybris.platform.commercefacades.travel.TravellerData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.travelfacades.facades.TravellerFacade;
import de.hybris.platform.travelfacades.strategies.SelectedAccommodationValidationStrategy;
import de.hybris.platform.travelservices.enums.AccommodationStatus;
import de.hybris.platform.travelservices.model.travel.SelectedAccommodationModel;
import de.hybris.platform.travelservices.services.accommodationmap.AccommodationMapService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * The type Default selected accommodation validation strategy.
 */
public class DefaultSelectedAccommodationValidationStrategy implements SelectedAccommodationValidationStrategy
{
	private AccommodationMapService accommodationMapService;
	private TravellerFacade travellerFacade;

	@Override
	public boolean validateSelectedAccommodation(final CartModel cartModel)
	{
		final List<AccommodationStatus> selectedAccomStatuses = new ArrayList<>();
		selectedAccomStatuses.add(AccommodationStatus.OCCUPIED);
		selectedAccomStatuses.add(AccommodationStatus.UNAVAILABLE);
		final List<OrderStatus> cancelledOrderStatuses = new ArrayList<>();
		cancelledOrderStatuses.add(OrderStatus.CANCELLED);
		cancelledOrderStatuses.add(OrderStatus.CANCELLING);

		if (CollectionUtils.isEmpty(cartModel.getSelectedAccommodations()))
		{
			return true;
		}
		final List<SelectedAccommodationModel> availableAccommodation = cartModel.getSelectedAccommodations().stream()
				.filter(selectedAccommodationModel -> getAccommodationMapService()
						.isAccommodationAvailableForBooking(selectedAccommodationModel.getConfiguredAccommodation(),
								selectedAccommodationModel.getTransportOffering().getCode(),
								getTravellerFacade().getTraveller(selectedAccommodationModel.getTraveller().getUid())))
				.collect(Collectors.toList());
		return availableAccommodation.size() == cartModel.getSelectedAccommodations().size();
	}

	/**
	 * Gets accommodation map service.
	 *
	 * @return the accommodation map service
	 */
	protected AccommodationMapService getAccommodationMapService()
	{
		return accommodationMapService;
	}

	/**
	 * Sets accommodation map service.
	 *
	 * @param accommodationMapService
	 * 		the accommodation map service
	 */
	@Required
	public void setAccommodationMapService(
			final AccommodationMapService accommodationMapService)
	{
		this.accommodationMapService = accommodationMapService;
	}

	/**
	 * Gets traveller facade.
	 *
	 * @return the traveller facade
	 */
	protected TravellerFacade getTravellerFacade()
	{
		return travellerFacade;
	}

	/**
	 * Sets traveller facade.
	 *
	 * @param travellerFacade
	 * 		the traveller facade
	 */
	@Required
	public void setTravellerFacade(final TravellerFacade travellerFacade)
	{
		this.travellerFacade = travellerFacade;
	}
}
