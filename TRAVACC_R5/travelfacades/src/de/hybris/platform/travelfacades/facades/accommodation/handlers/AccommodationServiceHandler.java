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

package de.hybris.platform.travelfacades.facades.accommodation.handlers;

import de.hybris.platform.commercefacades.accommodation.AccommodationReservationData;
import de.hybris.platform.commercefacades.accommodation.ReservedRoomStayData;
import de.hybris.platform.commercefacades.accommodation.ServiceData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.travelservices.exceptions.AccommodationPipelineException;


/**
 * Interface for Accommodation Service handlers
 */
public interface AccommodationServiceHandler
{

	/**
	 * Handle the population of the serviceData for a given productModel and reservedRoomStayData
	 *
	 * @param productModel
	 * 		the product model
	 * @param reservedRoomStayData
	 * 		the reserved room stay data
	 * @param serviceData
	 * 		the service data
	 * @param accommodationReservationData
	 * 		the accommodation reservation data
	 * @throws AccommodationPipelineException
	 * 		the accommodation pipeline exception
	 */
	void handle(ProductModel productModel, ReservedRoomStayData reservedRoomStayData, ServiceData serviceData,
				AccommodationReservationData accommodationReservationData) throws AccommodationPipelineException;

}