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

package de.hybris.platform.travelfacades.facades.packages.handlers;

import de.hybris.platform.commercefacades.packages.request.PackageRequestData;
import de.hybris.platform.commercefacades.packages.response.PackageResponseData;


/**
 * Interface for Package Response Handlers
 */
public interface PackageResponseHandler
{
	/**
	 * Handle method.
	 *
	 * @param packageResponseData
	 * 		as the packageResponseData
	 * @param packageRequestData
	 * 		as the packageRequestData
	 */
	void handle(PackageRequestData packageRequestData, PackageResponseData packageResponseData);
}
