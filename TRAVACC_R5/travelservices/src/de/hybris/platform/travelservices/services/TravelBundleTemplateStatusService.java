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
package de.hybris.platform.travelservices.services;

import de.hybris.platform.configurablebundleservices.model.BundleTemplateStatusModel;


/**
 * Service that exposes the methods related to the bundle template status
 */
public interface TravelBundleTemplateStatusService
{
	/**
	 * Returns the bundle template status for the given statusId
	 *
	 * @param statusId
	 * 		as the status id
	 *
	 * @return the bundle template status
	 */
	BundleTemplateStatusModel getBundleTemplateStatusForId(String statusId);
}
