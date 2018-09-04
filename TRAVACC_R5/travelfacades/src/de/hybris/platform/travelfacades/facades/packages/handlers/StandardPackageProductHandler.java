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

import de.hybris.platform.commercefacades.packages.response.PackageProductData;
import de.hybris.platform.configurablebundlefacades.data.BundleTemplateData;

import java.util.List;


/**
 * Interface for Standard Package Product Handlers
 */
public interface StandardPackageProductHandler
{
	/**
	 * Handle method.
	 *
	 * @param bundleTemplate
	 * 		the bundle template data
	 * @param packageProductDataList
	 * 		the list of package product data list
	 */
	void handle(BundleTemplateData bundleTemplate, List<PackageProductData> packageProductDataList);
}
