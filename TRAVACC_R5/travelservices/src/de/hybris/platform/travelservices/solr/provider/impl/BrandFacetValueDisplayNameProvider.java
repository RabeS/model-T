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
import de.hybris.platform.solrfacetsearch.provider.FacetValueDisplayNameProvider;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.travelservices.model.accommodation.AccommodationProviderModel;
import de.hybris.platform.travelservices.services.AccommodationProviderService;

import org.springframework.beans.factory.annotation.Required;

/**
 * The type Brand facet value display name provider.
 */
public class BrandFacetValueDisplayNameProvider implements FacetValueDisplayNameProvider
{

	private AccommodationProviderService accommodationProviderService;

	@Override
	public String getDisplayName(final SearchQuery query, final IndexedProperty indexedProperty, final String brandCode)
	{
		final AccommodationProviderModel accommodationProviderModel = getAccommodationProviderService().getAccommodationProviderForProviderCode(brandCode);
		if (accommodationProviderModel != null)
		{
			return accommodationProviderModel.getName();
		}
		return brandCode;
	}

	/**
	 * @return the accommodationProviderService
	 */
	protected AccommodationProviderService getAccommodationProviderService()
	{
		return accommodationProviderService;
	}

	/**
	 * @param accommodationProviderService
	 *           the accommodationProviderService to set
	 */
	@Required
	public void setAccommodationProviderService(AccommodationProviderService accommodationProviderService)
	{
		this.accommodationProviderService = accommodationProviderService;
	}
}
