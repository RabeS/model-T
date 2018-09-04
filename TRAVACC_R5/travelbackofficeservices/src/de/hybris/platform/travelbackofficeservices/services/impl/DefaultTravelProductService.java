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

package de.hybris.platform.travelbackofficeservices.services.impl;

import de.hybris.platform.travelbackofficeservices.dao.TravelProductDao;
import de.hybris.platform.travelbackofficeservices.services.TravelProductService;
import de.hybris.platform.travelservices.model.TravelProductModel;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link TravelProductService}
 */
public class DefaultTravelProductService implements TravelProductService
{
	TravelProductDao travelProductDao;

	@Override
	public List<TravelProductModel> getProductsForAccommodationOffering(String code)
	{
		return getTravelProductDao().getProductsForAccommodationOffering(code);
	}

	/**
	 * @return
	 */
	protected TravelProductDao getTravelProductDao()
	{
		return travelProductDao;
	}

	/**
	 * @param travelProductDao
	 */
	@Required
	public void setTravelProductDao(TravelProductDao travelProductDao)
	{
		this.travelProductDao = travelProductDao;
	}

}
