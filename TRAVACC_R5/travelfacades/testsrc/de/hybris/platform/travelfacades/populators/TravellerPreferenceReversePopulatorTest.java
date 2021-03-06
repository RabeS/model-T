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
import de.hybris.platform.commercefacades.travel.TravellerPreferenceData;
import de.hybris.platform.travelservices.enums.TravellerPreferenceType;
import de.hybris.platform.travelservices.model.user.TravellerPreferenceModel;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;


/**
 * unit test for {@link TravellerPreferenceReversePopulator}
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class TravellerPreferenceReversePopulatorTest
{
	@InjectMocks
	TravellerPreferenceReversePopulator travellerPreferenceReversePopulator;

	@Test
	public void testPopulateTravellerPreferenceModel()
	{
		final TravellerPreferenceData tpData = new TravellerPreferenceData();
		tpData.setType(TravellerPreferenceType.LANGUAGE.name());
		tpData.setValue(TravellerPreferenceType.LANGUAGE.toString());

		final TravellerPreferenceModel tpModel = new TravellerPreferenceModel();
		travellerPreferenceReversePopulator.populate(tpData, tpModel);
		Assert.assertEquals(TravellerPreferenceType.LANGUAGE.toString(), tpModel.getValue());
		Assert.assertEquals(TravellerPreferenceType.LANGUAGE, tpModel.getType());

	}
}
