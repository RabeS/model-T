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
package de.hybris.platform.ndcfacades.populators.response;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.ndcfacades.ndc.FlightPriceRQ;
import de.hybris.platform.ndcfacades.ndc.FlightPriceRS;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

/**
 * NDC Document populator for {@link FlightPriceRS}
 */
public class NDCFlightPriceRSDocumentPopulator extends NDCMsgDocumentTypePopulator implements Populator<FlightPriceRQ, FlightPriceRS>
{
	@Override
	public void populate(final FlightPriceRQ flightPriceRQ, final FlightPriceRS flightPriceRS) throws ConversionException
	{
		flightPriceRS.setDocument(getMsgDocumentType());
	}
}
