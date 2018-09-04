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

package de.hybris.platform.travelservices.services.impl;


import de.hybris.platform.travelservices.dao.AccommodationProviderDao;
import de.hybris.platform.travelservices.model.accommodation.AccommodationProviderModel;
import de.hybris.platform.travelservices.services.AccommodationProviderService;

import org.springframework.beans.factory.annotation.Required;

/**
 * Default implementation of {@link AccommodationProviderService}
 */
public class DefaultAccommodationProviderService implements AccommodationProviderService
{

	private AccommodationProviderDao accommodationProviderDao;

	@Override
	public AccommodationProviderModel getAccommodationProviderForProviderCode(String accommodationProviderCode)
	{
		return getAccommodationProviderDao().findAccommodationProviderForProviderCode(accommodationProviderCode);
	}

	/**
	 * @return the accommodationProviderDao
	 */
	protected AccommodationProviderDao getAccommodationProviderDao()
	{
		return accommodationProviderDao;
	}

	/**
	 * @param accommodationProviderDao
	 *           the accommodationProviderDao to set
	 */
	@Required
	public void setAccommodationProviderDao(AccommodationProviderDao accommodationProviderDao)
	{
		this.accommodationProviderDao = accommodationProviderDao;
	}
}
