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

import de.hybris.platform.travelservices.model.facility.AccommodationFacilityModel;


/**
 * AccommodationFacilityDao interface which provides functionality to manage the AccommodationFacilityModel.
 */
public interface AccommodationFacilityDao
{

	/**
	 * Returns a {@link AccommodationFacilityModel} based on the accommodation facility code
	 *
	 * @param accommodationFacilityCode
	 * 		as the accommodation facility code
	 *
	 * @return the accommodation facility model
	 */
	AccommodationFacilityModel getAccommodationFacilityByCode(String accommodationFacilityCode);
}
