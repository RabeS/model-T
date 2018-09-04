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

package de.hybris.platform.travelfacades.facades;

import de.hybris.platform.commercefacades.travel.CabinClassData;

import java.util.List;


/**
 * Facade that exposes Cabin Class specific services
 */
public interface CabinClassFacade
{

	/**
	 * Facade which returns a list of CabinClassData data types
	 *
	 * @return List<CabinClassData> cabin classes
	 */
	List<CabinClassData> getCabinClasses();

	/**
	 * Returns the CabinClassData for the given bundleTemplate id.
	 *
	 * @param bundleTemplateId
	 * 		the bundleTemplate id.
	 *
	 * @return the CabinClassData.
	 */
	CabinClassData findCabinClassFromBundleTemplate(String bundleTemplateId);

}
