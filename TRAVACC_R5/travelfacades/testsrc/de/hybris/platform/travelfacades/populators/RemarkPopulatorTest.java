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

import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.travel.RemarkData;
import de.hybris.platform.travelservices.model.travel.RemarkModel;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


/**
 * Unit Test for the implementation of {@link RemarkPopulator}.
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class RemarkPopulatorTest
{

	@InjectMocks
	RemarkPopulator remarkPopulator;

	private final String TEST_REMARK_CODE = "TEST_REMARK_CODE";
	@Test
	public void populateTest()
	{
		final RemarkModel source = Mockito.mock(RemarkModel.class);
		given(source.getCode()).willReturn(TEST_REMARK_CODE);

		final RemarkData target = new RemarkData();
		remarkPopulator.populate(source, target);
		Assert.assertEquals(TEST_REMARK_CODE, target.getCode());
	}
}
