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

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.ruleengineservices.converters.populator.OrderEntryRaoPopulator;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.travelrulesengine.rao.TransportOfferingRAO;
import de.hybris.platform.travelservices.model.warehouse.TransportOfferingModel;

import java.util.ArrayList;
import java.util.List;


public class TravelOrderEntryRaoPopulator extends OrderEntryRaoPopulator
{
	@Override
	public void populate(final AbstractOrderEntryModel source, final OrderEntryRAO target) throws ConversionException
	{
		super.populate(source, target);
		if (source.getTravelOrderEntryInfo() != null)
		{
			final List<TransportOfferingRAO> transportOfferingRaos = new ArrayList<>();
			for (final TransportOfferingModel transportOffering : source.getTravelOrderEntryInfo().getTransportOfferings())
			{
				final TransportOfferingRAO transportOfferingRao = new TransportOfferingRAO();
				transportOfferingRao.setDepartureTime(transportOffering.getDepartureTime());
				transportOfferingRaos.add(transportOfferingRao);
			}
			target.setTransportOfferings(transportOfferingRaos);
		}
	}

}
