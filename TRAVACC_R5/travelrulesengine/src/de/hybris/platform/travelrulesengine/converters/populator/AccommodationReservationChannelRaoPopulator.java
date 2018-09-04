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

import de.hybris.platform.commercefacades.accommodation.AccommodationReservationData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.travelrulesengine.rao.AccommodationReservationRAO;

/**
 * The type Accommodation reservation channel rao populator.
 */
public class AccommodationReservationChannelRaoPopulator extends AbstractAccommodationChannelRaoPopulator
		implements Populator<AccommodationReservationData, AccommodationReservationRAO>
{
	@Override
	public void populate(final AccommodationReservationData source, final AccommodationReservationRAO target)
			throws ConversionException
	{
		target.setSalesApplication(getSalesApplication());
	}

}
