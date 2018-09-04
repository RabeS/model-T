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
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.travelservices.model.order.AccommodationOrderEntryGroupModel;

import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.DateUtils;


/**
 * The type Accommodation order entry date cart rao populator.
 */
public class StayDatesCartRaoPopulator implements Populator<AbstractOrderModel, CartRAO>
{

	@Override
	public void populate(final AbstractOrderModel source, final CartRAO target) throws ConversionException
	{
		if (Objects.isNull(source) || CollectionUtils.isEmpty(source.getEntries()))
		{
			return;
		}

		final AccommodationOrderEntryGroupModel accommodationOrderEntryGroupModel = source.getEntries().stream()
				.map(AbstractOrderEntryModel::getEntryGroup).filter(AccommodationOrderEntryGroupModel.class::isInstance)
				.map(AccommodationOrderEntryGroupModel.class::cast).findFirst().orElse(null);

		if (Objects.nonNull(accommodationOrderEntryGroupModel))
		{
			target.setCheckInDate(accommodationOrderEntryGroupModel.getStartingDate());
			target.setCheckOutDate(DateUtils.addDays(accommodationOrderEntryGroupModel.getEndingDate(), -1));
		}
	}
}
