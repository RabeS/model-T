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
package de.hybris.platform.travelbackofficeservices.validator;

import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.travelbackofficeservices.dao.TravelBackofficeGenericDao;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Validator to provide functionality for custom validation for the backoffice wizards.
 */
public class TravelBackofficeCommonValidator
{
	private static final Logger LOG = Logger.getLogger(TravelBackofficeCommonValidator.class);

	private TravelBackofficeGenericDao travelBackofficeGenericDao;

	/**
	 * This method checks if a model of type corresponding to the typeCode for the specified attributes.
	 * Attributes is a Map<String, Object> where the key is the name of the attributes and the value is the value of that
	 * attribute.
	 * If a ModelNotFoundException is caught, it means that no items exist, so the validation result is true; if no exceptions are
	 * caught, it means that at least one model exists, so the validation result is false.
	 *
	 * @param attributes
	 * 		the map of the name of the attribute and the value
	 * @param typeCode
	 * 		the typeCode
	 *
	 * @return true if the validation is successful, false otherwise
	 */
	public boolean validateUniqueForAttributes(final Map<String, Object> attributes, final String typeCode)
	{
		try
		{
			getTravelBackofficeGenericDao().getModelForAttribute(attributes, typeCode);
		}
		catch (final ModelNotFoundException ex)
		{
			LOG.debug(ex);
			return Boolean.TRUE;
		}
		catch (final AmbiguousIdentifierException ex)
		{
			LOG.debug(ex);
		}

		return Boolean.FALSE;
	}

	/**
	 * @return the travelBackofficeGenericDao
	 */
	protected TravelBackofficeGenericDao getTravelBackofficeGenericDao()
	{
		return travelBackofficeGenericDao;
	}

	/**
	 * @param travelBackofficeGenericDao
	 * 		the travelBackofficeGenericDao to set
	 */
	@Required
	public void setTravelBackofficeGenericDao(final TravelBackofficeGenericDao travelBackofficeGenericDao)
	{
		this.travelBackofficeGenericDao = travelBackofficeGenericDao;
	}

}
