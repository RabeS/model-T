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

import de.hybris.platform.travelservices.dao.GuestOccupancyDao;
import de.hybris.platform.travelservices.model.accommodation.GuestOccupancyModel;
import de.hybris.platform.travelservices.services.GuestOccupancyService;

import org.springframework.beans.factory.annotation.Required;

/**
 * Class is responsible for providing concrete implementation of the GuestOccupancyService interface. The class uses the
 * GuestOccupancyDao class to query the database and return the {@link GuestOccupancyModel} corresponding to the search parameters.
 */
public class DefaultGuestOccupancyService implements GuestOccupancyService
{

	private GuestOccupancyDao guestOccupancyDao;

	@Override
	public GuestOccupancyModel getGuestOccupancy(final String guestOccupancyCode)
	{
		return getGuestOccupancyDao().getGuestOccupancyByCode(guestOccupancyCode);
	}

	/**
	 * @return guestOccupancyDao
	 */
	protected GuestOccupancyDao getGuestOccupancyDao()
	{
		return guestOccupancyDao;
	}

	/**
	 * @param guestOccupancyDao
	 *           the guestOccupancyDao to set
	 */
	@Required
	public void setGuestOccupancyDao(final GuestOccupancyDao guestOccupancyDao)
	{
		this.guestOccupancyDao = guestOccupancyDao;
	}
}
