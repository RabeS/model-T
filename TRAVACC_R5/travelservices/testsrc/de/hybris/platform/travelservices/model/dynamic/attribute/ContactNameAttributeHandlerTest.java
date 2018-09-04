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

package de.hybris.platform.travelservices.model.dynamic.attribute;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.travelservices.model.order.AccommodationOrderEntryGroupModel;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;


/**
 * The type Contact name attribute handler test.
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class ContactNameAttributeHandlerTest
{
	@InjectMocks
	private ContactNameAttributeHandler contactNameAttributeHandler;

	@Test
	public void testGet()
	{
		final AccommodationOrderEntryGroupModel accommodationOrderEntryGroupModel = new AccommodationOrderEntryGroupModel();
		accommodationOrderEntryGroupModel.setFirstName("First");
		accommodationOrderEntryGroupModel.setLastName("Last");

		Assert.assertEquals("First Last", contactNameAttributeHandler.get(accommodationOrderEntryGroupModel));

	}

}
