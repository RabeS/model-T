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

package de.hybris.platform.travelservices.dao.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.travelservices.dao.TransportVehicleInfoDao;
import de.hybris.platform.travelservices.model.travel.TransportVehicleInfoModel;

import java.util.Collections;


/**
 * The type Default transport vehicle info dao.
 */
public class DefaultTransportVehicleInfoDao extends DefaultGenericDao<TransportVehicleInfoModel>implements TransportVehicleInfoDao
{

	public DefaultTransportVehicleInfoDao(final String typecode)
	{
		super(typecode);
	}

	@Override
	public TransportVehicleInfoModel findTranportVehicleInfo(final String transportVehicleInfoCode)
	{
		validateParameterNotNull(transportVehicleInfoCode, "Transport Vehicle Info Code must not be null!");

		return find(Collections.singletonMap(TransportVehicleInfoModel.CODE, (Object) transportVehicleInfoCode)).stream()
				.findFirst().get();
	}

}
