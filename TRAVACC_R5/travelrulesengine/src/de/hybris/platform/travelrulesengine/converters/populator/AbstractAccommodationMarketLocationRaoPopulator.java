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

import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.travelrulesengine.dao.RuleTravelLocationDao;
import de.hybris.platform.travelservices.model.travel.LocationModel;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Abstract class that expose the functionalities for the Accommodation Market Location Rao Populator.
 * The market location is mocked and populated from the address saved against the userModel.
 */
public abstract class AbstractAccommodationMarketLocationRaoPopulator
{
	private RuleTravelLocationDao ruleTravelLocationDao;
	private UserService userService;

	/**
	 * Gets the set of market location codes for the given collection of addresses.
	 * It retrieves the town name first and search for a correspondence with the locations in the system. If nothing can be found,
	 * it searches for the country name and lastly for the region.
	 *
	 * @return the set of codes
	 */
	protected Set<String> getMarketLocations()
	{
		final Collection<AddressModel> addresses = getUserService().getCurrentUser().getAddresses();

		if(CollectionUtils.isEmpty(addresses))
		{
			return Collections.emptySet();
		}

		final String town = addresses.stream().map(AddressModel::getTown).findFirst().orElse(null);
		final Set<String> townLocations = getLocations(town);

		if (CollectionUtils.isNotEmpty(townLocations))
		{
			return townLocations;
		}

		final String country = addresses.stream().map(address -> address.getCountry().getName()).findFirst().orElse(null);
		final Set<String> countryLocations = getLocations(country);
		if (CollectionUtils.isNotEmpty(countryLocations))
		{
			return countryLocations;
		}

		final String region = addresses.stream().map(address -> address.getRegion().getName()).findFirst().orElse(null);
		final Set<String> regionLocations = getLocations(region);

		if (CollectionUtils.isNotEmpty(regionLocations))
		{
			return regionLocations;
		}

		return Collections.emptySet();
	}

	/**
	 * Get the location codes set for the given location name
	 *
	 * @param locationName
	 * 		the location name
	 *
	 * @return the location codes set
	 */
	protected Set<String> getLocations(final String locationName)
	{
		final Set<String> locationsHierarchyCodes = new HashSet<>();
		if (StringUtils.isNotBlank(locationName))
		{
			final List<LocationModel> locationModels = getLocationModels(locationName);
			if (CollectionUtils.isNotEmpty(locationModels))
			{
				populateLocationsFromLocation(locationModels.get(0), locationsHierarchyCodes);
			}
		}

		return locationsHierarchyCodes;
	}

	/**
	 * Populate locations from location.
	 *
	 * @param locationModel
	 * 		the location model
	 * @param locationHierarchySet
	 * 		the location hierarchy set
	 */
	protected void populateLocationsFromLocation(final LocationModel locationModel, final Set<String> locationHierarchySet)
	{
		if (Objects.isNull(locationModel))
		{
			return;
		}
		locationHierarchySet.add(locationModel.getCode());
		locationModel.getSuperlocations()
				.forEach(superLocation -> populateLocationsFromLocation(superLocation, locationHierarchySet));
	}

	/**
	 * Gets location model.
	 *
	 * @param locationName
	 * 		the location name
	 *
	 * @return the location model
	 */
	protected List<LocationModel> getLocationModels(final String locationName)
	{
		return getRuleTravelLocationDao().findLocationsByName(locationName);
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

	/**
	 * Gets user service.
	 *
	 * @return the user service
	 */
	protected UserService getUserService()
	{
		return userService;
	}

	/**
	 * Sets user service.
	 *
	 * @param userService
	 * 		the user service
	 */
	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}
}
