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
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.travelrulesengine.constants.TravelrulesengineConstants;


import org.apache.commons.lang3.StringUtils;


/**
 * This class populates the sales application in CartRao fetched from session and during NDC request it will fetch the
 * same from an OrderModel
 */
public class ChannelCartRaoPopulator extends AbstractAccommodationChannelRaoPopulator
		implements Populator<AbstractOrderModel, CartRAO>
{
	@Override
	public void populate(final AbstractOrderModel source, final CartRAO target) throws ConversionException
	{
		SalesApplication salesApp = (SalesApplication) getSessionService()
				.getAttribute(TravelrulesengineConstants.SESSION_SALES_APPLICATION);
		String salesApplication = getSalesApplication();
		/*
		 * Below block will execute during NDC requests
		 */
		if (StringUtils.isEmpty(salesApplication) && source instanceof OrderModel)
		{
			salesApp = ((OrderModel) source).getSalesApplication();
			salesApplication = salesApp.getCode();
		}
		target.setSalesApplication(salesApplication);
	}
}
