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

package de.hybris.platform.travelfacades.facades.packages.manager;

import de.hybris.platform.commercefacades.packages.request.PackageRequestData;
import de.hybris.platform.commercefacades.packages.response.PackageResponseData;


/**
 * Interface for Package Details Pipeline Manager.
 */
public interface PackageDetailsPipelineManager
{

	/**
	 * Execute pipeline deal details response data.
	 *
	 * @param packageRequestData
	 *           the package request data
	 * @return the package response data
	 */
	PackageResponseData executePipeline(PackageRequestData packageRequestData);
}