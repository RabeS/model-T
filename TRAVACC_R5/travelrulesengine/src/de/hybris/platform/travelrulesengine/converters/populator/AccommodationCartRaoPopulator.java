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


import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.travelrulesengine.rao.AccommodationRAO;
import de.hybris.platform.travelservices.model.order.AccommodationOrderEntryGroupModel;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;


/**
 * The type Accommodation cart rao populator.
 */
public class AccommodationCartRaoPopulator implements Populator<AbstractOrderModel, CartRAO>
{

	@Override
	public void populate(final AbstractOrderModel source, final CartRAO target) throws ConversionException
	{
		if (Objects.isNull(source) || CollectionUtils.isEmpty(source.getEntries()))
		{
			return;
		}

		final List<AccommodationOrderEntryGroupModel> accommodationOrderEntryGroups = source.getEntries().stream()
				.filter(entry -> entry.getEntryGroup() instanceof AccommodationOrderEntryGroupModel)
				.map(entry -> (AccommodationOrderEntryGroupModel) entry.getEntryGroup()).collect(Collectors.toList());

		final List<AccommodationRAO> accommodationRAOs = new LinkedList<>();
		populateAccommodations(accommodationOrderEntryGroups, accommodationRAOs);

		target.setAccommodations(accommodationRAOs);
	}

	/**
	 * Populate accommodations.
	 *
	 * @param accommodationOrderEntryGroups
	 * 		the accommodation models
	 * @param accommodationRAOs
	 * 		the target
	 */
	protected void populateAccommodations(final List<AccommodationOrderEntryGroupModel> accommodationOrderEntryGroups,
			final List<AccommodationRAO> accommodationRAOs)
	{
		if (CollectionUtils.isEmpty(accommodationOrderEntryGroups))
		{
			return;
		}

		for (final AccommodationOrderEntryGroupModel accommodationOrderEntryGroup : accommodationOrderEntryGroups)
		{
			final AccommodationRAO accommodationRao = new AccommodationRAO();
			accommodationRao.setCode(accommodationOrderEntryGroup.getAccommodation().getCode());
			accommodationRao.setRatePlan(accommodationOrderEntryGroup.getRatePlan().getCode());
			accommodationRAOs.add(accommodationRao);
		}
	}
}
