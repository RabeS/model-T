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

import de.hybris.platform.commercefacades.travel.ancillary.data.OfferResponseData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.ndcfacades.ndc.BaggageListRS;
import de.hybris.platform.ndcfacades.ndc.DataListType;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.Map;


/**
 * Data list populator for NDC {@link BaggageListRS}
 */
public class NDCBaggageListRSDataListPopulator extends NDCAbstractOffersRSDataListPopulator
		implements Populator<OfferResponseData, BaggageListRS>
{

	@Override
	public void populate(final OfferResponseData source, final BaggageListRS target) throws ConversionException
	{
		final Map<String, Integer> map = createTravellerTypeCountMap(source);
		final DataListType dataListType = new DataListType();
		populateFlightSegments(source, dataListType);
		dataListType.setAnonymousTravelerList(createTravellers(map));
		target.setDataLists(dataListType);
	}

}
