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
import de.hybris.platform.solrfacetsearch.provider.FacetValueDisplayNameProvider;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.travelservices.enums.AccommodationOfferingType;
import de.hybris.platform.travelservices.model.facility.PropertyFacilityModel;
import de.hybris.platform.travelservices.services.AccommodationOfferingService;
import de.hybris.platform.travelservices.services.PropertyFacilityService;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;

/**
 * The type Accommodation Property Type facet value display name provider.
 */
public class AccommodationPropertyTypeFacetValueDisplayNameProvider implements FacetValueDisplayNameProvider {

	private EnumerationService enumerationService;

	@Override
	public String getDisplayName(final SearchQuery query, final IndexedProperty indexedProperty, final String accommodationPropertyTypeCode)
	{
		final List<AccommodationOfferingType> accommodationPropertyTypes = getEnumerationService().getEnumerationValues(AccommodationOfferingType._TYPECODE);

		for(AccommodationOfferingType accommodationPropertyType : accommodationPropertyTypes)
		{
			if (accommodationPropertyTypeCode.equalsIgnoreCase(accommodationPropertyType.getCode())) {
				return getEnumerationService().getEnumerationName(accommodationPropertyType);
			}
		}
		return accommodationPropertyTypeCode;
	}

	/**
	 * Gets enumeration service.
	 *
	 * @return the enumeration service
	 */
	protected EnumerationService getEnumerationService()
	{
		return enumerationService;
	}

	/**
	 * Sets enumeration service.
	 *
	 * @param enumerationService
	 *           the enumeration service
	 */
	@Required
	public void setEnumerationService(EnumerationService enumerationService)
	{
		this.enumerationService = enumerationService;
	}
}
