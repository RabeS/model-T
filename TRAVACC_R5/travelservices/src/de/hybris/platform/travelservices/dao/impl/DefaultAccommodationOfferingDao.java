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

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.travelservices.dao.AccommodationOfferingDao;
import de.hybris.platform.travelservices.model.product.AccommodationModel;
import de.hybris.platform.travelservices.model.warehouse.AccommodationOfferingModel;

import org.apache.commons.collections.CollectionUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;


/**
 * Implementation of the DAO on Accommodation Offering model objects. Default implementation of the
 * {@link de.hybris.platform.travelservices.dao.AccommodationOfferingDao} interface.
 */
public class DefaultAccommodationOfferingDao extends DefaultGenericDao<AccommodationOfferingModel>
		implements AccommodationOfferingDao
{
	private static final String FIND_ACCOMMODATION_OFFERINGS = "SELECT {ao." + AccommodationOfferingModel.PK + "} FROM {"
			+ AccommodationOfferingModel._TYPECODE + " AS ao}";

	private static final String FIND_BY_CATALOG_ID =
			"SELECT DISTINCT {ao." + AccommodationOfferingModel.PK + "} " + "FROM {" + AccommodationOfferingModel._TYPECODE
					+ " AS ao " + "JOIN " + StockLevelModel._TYPECODE + " AS sl ON {sl." + StockLevelModel.WAREHOUSE + "} = {ao."
					+ AccommodationOfferingModel.PK + "} " + "JOIN " + AccommodationModel._TYPECODE + " AS a ON {sl."
					+ StockLevelModel.PRODUCTCODE + "} = {a." + AccommodationModel.CODE + "} " + "JOIN "
					+ CatalogVersionModel._TYPECODE + " AS cv ON {a." + AccommodationModel.CATALOGVERSION + "} = {cv."
					+ CatalogVersionModel.PK + "} " + "JOIN " + CatalogModel._TYPECODE + " AS c ON {cv." + CatalogVersionModel.CATALOG
					+ "} = {c." + CatalogModel.PK + "} " + "} " + "WHERE {c." + CatalogModel.ID + "} = ?" + CatalogModel.ID;

	/**
	 * @param typecode
	 */
	public DefaultAccommodationOfferingDao(final String typecode)
	{
		super(typecode);
	}

	@Override
	public List<AccommodationOfferingModel> findAccommodationOfferings()
	{
		return find();
	}

	@Override
	public SearchResult<AccommodationOfferingModel> findAccommodationOfferings(final int batchSize, final int offset)
	{
		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(FIND_ACCOMMODATION_OFFERINGS);
		flexibleSearchQuery.setCount(batchSize);
		flexibleSearchQuery.setStart(offset);
		flexibleSearchQuery.setNeedTotal(true);

		final SearchResult<AccommodationOfferingModel> searchResult = getFlexibleSearchService().search(flexibleSearchQuery);
		return searchResult;
	}

	@Override
	public AccommodationOfferingModel findAccommodationOffering(final String code)
	{
		validateParameterNotNull(code, "Accommodation Offering Code must not be null!");

		final List<AccommodationOfferingModel> accommodationOfferings = find(
				Collections.singletonMap(AccommodationOfferingModel.CODE, (Object) code));
		if (CollectionUtils.isEmpty(accommodationOfferings))
		{
			throw new ModelNotFoundException("No result for the given query");
		}
		else if (accommodationOfferings.size() > 1)
		{
			throw new AmbiguousIdentifierException("Found " + accommodationOfferings.size() + " results for the given query");
		}
		final Optional<AccommodationOfferingModel> accommodationOfferingModel = accommodationOfferings.stream().findFirst();
		return accommodationOfferingModel.orElse(null);
	}

	@Override
	public SearchResult<AccommodationOfferingModel> findAccommodationOfferings(final int batchSize, final int offset,
			final String catalogId)
	{
		validateParameterNotNull(catalogId, "Catalog Id must not be null!");

		final Map<String, Object> params = new HashMap<>();
		params.put(CatalogModel.ID, catalogId);

		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(FIND_BY_CATALOG_ID, params);
		flexibleSearchQuery.setCount(batchSize);
		flexibleSearchQuery.setStart(offset);
		flexibleSearchQuery.setNeedTotal(true);

		return getFlexibleSearchService().search(flexibleSearchQuery);
	}

}
