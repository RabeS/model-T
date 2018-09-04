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

import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;
import de.hybris.platform.solrfacetsearch.provider.impl.ValueProviderParameterUtils;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.travelservices.model.accommodation.MarketingRatePlanInfoModel;

import java.util.Iterator;

import org.apache.commons.lang.StringUtils;


/**
 * Resolves value of contact number of indexed AccommodationOffering
 */
public class ContactNumberValueResolver extends AbstractValueResolver<MarketingRatePlanInfoModel, Object, Object>
{
	private static final String OPTIONAL_PARAM = "optional";
	private static final boolean OPTIONAL_PARAM_DEFAULT_VALUE = true;

	@Override
	protected void addFieldValues(final InputDocument document, final IndexerBatchContext indexerBatchContext,
			final IndexedProperty indexedProperty, final MarketingRatePlanInfoModel marketingRatePlanInfoModel,
			final ValueResolverContext<Object, Object> resolverContext) throws FieldValueProviderException
	{
		final Iterator<PointOfServiceModel> posIterator = marketingRatePlanInfoModel.getAccommodationOffering().getLocation()
				.getPointOfService().iterator();
		if (posIterator.hasNext())
		{
			final AddressModel address = posIterator.next().getAddress();
			if (address != null && StringUtils.isNotEmpty(address.getPhone1()))
			{
				document.addField(indexedProperty, address.getPhone1(), resolverContext.getFieldQualifier());
				return;
			}
		}

		final boolean isOptional = ValueProviderParameterUtils
				.getBoolean(indexedProperty, OPTIONAL_PARAM, OPTIONAL_PARAM_DEFAULT_VALUE);
		if (!isOptional)
		{
			throw new FieldValueProviderException("No value resolved for indexed property " + indexedProperty.getName());
		}

	}
}
