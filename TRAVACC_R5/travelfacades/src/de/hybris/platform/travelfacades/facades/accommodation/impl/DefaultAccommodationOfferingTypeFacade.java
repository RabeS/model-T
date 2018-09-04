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

package de.hybris.platform.travelfacades.facades.accommodation.impl;

import de.hybris.platform.commercefacades.travel.AccommodationPropertyTypeData;
import de.hybris.platform.commercefacades.travel.ReasonForTravelData;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.travelfacades.facades.accommodation.AccommodationOfferingTypeFacade;
import de.hybris.platform.travelservices.enums.AccommodationOfferingType;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

/**
 * Default implementation of {@link AccommodationOfferingTypeFacade}
 */
public class DefaultAccommodationOfferingTypeFacade implements AccommodationOfferingTypeFacade
{
	private EnumerationService enumerationService;

	@Override
	public List<AccommodationPropertyTypeData> getAccommodationPropertyTypes()
	{
		final List<AccommodationOfferingType> accommodationPropertyTypes = getEnumerationService().getEnumerationValues(AccommodationOfferingType.class);
		final List<AccommodationPropertyTypeData> accommodationPropertyTypeList = new ArrayList<>();

		accommodationPropertyTypes.forEach(apt ->
		{
			final AccommodationPropertyTypeData accommodationPropertyTypeData = new AccommodationPropertyTypeData();
			accommodationPropertyTypeData.setCode(apt.getCode());
			accommodationPropertyTypeData.setName(getEnumerationService().getEnumerationName(apt));

			accommodationPropertyTypeList.add(accommodationPropertyTypeData);
		});
		return accommodationPropertyTypeList;
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
