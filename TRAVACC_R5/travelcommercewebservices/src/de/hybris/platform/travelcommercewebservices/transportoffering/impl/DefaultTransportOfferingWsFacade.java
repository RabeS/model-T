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
package de.hybris.platform.travelcommercewebservices.transportoffering.impl;

import de.hybris.platform.commercefacades.travel.TransportOfferingData;
import de.hybris.platform.commercefacades.travel.enums.TransportOfferingOption;
import de.hybris.platform.travelcommercewebservices.transportoffering.TransportOfferingWsFacade;
import de.hybris.platform.travelfacades.facades.impl.DefaultTransportOfferingFacade;
import de.hybris.platform.travelservices.constants.TravelservicesConstants;
import de.hybris.platform.travelservices.model.warehouse.TransportOfferingModel;
import de.hybris.platform.travelservices.utils.TravelDateUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;


/**
 * Facade for operations on carts {@link TransportOfferingWsFacade}.
 */
public class DefaultTransportOfferingWsFacade extends DefaultTransportOfferingFacade implements TransportOfferingWsFacade
{

	@Override
	public TransportOfferingData getTransportOffering(final String code)
	{
		final TransportOfferingModel transportOffering = getTransportOfferingService().getTransportOffering(code);
		return Objects.isNull(transportOffering) ? null : getTransportOfferingConverter().convert(transportOffering);
	}

	@Override
	public List<TransportOfferingData> getTransportOfferings(final String number, final String departureDate)
	{
		final Date date = TravelDateUtils.convertStringDateToDate(departureDate, TravelservicesConstants.DATE_PATTERN);
		return super.getTransportOfferings(number, date,
				Arrays.asList(TransportOfferingOption.STATUS, TransportOfferingOption.TERMINAL));
	}

}
