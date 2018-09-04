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
package de.hybris.platform.travelcommercewebservices.transportoffering;

import de.hybris.platform.commercefacades.travel.TransportOfferingData;
import de.hybris.platform.travelfacades.facades.TransportOfferingFacade;

import java.util.List;


/**
 * Facade that exposes Transport Offering specific services
 */
public interface TransportOfferingWsFacade extends TransportOfferingFacade
{

	/**
	 * Get a TransportOfferingData by code.
	 *
	 * @param code
	 * 		the unique code for a transport offering
	 * @return TransportOfferingData transport offering
	 */
	@Override
	TransportOfferingData getTransportOffering(String code);

	/**
	 * Get a list of TransportOfferingData by number and departureDate.
	 *
	 * @param number
	 * 		the number of requested transport offering
	 * @param departureDate
	 * 		the departure date of requested transport offering
	 * @return List<TransportOfferingData> transport offerings
	 */
	List<TransportOfferingData> getTransportOfferings(String number,String departureDate);
}
