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

package de.hybris.platform.travelrulesengine.converters.populator;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ruleengineservices.converters.populator.ProductRaoPopulator;
import de.hybris.platform.ruleengineservices.rao.ProductRAO;
import de.hybris.platform.travelservices.enums.ProductType;
import de.hybris.platform.travelservices.model.product.FareProductModel;


public class TravelProductRaoPopulator extends ProductRaoPopulator
{
	@Override
	public void populate(final ProductModel source, final ProductRAO target)
	{
		super.populate(source, target);
		if (source instanceof FareProductModel)
		{
			target.setProductType(ProductType.FARE_PRODUCT);
		}
	}
}
