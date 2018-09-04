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

package de.hybris.platform.travelservices.vendor;

import de.hybris.platform.ordersplitting.model.VendorModel;


/**
 * Service that exposes method related to {@link VendorModel}
 */
public interface TravelVendorService
{

	/**
	 * Method returns {@link VendorModel} for given code.
	 * 
	 * @param code
	 * @return VendorModel
	 */
	VendorModel getVendorByCode(String code);

}
