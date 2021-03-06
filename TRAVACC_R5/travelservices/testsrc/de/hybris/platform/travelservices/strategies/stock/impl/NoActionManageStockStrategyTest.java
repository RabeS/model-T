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

package de.hybris.platform.travelservices.strategies.stock.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


/**
 * unit test for {@link NoActionManageStockStrategy}
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class NoActionManageStockStrategyTest
{
	@InjectMocks
	NoActionManageStockStrategy noActionManageStockStrategy;

	@Mock
	private AbstractOrderEntryModel entry;

	@Test
	public void testReserve()
	{
		noActionManageStockStrategy.reserve(entry);
	}


	@Test
	public void testRelease()
	{
		noActionManageStockStrategy.release(entry);
	}
}
