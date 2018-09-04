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

package de.hybris.platform.travelfacades.facades.accommodation;


import de.hybris.platform.commercefacades.travel.AccommodationPropertyTypeData;

import java.util.List;

/**
 * Facade that exposes the methods related to the property type of an accommodation and accommodationOffering
 */
public interface AccommodationOfferingTypeFacade
{
	/**
	 * Returns the list of all the availableServiceData for the reserved accommodation
	 *
	 * @return the list of all the available propertyTypes
	 */
	List<AccommodationPropertyTypeData> getAccommodationPropertyTypes();
}
