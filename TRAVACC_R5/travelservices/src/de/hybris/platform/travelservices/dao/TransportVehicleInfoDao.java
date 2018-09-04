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

import de.hybris.platform.travelservices.model.travel.TransportVehicleInfoModel;


/**
 * This interface find and return transport vehicle info
 */
public interface TransportVehicleInfoDao
{

	/**
	 * This API find and return transport vehicle info based on transport vehicle info code
	 *
	 * @param transportVehicleInfoCode
	 * 		the transport vehicle info code
	 * @return transport vehicle info model
	 */
	TransportVehicleInfoModel findTranportVehicleInfo(String transportVehicleInfoCode);
}
