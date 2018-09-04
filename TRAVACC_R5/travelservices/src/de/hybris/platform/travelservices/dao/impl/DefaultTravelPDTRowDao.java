package de.hybris.platform.travelservices.dao.impl;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PDTRowModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.travelservices.dao.TravelPDTRowDao;

import java.util.HashMap;
import java.util.Map;


/**
 * Concrete implementation of {@link TravelPDTRowDao}
 */
public class DefaultTravelPDTRowDao extends DefaultGenericDao<PDTRowModel> implements TravelPDTRowDao
{
	private static final String FIND_ALL_PRICES_FOR_PRODUCT =
			"SELECT {pr." + PriceRowModel.PK + "} "
					+ "FROM {" + PriceRowModel._TYPECODE
					+ " AS pr} WHERE {pr." + PriceRowModel.PRODUCT + "}=?product";

	public DefaultTravelPDTRowDao(final String typecode)
	{
		super(typecode);
	}

	@Override
	public PriceRowModel findFirstAvailablePriceForProduct(final ProductModel product)
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("product", product);
		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(FIND_ALL_PRICES_FOR_PRODUCT, params);
		searchQuery.setCount(1);

		final SearchResult<PriceRowModel> searchResult = getFlexibleSearchService().search(searchQuery);

		return searchResult.getResult().stream().findFirst().orElse(null);
	}
}
