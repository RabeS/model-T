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

import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.travelservices.dao.GuestOccupancyDao;
import de.hybris.platform.travelservices.model.accommodation.GuestOccupancyModel;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;


/**
 * Default implementation of {@link GuestOccupancyDao}
 */
public class DefaultGuestOccupancyDao extends DefaultGenericDao<GuestOccupancyModel> implements GuestOccupancyDao
{

	public DefaultGuestOccupancyDao(final String typecode)
	{
		super(typecode);
	}

	@Override
	public GuestOccupancyModel getGuestOccupancyByCode(final String guestOccupancyCode)
	{
		validateParameterNotNull(guestOccupancyCode, "guestOccupancyCode must not be null!");

		final List<GuestOccupancyModel> guestOccupancyModelList = find(
				Collections.singletonMap(GuestOccupancyModel.CODE, (Object) guestOccupancyCode));

		if (CollectionUtils.isEmpty(guestOccupancyModelList))
		{
			throw new ModelNotFoundException("No result for the given guestOccupancyCode");
		}
		else if (guestOccupancyModelList.size() > 1)
		{
			throw new AmbiguousIdentifierException("Found " + guestOccupancyModelList.size() + " results for the given guestOccupancyCode");
		}

		return guestOccupancyModelList.stream().findFirst().orElse(null);
	}

}
