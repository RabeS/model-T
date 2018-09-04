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

package de.hybris.platform.travelservices.dao;

import de.hybris.platform.travelservices.model.accommodation.GuestOccupancyModel;

/**
 * GuestOccupancyDao interface which provides functionality to manage the GuestOccupancyModel.
 */
public interface GuestOccupancyDao
{
	/**
	 * Returns a {@link GuestOccupancyModel} based on the guest occupancy code
	 *
	 * @param guestOccupancyCode
	 * 		as the guest occupancy code
	 *
	 * @return the guest occupancy model
	 */
	GuestOccupancyModel getGuestOccupancyByCode(String guestOccupancyCode);

}
