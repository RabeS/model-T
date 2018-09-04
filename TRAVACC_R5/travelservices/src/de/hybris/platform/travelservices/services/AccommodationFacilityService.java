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

import de.hybris.platform.travelservices.model.facility.AccommodationFacilityModel;

/**
 * Interface that exposes Accommodation Facility specific services
 */
public interface AccommodationFacilityService
{
	/**
	 * Return the AccommodationFacilityModel for given accommodationFacilityCode
	 *
	 * @param accommodationFacilityCode
	 * 		the accommodation facility code
	 *
	 * @return the AccommodationFacilityModel
	 */
	AccommodationFacilityModel getAccommodationFacility(String accommodationFacilityCode);
}
