package de.hybris.platform.travelservices.dao;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PDTRowModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.internal.dao.GenericDao;


/**
 * Iterface for query operations on Price/Discount/Tax Rows
 */
public interface TravelPDTRowDao extends GenericDao<PDTRowModel>
{
	/**
	 * Retrieves the first available price for a given product to extract common pricing information
	 * @param product
	 * @return
	 */
	PriceRowModel findFirstAvailablePriceForProduct(ProductModel product);
}
