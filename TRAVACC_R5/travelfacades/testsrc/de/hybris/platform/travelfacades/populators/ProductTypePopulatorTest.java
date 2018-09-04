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

package de.hybris.platform.travelfacades.populators;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.travelservices.enums.ProductType;
import de.hybris.platform.travelservices.model.product.FareProductModel;

import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;


/**
 * unit test for {@link ProductTypePopulator}
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class ProductTypePopulatorTest
{
	@InjectMocks
	ProductTypePopulator productTypePopulator;

	@Mock
	private Map<String, ProductType> productTypeInstanceMap;

	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testProductTypePopulator()
	{

		final ProductModel productTypeModel = new ProductModel();
		productTypeModel.setProductType(ProductType.FARE_PRODUCT);
		final ProductData productData = new ProductData();

		Mockito.when(productTypePopulator.getProductTypeInstanceMap().get(FareProductModel.class.getSimpleName()))
				.thenReturn(ProductType.FARE_PRODUCT);
		productTypePopulator.populate(productTypeModel, productData);
		Assert.assertEquals(ProductType.FARE_PRODUCT.toString(), productData.getProductType());

	}
}