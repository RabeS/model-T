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

package de.hybris.platform.travelservices.services;

import de.hybris.platform.travelservices.model.accommodation.GuestOccupancyModel;

/**
 * Interface that exposes Guest Occupancy specific services
 */
public interface GuestOccupancyService
{
	/**
	 * Return the GuestOccupancyModel for given guestOccupancyCode
	 *
	 * @param guestOccupancyCode
	 * 		the guest occupancy code
	 *
	 * @return the GuestOccupancyModel
	 */
	GuestOccupancyModel getGuestOccupancy(String guestOccupancyCode);
}
