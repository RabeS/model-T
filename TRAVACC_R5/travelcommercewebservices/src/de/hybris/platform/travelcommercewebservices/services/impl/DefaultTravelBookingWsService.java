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
package de.hybris.platform.travelcommercewebservices.services.impl;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.travelservices.services.impl.DefaultBookingService;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


/**
 * Service for operations on order {@link DefaultBookingService}.
 */
public class DefaultTravelBookingWsService extends DefaultBookingService
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultTravelBookingWsService.class);

	private BaseSiteService baseSiteService;

	@Override
	public OrderModel getOrder(final String bookingReference)
	{
		try
		{
			final BaseStoreModel baseStoreModel = getBaseSiteService().getCurrentBaseSite().getStores().get(0);
			final OrderModel orderModel = getCustomerAccountService().getOrderForCode(bookingReference, baseStoreModel);
			if (Objects.isNull(orderModel))
			{
				LOG.warn("Unable to find Order with bookingReference " + bookingReference);
			}
			return orderModel;
		}
		catch ( final ModelNotFoundException | UnknownIdentifierException e )
		{
			LOG.warn("Unable to find Order with bookingReference " + bookingReference);
			LOG.debug("Logging exception: ", e);
			return null;
		}
	}

	/**
	 * Gets baseSite service
	 *
	 * @return the baseSiteService
	 */
	protected BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	/**
	 * Sets travel reservation facade
	 *
	 * @param baseSiteService
	 * 		the baseSiteService to set
	 */
	@Required
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}
}
