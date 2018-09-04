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
package de.hybris.platform.travelservices.dao;

import de.hybris.platform.configurablebundleservices.model.BundleTemplateStatusModel;


/**
 * Dao to retrieve BundleTemplatesStatus
 */
public interface TravelBundleTemplateStatusDao
{
	/**
	 * Returns the Bundle Template Status Model for the given id
	 *
	 * @param id
	 * 		as the bundle template status id.
	 *
	 * @return the bundle template status
	 */
	BundleTemplateStatusModel findBundleTemplateStatus(String id);
}
