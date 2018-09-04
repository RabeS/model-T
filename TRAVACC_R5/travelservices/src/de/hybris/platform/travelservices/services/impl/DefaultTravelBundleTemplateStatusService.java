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
package de.hybris.platform.travelservices.services.impl;

import de.hybris.platform.configurablebundleservices.model.BundleTemplateStatusModel;
import de.hybris.platform.travelservices.dao.TravelBundleTemplateStatusDao;
import de.hybris.platform.travelservices.services.TravelBundleTemplateStatusService;

import org.springframework.beans.factory.annotation.Required;


/**
 * Implementation of TravelBundleTemplateStatusService - Provides business logic for retrieving BundleTemplateStatus.
 */
public class DefaultTravelBundleTemplateStatusService implements TravelBundleTemplateStatusService
{
	private TravelBundleTemplateStatusDao travelBundleTemplateStatusDao;

	@Override
	public BundleTemplateStatusModel getBundleTemplateStatusForId(final String statusId)
	{
		return getTravelBundleTemplateStatusDao().findBundleTemplateStatus(statusId);
	}

	/**
	 * Gets travel bundle template status dao.
	 *
	 * @return the travel bundle template status dao
	 */
	protected TravelBundleTemplateStatusDao getTravelBundleTemplateStatusDao()
	{
		return travelBundleTemplateStatusDao;
	}

	/**
	 * Sets travel bundle template status dao.
	 *
	 * @param travelBundleTemplateStatusDao
	 * 		the travel bundle template status dao
	 */
	@Required
	public void setTravelBundleTemplateStatusDao(final TravelBundleTemplateStatusDao travelBundleTemplateStatusDao)
	{
		this.travelBundleTemplateStatusDao = travelBundleTemplateStatusDao;
	}
}
