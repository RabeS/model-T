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

package de.hybris.platform.travelbackofficeservices.dao;

import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import java.util.Map;


/**
 * TravelBackofficeGenericDao interface which provides functionality to manage DB operations for backoffice wizards.
 */
public interface TravelBackofficeGenericDao
{

	/**
	 * This method retrieves the list of objects for the specified attributes and typeCode.
	 * Attributes is a Map<String, Object> where the key is the name of the attributes and the value is the value of that
	 * attribute.
	 *
	 * @param attributes
	 * 		the map of the name of the attribute and the value
	 * @param typeCode
	 * 		the typeCode
	 *
	 * @throws ModelNotFoundException
	 * 		if no items are found
	 * @throws AmbiguousIdentifierException
	 * 		if more than one item is found
	 *
	 * @return the object
	 */
	Object getModelForAttribute(Map<String, Object> attributes, String typeCode) throws ModelNotFoundException,
			AmbiguousIdentifierException;
}
