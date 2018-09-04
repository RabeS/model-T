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

import de.hybris.platform.travelservices.dao.MarketingRatePlanInfoDao;
import de.hybris.platform.travelservices.model.accommodation.MarketingRatePlanInfoModel;
import de.hybris.platform.travelservices.services.MarketingRatePlanInfoService;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link MarketingRatePlanInfoService}
 */
public class DefaultMarketingRatePlanInfoService implements MarketingRatePlanInfoService
{

	private MarketingRatePlanInfoDao marketingRatePlanInfoDao;

	@Override
	public MarketingRatePlanInfoModel getMarketingRatePlanInfoForCode(final String marketingRatePlanInfoCode)
	{
		return getMarketingRatePlanInfoDao().findMarketingRatePlanInfo(marketingRatePlanInfoCode);
	}

	/**
	 * @return the marketingRatePlanInfoDao
	 */
	protected MarketingRatePlanInfoDao getMarketingRatePlanInfoDao()
	{
		return marketingRatePlanInfoDao;
	}

	/**
	 * @param marketingRatePlanInfoDao
	 *           the marketingRatePlanInfoDao to set
	 */
	@Required
	public void setMarketingRatePlanInfoDao(final MarketingRatePlanInfoDao marketingRatePlanInfoDao)
	{
		this.marketingRatePlanInfoDao = marketingRatePlanInfoDao;
	}

}
