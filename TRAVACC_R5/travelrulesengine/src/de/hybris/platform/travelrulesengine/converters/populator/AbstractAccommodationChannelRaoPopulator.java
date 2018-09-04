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

package de.hybris.platform.travelrulesengine.converters.populator;

import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.travelrulesengine.constants.TravelrulesengineConstants;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * The type Abstract accommodation channel rao populator.
 */
public abstract class AbstractAccommodationChannelRaoPopulator
{

	private SessionService sessionService;

	protected String getSalesApplication()
	{
		final SalesApplication salesApp = getSessionService().getAttribute(TravelrulesengineConstants.SESSION_SALES_APPLICATION);

		return Objects.isNull(salesApp) ? StringUtils.EMPTY : salesApp.getCode();
	}


	/**
	 * Gets session service.
	 *
	 * @return the session service
	 */
	protected SessionService getSessionService()
	{
		return sessionService;
	}

	/**
	 * Sets session service.
	 *
	 * @param sessionService
	 * 		the session service
	 */
	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}
}
