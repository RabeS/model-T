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
package de.hybris.platform.accommodationbackoffice.widgets.handler;

import de.hybris.platform.travelbackofficeservices.validator.TravelBackofficeCommonValidator;

import java.util.Map;

import org.springframework.beans.factory.annotation.Required;


/**
 * Abstract validation handler for accommodation backoffice
 */
public abstract class AbstractAccommodationBackofficeValidationHandler
{
	private TravelBackofficeCommonValidator travelBackofficeCommonValidator;

	protected boolean validateUniqueForAttributes(final Map<String, Object> attributes, final String typeCode)
	{
		return getTravelBackofficeCommonValidator().validateUniqueForAttributes(attributes, typeCode);
	}

	/**
	 * @return the travelBackofficeCommonValidator
	 */
	protected TravelBackofficeCommonValidator getTravelBackofficeCommonValidator()
	{
		return travelBackofficeCommonValidator;
	}

	/**
	 * @param travelBackofficeCommonValidator
	 * 		the travelBackofficeCommonValidator to set
	 */
	@Required
	public void setTravelBackofficeCommonValidator(
			final TravelBackofficeCommonValidator travelBackofficeCommonValidator)
	{
		this.travelBackofficeCommonValidator = travelBackofficeCommonValidator;
	}
}
