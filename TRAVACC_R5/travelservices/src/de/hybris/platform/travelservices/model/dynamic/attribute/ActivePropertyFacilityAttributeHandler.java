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

import de.hybris.platform.servicelayer.model.attribute.AbstractDynamicAttributeHandler;
import de.hybris.platform.travelservices.model.facility.PropertyFacilityModel;
import de.hybris.platform.travelservices.model.warehouse.AccommodationOfferingModel;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;


/**
 * The type Active property facility attribute handler.
 */
public class ActivePropertyFacilityAttributeHandler
		extends AbstractDynamicAttributeHandler<List<PropertyFacilityModel>, AccommodationOfferingModel>
{
	@Override
	public List<PropertyFacilityModel> get(final AccommodationOfferingModel accommodationOfferingModel)
	{
		if (CollectionUtils.isEmpty(accommodationOfferingModel.getPropertyFacility()))
		{
			return Collections.emptyList();
		}

		return accommodationOfferingModel.getPropertyFacility().stream().filter(
				propertyFacility -> propertyFacility != null && (propertyFacility.getActive() == null || propertyFacility
						.getActive())).collect(Collectors.toList());
	}
}
