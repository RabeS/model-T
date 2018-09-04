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

package de.hybris.platform.travelservices.services.impl;

import de.hybris.platform.travelservices.dao.AccommodationFacilityDao;
import de.hybris.platform.travelservices.model.facility.AccommodationFacilityModel;
import de.hybris.platform.travelservices.services.AccommodationFacilityService;

import org.springframework.beans.factory.annotation.Required;

/**
 * Class is responsible for providing concrete implementation of the AccommodationFacilityService interface. The class uses the
 * AccommodationFacilityDao class to query the database and return the {@link AccommodationFacilityModel} corresponding to the
 * search parameters.
 */
public class DefaultAccommodationFacilityService implements AccommodationFacilityService
{
	private AccommodationFacilityDao accommodationFacilityDao;

	@Override
	public AccommodationFacilityModel getAccommodationFacility(final String accommodationFacilityCode)
	{
		return getAccommodationFacilityDao().getAccommodationFacilityByCode(accommodationFacilityCode);
	}

	/**
	 * @return accommodationFacilityDao
	 */
	protected AccommodationFacilityDao getAccommodationFacilityDao()
	{
		return accommodationFacilityDao;
	}

	/**
	 * @param accommodationFacilityDao
	 *           the accommodationFacilityDao to set
	 */
	@Required
	public void setAccommodationFacilityDao(final AccommodationFacilityDao accommodationFacilityDao)
	{
		this.accommodationFacilityDao = accommodationFacilityDao;
	}
}
