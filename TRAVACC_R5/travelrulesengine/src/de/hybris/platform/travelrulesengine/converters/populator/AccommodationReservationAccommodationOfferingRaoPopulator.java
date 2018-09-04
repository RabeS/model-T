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

package de.hybris.platform.travelrulesengine.converters.populator;

import de.hybris.platform.commercefacades.accommodation.AccommodationReservationData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.travelrulesengine.dao.RuleTravelLocationDao;
import de.hybris.platform.travelrulesengine.rao.AccommodationOfferingRAO;
import de.hybris.platform.travelrulesengine.rao.AccommodationProviderRAO;
import de.hybris.platform.travelrulesengine.rao.AccommodationReservationRAO;
import de.hybris.platform.travelservices.model.travel.LocationModel;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;


/**
 * The type Accommodation reservation accommodation rao populator.
 */
public class AccommodationReservationAccommodationOfferingRaoPopulator
		implements Populator<AccommodationReservationData, AccommodationReservationRAO>
{
	private RuleTravelLocationDao ruleTravelLocationDao;

	@Override
	public void populate(final AccommodationReservationData source, final AccommodationReservationRAO target)
			throws ConversionException
	{
		if (Objects.isNull(source))
		{
			return;
		}

		final AccommodationOfferingRAO accommodationOfferingRAO = new AccommodationOfferingRAO();

		accommodationOfferingRAO.setCode(source.getAccommodationReference().getAccommodationOfferingCode());

		final Set<String> accommodationDestinationLocations = new HashSet<>();
		final LocationModel locationModel = getRuleTravelLocationDao()
				.findLocation(source.getAccommodationReference().getLocation().getCode());
		populateLocationsFromLocation(locationModel, accommodationDestinationLocations);
		accommodationOfferingRAO.setLocations(accommodationDestinationLocations);

		final Set<String> accommodationOfferingTypes = new HashSet<>(source.getAccommodationReference().getAccommodationOfferingTypes());
		accommodationOfferingRAO.setAccommodationOfferingTypes(accommodationOfferingTypes);

		final AccommodationProviderRAO accommodationProviderRAO = new AccommodationProviderRAO();
		accommodationProviderRAO.setCode(source.getAccommodationReference().getBrandCode());
		accommodationOfferingRAO.setProvider(accommodationProviderRAO);

		target.setAccommodationOffering(accommodationOfferingRAO);
	}

	/**
	 * Populate locations from location.
	 *
	 * @param locationModel
	 * 		the location model
	 * @param locationSet
	 * 		the location set
	 */
	protected void populateLocationsFromLocation(final LocationModel locationModel, final Set<String> locationSet)
	{
		if (Objects.isNull(locationModel))
		{
			return;
		}
		locationSet.add(locationModel.getCode());
		locationModel.getSuperlocations().forEach(superLocation -> populateLocationsFromLocation(superLocation, locationSet));
	}

	/**
	 * Gets rule travel location dao.
	 *
	 * @return the rule travel location dao
	 */
	protected RuleTravelLocationDao getRuleTravelLocationDao()
	{
		return ruleTravelLocationDao;
	}

	/**
	 * Sets rule travel location dao.
	 *
	 * @param ruleTravelLocationDao
	 * 		the rule travel location dao
	 */
	@Required
	public void setRuleTravelLocationDao(final RuleTravelLocationDao ruleTravelLocationDao)
	{
		this.ruleTravelLocationDao = ruleTravelLocationDao;
	}
}
