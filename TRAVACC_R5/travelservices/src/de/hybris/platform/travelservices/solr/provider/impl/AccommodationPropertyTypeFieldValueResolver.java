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

import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;
import de.hybris.platform.solrfacetsearch.provider.impl.ValueProviderParameterUtils;
import de.hybris.platform.travelservices.enums.AccommodationOfferingType;
import de.hybris.platform.travelservices.model.accommodation.MarketingRatePlanInfoModel;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

/**
 * The Accommodation property type field value resolver.
 */
public class AccommodationPropertyTypeFieldValueResolver extends AbstractValueResolver<MarketingRatePlanInfoModel, Object, Object>
{
	private static final String OPTIONAL_PARAM = "optional";
	private static final boolean OPTIONAL_PARAM_DEFAULT_VALUE = true;

	private EnumerationService enumerationService;

	@Override
	protected void addFieldValues(final InputDocument document, final IndexerBatchContext indexerBatchContext,
											final IndexedProperty indexedProperty, final MarketingRatePlanInfoModel marketingRatePlanInfoModel,
											final ValueResolverContext<Object, Object> resolverContext) throws FieldValueProviderException
	{
		if (marketingRatePlanInfoModel.getAccommodationOffering() == null)
		{
			return;
		}
		final List<AccommodationOfferingType> accommodationOfferingTypes = marketingRatePlanInfoModel.getAccommodationOffering().getTypes();

		if (CollectionUtils.isNotEmpty(accommodationOfferingTypes))
		{
			final List<String> accommodationOfferingTypeNames = accommodationOfferingTypes.stream()
					.map(AccommodationOfferingType::getCode).collect(Collectors.toList());

			document.addField(indexedProperty, accommodationOfferingTypeNames, resolverContext.getFieldQualifier());
			return;
		}

		final boolean isOptional = ValueProviderParameterUtils.getBoolean(indexedProperty, OPTIONAL_PARAM,
				OPTIONAL_PARAM_DEFAULT_VALUE);
		if (!isOptional)
		{
			throw new FieldValueProviderException("No value resolved for indexed property " + indexedProperty.getName());
		}
	}


	/**
	 * Gets enumeration service.
	 *
	 * @return the enumerationService
	 */
	protected EnumerationService getEnumerationService()
	{
		return enumerationService;
	}

	/**
	 * Sets enumeration service.
	 *
	 * @param enumerationService
	 * 		the enumerationService to set
	 */
	@Required
	public void setEnumerationService(final EnumerationService enumerationService)
	{
		this.enumerationService = enumerationService;
	}
}
