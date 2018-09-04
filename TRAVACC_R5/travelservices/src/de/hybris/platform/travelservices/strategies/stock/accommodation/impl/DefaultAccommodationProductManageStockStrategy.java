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

package de.hybris.platform.travelservices.strategies.stock.accommodation.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.stock.exception.InsufficientStockLevelException;
import de.hybris.platform.travelservices.model.order.AccommodationOrderEntryGroupModel;
import de.hybris.platform.travelservices.stock.TravelCommerceStockService;
import de.hybris.platform.travelservices.strategies.stock.TravelManageStockStrategy;
import de.hybris.platform.travelservices.utils.TravelDateUtils;

import java.util.Collections;
import java.util.Date;
import java.util.stream.Stream;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Class to manage stock for accommodation products which haven't got any specific associated strategy
 */
public class DefaultAccommodationProductManageStockStrategy implements TravelManageStockStrategy
{

	private TravelCommerceStockService commerceStockService;

	@Override
	public void reserve(final AbstractOrderEntryModel abstractOrderEntry) throws InsufficientStockLevelException
	{
		final AccommodationOrderEntryGroupModel accommodationEntryGroup = (AccommodationOrderEntryGroupModel) abstractOrderEntry
				.getEntryGroup();
		Date date = accommodationEntryGroup.getStartingDate();
		final Date endingDate = accommodationEntryGroup.getEndingDate();

		if(TravelDateUtils.isSameDate(date, endingDate))
		{
			return;
		}

		final int quantityPerDay =
				abstractOrderEntry.getQuantity().intValue() / (int) TravelDateUtils.getDaysBetweenDates(date, endingDate);
		while (!TravelDateUtils.isSameDate(date, endingDate))
		{
			getCommerceStockService().reservePerDateProduct(abstractOrderEntry.getProduct(), date, quantityPerDay,
					Collections.singletonList(accommodationEntryGroup.getAccommodationOffering()));
			date = DateUtils.addDays(date, 1);
		}
	}

	@Override
	public void release(final AbstractOrderEntryModel abstractOrderEntry)
	{
		final int qty = abstractOrderEntry.getQuantity().intValue();
		if (qty > 0)
		{
			final AccommodationOrderEntryGroupModel accommodationEntryGroup = (AccommodationOrderEntryGroupModel) abstractOrderEntry
					.getEntryGroup();
			Date date = accommodationEntryGroup.getStartingDate();
			final Date endingDate = accommodationEntryGroup.getEndingDate();

			if(TravelDateUtils.isSameDate(date, endingDate))
			{
				return;
			}

			final int quantityPerDay =
					abstractOrderEntry.getQuantity().intValue() / (int) TravelDateUtils.getDaysBetweenDates(date, endingDate);
			while (!TravelDateUtils.isSameDate(date, endingDate))
			{
				getCommerceStockService().releasePerDateProduct(abstractOrderEntry.getProduct(), date, quantityPerDay,
						Collections.singletonList(accommodationEntryGroup.getAccommodationOffering()));
				date = DateUtils.addDays(date, 1);
			}
		}
	}

	/**
	 * @return commerceStockService
	 */
	protected TravelCommerceStockService getCommerceStockService()
	{
		return commerceStockService;
	}

	/**
	 * @param commerceStockService the commerceStockService to set
	 */
	@Required
	public void setCommerceStockService(final TravelCommerceStockService commerceStockService)
	{
		this.commerceStockService = commerceStockService;
	}



}
