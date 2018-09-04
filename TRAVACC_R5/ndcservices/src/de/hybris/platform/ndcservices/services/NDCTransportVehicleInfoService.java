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
package de.hybris.platform.ndcservices.services;

import de.hybris.platform.travelservices.model.travel.TransportVehicleInfoModel;

/**
 * Interface that exposes TransportVehicleInfoModel through his ndcCode
 */
public interface NDCTransportVehicleInfoService
{
	/**
	 * Find a TransportVehicleInfoModel through his ndcCode
	 *
	 * @param ndcCode
	 * 	of the TransportVehicleInfoModel
	 * @return
	 * 	the TransportVehicleInfoModel with that ndcCode
	 */
	TransportVehicleInfoModel getTransportVehicle(String ndcCode);
}
