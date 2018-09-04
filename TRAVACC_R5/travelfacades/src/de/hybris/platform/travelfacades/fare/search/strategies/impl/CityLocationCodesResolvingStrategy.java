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

import de.hybris.platform.travelfacades.fare.search.strategies.LocationCodesResolvingStrategy;
import de.hybris.platform.travelservices.model.travel.LocationModel;
import de.hybris.platform.travelservices.model.travel.TransportFacilityModel;
import de.hybris.platform.travelservices.services.TravelLocationService;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Strategy returning the list of transport facility codes belonging to the city passed as a parameter
 */
public class CityLocationCodesResolvingStrategy implements LocationCodesResolvingStrategy
{

	private TravelLocationService travelLocationService;

	@Override
	public List<String> getLocationCodes(final String locationParameter)
	{
		final LocationModel locationModel = getTravelLocationService().getLocation(locationParameter);
		if (Objects.isNull(locationModel) || CollectionUtils.isEmpty(locationModel.getTransportFacility()))
		{
			return Collections.emptyList();
		}
		return locationModel.getTransportFacility().stream().map(TransportFacilityModel::getCode).collect(Collectors.toList());
	}

	/**
	 * @return travelLocationService
	 */
	protected TravelLocationService getTravelLocationService()
	{
		return travelLocationService;
	}

	/**
	 * @param travelLocationService
	 * 		the travelLocationService to set
	 */
	@Required
	public void setTravelLocationService(final TravelLocationService travelLocationService)
	{
		this.travelLocationService = travelLocationService;
	}


}
