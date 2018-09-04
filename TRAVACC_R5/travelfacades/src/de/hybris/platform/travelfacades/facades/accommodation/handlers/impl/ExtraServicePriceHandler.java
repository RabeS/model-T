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

package de.hybris.platform.travelfacades.facades.accommodation.handlers.impl;

import de.hybris.platform.commercefacades.accommodation.AccommodationReservationData;
import de.hybris.platform.commercefacades.accommodation.ReservedRoomStayData;
import de.hybris.platform.commercefacades.accommodation.ServiceData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.travelfacades.facades.accommodation.handlers.AccommodationServiceHandler;


/**
 * Handler class to populate the price of the serviceData for a given productModel and reservedRoomStayData
 */
public class ExtraServicePriceHandler extends AbstractExtraServicePriceHandler implements AccommodationServiceHandler
{
	@Override
	public void handle(final ProductModel productModel, final ReservedRoomStayData reservedRoomStayData,
					   final ServiceData serviceData, final AccommodationReservationData accommodationReservationData)
	{
		serviceData.setPrice(getServiceRate(serviceData, reservedRoomStayData));
	}
}
