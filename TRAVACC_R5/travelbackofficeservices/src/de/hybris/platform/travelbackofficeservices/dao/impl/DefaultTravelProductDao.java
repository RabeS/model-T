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

package de.hybris.platform.travelbackofficeservices.dao.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.travelbackofficeservices.dao.TravelProductDao;
import de.hybris.platform.travelservices.model.TravelProductModel;
import de.hybris.platform.travelservices.model.warehouse.AccommodationOfferingModel;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Default implementation of {@link TravelProductDao}
 */
public class DefaultTravelProductDao extends DefaultGenericDao<TravelProductModel> implements TravelProductDao
{
	private static final String ACCOMMODATION_OFFERING_CODE = "accommodationOfferingCode";

	private static final String FIND_PRODUCTS_BY_ACCOMMODATION_OFFERING_CODE = "SELECT DISTINCT {a." + TravelProductModel.PK
			+ "} FROM {" + TravelProductModel._TYPECODE + " AS a JOIN " + StockLevelModel._TYPECODE + " AS sl ON {sl."
			+ StockLevelModel.PRODUCTCODE + "} = {a." + TravelProductModel.CODE + "} JOIN " + AccommodationOfferingModel._TYPECODE
			+ " AS ao ON {sl." + StockLevelModel.WAREHOUSE + "} = {ao." + AccommodationOfferingModel.PK + "} } WHERE {ao."
			+ AccommodationOfferingModel.CODE + "} = ?accommodationOfferingCode";

	public DefaultTravelProductDao(final String typecode)
	{
		super(typecode);
	}

	@Override
	public List<TravelProductModel> getProductsForAccommodationOffering(final String code)
	{
		validateParameterNotNull(code, "AccommodationOfferingCode must not be null!");

		final Map<String, Object> params = new HashMap<String, Object>();
		params.put(ACCOMMODATION_OFFERING_CODE, code);

		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(FIND_PRODUCTS_BY_ACCOMMODATION_OFFERING_CODE,
				params);
		final SearchResult<TravelProductModel> searchResult = getFlexibleSearchService().search(flexibleSearchQuery);

		if (searchResult != null)
		{
			return searchResult.getResult();
		}
		return Collections.emptyList();
	}
}
