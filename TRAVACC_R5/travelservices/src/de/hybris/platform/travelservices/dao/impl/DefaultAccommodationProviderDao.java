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

import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.travelservices.dao.AccommodationProviderDao;
import de.hybris.platform.travelservices.model.accommodation.AccommodationProviderModel;

import java.util.Collections;
import java.util.Optional;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

/**
 * Default implementation of {@link AccommodationProviderDao}
 */
public class DefaultAccommodationProviderDao extends DefaultGenericDao<AccommodationProviderModel> implements AccommodationProviderDao
{
	/**
	 * @param typecode
	 */
	public DefaultAccommodationProviderDao(final String typecode)
	{
		super(typecode);
	}

	@Override
	public AccommodationProviderModel findAccommodationProviderForProviderCode(String accommodationProviderCode)
	{
		validateParameterNotNull(accommodationProviderCode, "accommodationProviderCode must not be null!");

		final Optional<AccommodationProviderModel> accommodationProviderModel = find(
				Collections.singletonMap(AccommodationProviderModel.CODE, (Object)  accommodationProviderCode)).stream().findFirst();

		return accommodationProviderModel.isPresent() ? accommodationProviderModel.get() : null;
	}
}
