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

package de.hybris.platform.travelbackofficeservices.services;

import de.hybris.platform.travelservices.model.TravelProductModel;

import java.util.List;

/**
 * The interface Travel product service.
 */
public interface TravelProductService
{

	/**
	 * Returns a list of TravelProductModel for a given AccommodationOfferingCode
	 * @param code
	 * @return
	 */
	List<TravelProductModel> getProductsForAccommodationOffering(String code);
}
