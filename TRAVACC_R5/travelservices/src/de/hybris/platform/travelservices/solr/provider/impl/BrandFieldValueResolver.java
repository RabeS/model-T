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

import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;
import de.hybris.platform.solrfacetsearch.provider.impl.ValueProviderParameterUtils;
import de.hybris.platform.travelservices.model.accommodation.AccommodationProviderModel;
import de.hybris.platform.travelservices.model.accommodation.MarketingRatePlanInfoModel;

import org.apache.commons.lang3.StringUtils;

/**
 * The type Brand field value resolver.
 */
public class BrandFieldValueResolver extends AbstractValueResolver<MarketingRatePlanInfoModel, Object, Object>
{
	private static final String OPTIONAL_PARAM = "optional";
	private static final boolean OPTIONAL_PARAM_DEFAULT_VALUE = true;

	@Override
	protected void addFieldValues(final InputDocument document, final IndexerBatchContext indexerBatchContext,
											final IndexedProperty indexedProperty, final MarketingRatePlanInfoModel marketingRatePlanInfoModel,
											final ValueResolverContext<Object, Object> resolverContext) throws FieldValueProviderException
	{
		AccommodationProviderModel accommodationProviderModel =  marketingRatePlanInfoModel.getAccommodationOffering().getProvider();

		if (accommodationProviderModel != null) {

			final String brandCode = accommodationProviderModel.getCode();
			if (StringUtils.isNotBlank(brandCode))
			{
				document.addField(indexedProperty, brandCode, resolverContext.getFieldQualifier());
				return;
			}
		}

		final boolean isOptional = ValueProviderParameterUtils.getBoolean(indexedProperty, OPTIONAL_PARAM,
				OPTIONAL_PARAM_DEFAULT_VALUE);
		if (!isOptional)
		{
			throw new FieldValueProviderException("No value resolved for indexed property " + indexedProperty.getName());
		}
	}
}
