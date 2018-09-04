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

package de.hybris.platform.travelfacades.facades.accommodation.comparators;

import de.hybris.platform.commercefacades.accommodation.property.FacilityData;

import java.util.Comparator;


/**
 * The type Configured facility data comparator.
 */
public class ConfiguredFacilityDataComparator implements Comparator<FacilityData>
{

	@Override
	public final int compare(final FacilityData fd1, final FacilityData fd2)
	{
		return fd1.getFacilityType().compareTo(fd2.getFacilityType());
	}
}
