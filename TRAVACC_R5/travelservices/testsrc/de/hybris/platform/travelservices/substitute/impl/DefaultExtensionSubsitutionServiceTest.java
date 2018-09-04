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

package de.hybris.platform.travelservices.substitute.impl;

import de.hybris.bootstrap.annotations.UnitTest;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;


/**
 * unit test for {@link DefaultExtensionSubsitutionService}
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultExtensionSubsitutionServiceTest
{
	@InjectMocks
	DefaultExtensionSubsitutionService extensionSubsitutionService;
	private Map<String, String> extensionSubstitutionMap;
	@Test
	public void testGetSubstitutedExtension()
	{
		extensionSubstitutionMap = new HashMap<>();
		extensionSubstitutionMap.put("TEST_EXTENSION", "TEST_EXTENSION");
		final String extension = "TEST_EXTENSION";
		extensionSubsitutionService.setExtensionSubstitutionMap(extensionSubstitutionMap);
		Assert.assertEquals("TEST_EXTENSION", extensionSubsitutionService.getSubstitutedExtension(extension));
	}

	@Test
	public void testGetSubstitutedExtensionForUnknownExtension()
	{
		extensionSubstitutionMap = new HashMap<>();
		extensionSubstitutionMap.put("TEST_EXTENSION", "TEST_EXTENSION");
		final String extension = "TEST_EXTENSION_2";
		extensionSubsitutionService.setExtensionSubstitutionMap(extensionSubstitutionMap);
		Assert.assertEquals(extension, extensionSubsitutionService.getSubstitutedExtension(extension));
	}
}
