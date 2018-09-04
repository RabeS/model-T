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

package de.hybris.platform.travelservices.strategies.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.travelservices.stock.TravelCommerceStockService;
import de.hybris.platform.travelservices.strategies.cart.validation.impl.DefaultTravelCartValidationStrategy;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;;


/**
 * The type Default travel cart validation strategy test.
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultTravelCartValidationStrategyTest
{
	DefaultTravelCartValidationStrategy defaultTravelCartValidationStrategy;

	@Mock
	private TravelCommerceStockService commerceStockService;

	private CartEntryModel cartEntryModel;

	@Before
	public void setup()
	{
		defaultTravelCartValidationStrategy = new DefaultTravelCartValidationStrategy();
		defaultTravelCartValidationStrategy.setCommerceStockService(commerceStockService);
		cartEntryModel = new CartEntryModel();
	}

	@Test
	public void testGetStockLevel()
	{
		//		final Long longVal = defaultTravelCartValidationStrategy.getStockLevel(cartEntryModel);
		//		Assert.assertNotNull(longVal);
	}

}
