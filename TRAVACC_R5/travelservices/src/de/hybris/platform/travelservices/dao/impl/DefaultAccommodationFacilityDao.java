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

import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.travelservices.dao.AccommodationFacilityDao;
import de.hybris.platform.travelservices.model.facility.AccommodationFacilityModel;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

/**
 * Default implementation of {@link AccommodationFacilityDao}
 */
public class DefaultAccommodationFacilityDao extends DefaultGenericDao<AccommodationFacilityModel> implements
		AccommodationFacilityDao
{
	public DefaultAccommodationFacilityDao(final String typecode)
	{
		super(typecode);
	}

	@Override
	public AccommodationFacilityModel getAccommodationFacilityByCode(final String accommodationFacilityCode)
	{
		validateParameterNotNull(accommodationFacilityCode, "accommodationFacilityCode must not be null!");

		final List<AccommodationFacilityModel> accommodationFacilityModelList = find(
				Collections.singletonMap(AccommodationFacilityModel.CODE, (Object) accommodationFacilityCode));

		if (CollectionUtils.isEmpty(accommodationFacilityModelList))
		{
			throw new ModelNotFoundException("No result for the given accommodationFacilityCode");
		}
		else if (accommodationFacilityModelList.size() > 1)
		{
			throw new AmbiguousIdentifierException(
					"Found " + accommodationFacilityModelList.size() + " results for the given accommodationFacilityCode");
		}

		return accommodationFacilityModelList.stream().findFirst().orElse(null);
	}

}
