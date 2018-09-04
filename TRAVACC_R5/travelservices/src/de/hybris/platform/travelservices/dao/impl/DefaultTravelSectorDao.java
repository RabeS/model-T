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
import de.hybris.platform.servicelayer.search.SearchResult;
import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;
import de.hybris.platform.travelservices.dao.TravelSectorDao;
import de.hybris.platform.travelservices.model.travel.TransportFacilityModel;
import de.hybris.platform.travelservices.model.travel.TravelSectorModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;


/**
 * The type Default travel sector dao.
 */
public class DefaultTravelSectorDao extends DefaultGenericDao<TravelSectorModel> implements TravelSectorDao
{
	private static final Logger LOG = Logger.getLogger(DefaultTravelSectorDao.class);

	private static final String FIND_TRAVEL_SECTOR_BY_CODE =
			"SELECT {tr.pk} FROM { " + TravelSectorModel._TYPECODE + " AS tr JOIN "
					+ TransportFacilityModel._TYPECODE + " AS tfo ON {tr." + TravelSectorModel.ORIGIN + "}={tfo."
					+ TransportFacilityModel.PK + "} join " + TransportFacilityModel._TYPECODE + " AS tfd ON "
					+ "{tr." + TravelSectorModel.DESTINATION + "}={tfd." + TransportFacilityModel.PK
					+ "}} WHERE {tfo." + TransportFacilityModel.CODE + "} = ?originCode" + " AND {tfd."
					+ TransportFacilityModel.CODE + "} = ?destinationCode";

	public DefaultTravelSectorDao(final String typecode)
	{
		super(typecode);
	}

	@Override
	public TravelSectorModel findTravelSector(final TransportFacilityModel origin, final TransportFacilityModel destination)
	{
		validateParameterNotNull(origin, "Travel Sector origin must not be null!");
		validateParameterNotNull(destination, "Travel Sector destination must not be null!");
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put(TravelSectorModel.ORIGIN, origin);
		params.put(TravelSectorModel.DESTINATION, destination);
		final List<TravelSectorModel> travelSectorModel = find(params);
		if (CollectionUtils.isNotEmpty(travelSectorModel))
		{
			return travelSectorModel.get(0);
		}
		return null;
	}

	@Override
	public TravelSectorModel findTravelSectorByCodes(final String origin, final String destination)
	{
		validateParameterNotNull(origin, "Parameter origin cannot be null");
		validateParameterNotNull(destination, "Parameter destination cannot be null");

		final Map<String, Object> params = new HashMap<>();
		params.put("originCode", origin);
		params.put("destinationCode", destination);

		final SearchResult<TravelSectorModel> searchResult = getFlexibleSearchService().search(FIND_TRAVEL_SECTOR_BY_CODE, params);

		final List<TravelSectorModel> travelSectors = searchResult.getResult();

		if (CollectionUtils.isEmpty(travelSectors))
		{
			LOG.warn("No Sector found for origin " + origin + " and destination " + destination);
			return null;
		}
		if (CollectionUtils.size(travelSectors) > 1)
		{
			LOG.warn("Ambiguous Sector found for origin " + origin + " and destination " + destination);
			return null;
		}

		return travelSectors.get(0);
	}
}
