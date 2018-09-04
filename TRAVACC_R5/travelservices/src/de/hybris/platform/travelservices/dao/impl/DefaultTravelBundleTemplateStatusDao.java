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
package de.hybris.platform.travelservices.dao.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.configurablebundleservices.model.BundleTemplateStatusModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.travelservices.dao.TravelBundleTemplateStatusDao;

import java.util.Collections;
import java.util.List;


/**
 * Implementation of TravelBundleTemplateStatusDao to retrieve BundleTemplateStatus
 */
public class DefaultTravelBundleTemplateStatusDao extends DefaultGenericDao<BundleTemplateStatusModel>
		implements TravelBundleTemplateStatusDao
{

	public DefaultTravelBundleTemplateStatusDao(final String typecode)
	{
		super(typecode);
	}

	@Override
	public BundleTemplateStatusModel findBundleTemplateStatus(final String id)
	{
		validateParameterNotNull(id, "Bundle Template Status Id must not be null!");

		final List<BundleTemplateStatusModel> bundleTemplateStatusModels = find(
				Collections.singletonMap(BundleTemplateStatusModel.ID, id));
		return bundleTemplateStatusModels.stream().findFirst().orElseGet(null);
	}

}
