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
package de.hybris.platform.ndcfacades.context;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;


/**
 * The interface Ndcjaxb context.
 */
public interface NDCJAXBContext
{

	/**
	 * Retrieves the jaxb context for the NDC classes.
	 *
	 * @return the jaxb context
	 */
	JAXBContext getNDCJaxbContext() throws JAXBException;
}
