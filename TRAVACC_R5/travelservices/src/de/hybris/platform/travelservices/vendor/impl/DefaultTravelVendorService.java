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

package de.hybris.platform.travelservices.vendor.impl;

import de.hybris.platform.ordersplitting.model.VendorModel;
import de.hybris.platform.travelservices.vendor.TravelVendorService;
import de.hybris.platform.travelservices.vendor.dao.TravelVendorDao;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link TravelVendorService}
 */
public class DefaultTravelVendorService implements TravelVendorService
{

	private TravelVendorDao travelVendorDao;

	@Override
	public VendorModel getVendorByCode(final String code)
	{
		return getTravelVendorDao().getVendorByCode(code);
	}

	protected TravelVendorDao getTravelVendorDao()
	{
		return travelVendorDao;
	}

	@Required
	public void setTravelVendorDao(final TravelVendorDao travelVendorDao)
	{
		this.travelVendorDao = travelVendorDao;
	}

}
