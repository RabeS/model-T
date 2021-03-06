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

package de.hybris.platform.travelfacades.strategies.impl;

import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.travel.AddToCartResponseData;
import de.hybris.platform.travelfacades.order.TravelCartFacade;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * unit test for Product availability validation strategy.
 */
@UnitTest
public class ProductAvailabilityValidationStrategyTest
{
	private ProductAvailabilityValidationStrategy productAvailabilityValidationStrategy;
	@Mock
	private TravelCartFacade cartFacade;
	@Mock
	private List<String> transportOfferingCodes;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		productAvailabilityValidationStrategy = new ProductAvailabilityValidationStrategy();
		productAvailabilityValidationStrategy.setCartFacade(cartFacade);
	}

	@Test
	public void testValidateAddToCart()
	{
		when(productAvailabilityValidationStrategy.getCartFacade().isProductAvailable(Matchers.anyString(),
				Matchers.anyListOf(String.class), Matchers.anyLong())).thenReturn(true);

		final AddToCartResponseData addProductToCartResponseData = productAvailabilityValidationStrategy
				.validateAddToCart("Test_Fare_Product", 1, "adult1", transportOfferingCodes, "LGW_CDG");

		Assert.assertNotNull(addProductToCartResponseData);
		Assert.assertEquals(addProductToCartResponseData.isValid(), true);
	}
}
