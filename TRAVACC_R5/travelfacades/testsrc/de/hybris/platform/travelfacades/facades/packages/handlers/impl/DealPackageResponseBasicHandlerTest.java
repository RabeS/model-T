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

package de.hybris.platform.travelfacades.facades.packages.handlers.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.packages.request.PackageRequestData;
import de.hybris.platform.commercefacades.packages.response.PackageResponseData;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;


/**
 * unit test for Deal package response basic handler.
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DealPackageResponseBasicHandlerTest
{
	@InjectMocks
	DealPackageResponseBasicHandler dealPackageResponseBasicHandler;

	@Test
	public void testHandle()
	{
		final PackageRequestData packageRequestData = new PackageRequestData();
		final PackageResponseData packageResponseData = new PackageResponseData();
		packageRequestData.setStartingDatePattern("dd/MM/yyyy");

		dealPackageResponseBasicHandler.handle(packageRequestData, packageResponseData);
		Assert.assertEquals(packageResponseData.getStartingDatePattern(), packageRequestData.getStartingDatePattern());
	}

}
