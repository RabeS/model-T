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

package de.hybris.platform.travelservices.solr.provider.impl;

import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.provider.Qualifier;
import de.hybris.platform.travelservices.model.accommodation.MarketingRatePlanInfoModel;
import de.hybris.platform.travelservices.model.accommodation.RatePlanConfigModel;
import de.hybris.platform.travelservices.model.accommodation.RoomRateProductModel;
import de.hybris.platform.travelservices.utils.RatePlanUtils;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Resovles the price value of the RoomProduct(s) for given MarketingRatePlanInfo
 */
public class RoomRatePriceValueResolver extends AbstractPriceValueResolver
{
	@Override
	protected Map<String, Double> loadQualifierData(final IndexerBatchContext batchContext,
			final Collection<IndexedProperty> indexedProperties, final MarketingRatePlanInfoModel marketingRatePlanInfo,
			final Qualifier qualifier, final Date documentDate)
	{
		final Collection<RatePlanConfigModel> ratePlanConfigs = marketingRatePlanInfo.getRatePlanConfig();
		final Map<String, Double> priceInformationMap = new HashMap<>(ratePlanConfigs.size());

		ratePlanConfigs.forEach(ratePlanConfigModel -> {
			final RoomRateProductModel roomRateProduct = RatePlanUtils.getRoomRateForRatePlan(ratePlanConfigModel.getRatePlan(),
					documentDate);
			if (roomRateProduct != null)
			{
				final List<PriceInformation> priceInformations = loadPriceInformations(indexedProperties, roomRateProduct);
				Double roomRatePriceValue = getPriceValue(priceInformations);
				Double ratePlanConfigPrice = 0d;
				if (roomRatePriceValue != null)
				{
					// if net price, adding taxes to base price in order to be directly used when retrieving solr document
					if(getTravelCommercePriceService().isNetPricedProduct(roomRateProduct))
					{
						final Double roomRateTaxValue = getTaxes(roomRateProduct.getEurope1Taxes(), roomRatePriceValue);
						roomRatePriceValue += roomRateTaxValue;
					}
					ratePlanConfigPrice = roomRatePriceValue * ratePlanConfigModel.getQuantity();
				}
				priceInformationMap.put(ratePlanConfigModel.getCode(), ratePlanConfigPrice);
			}
		});

		return priceInformationMap;
	}

}
