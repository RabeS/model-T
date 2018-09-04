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

package de.hybris.platform.travelfacades.facades.accommodation.populators;

import de.hybris.platform.commercefacades.accommodation.RoomTypeData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.travelservices.model.product.AccommodationModel;
import de.hybris.platform.travelservices.model.product.SleepingRoomModel;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Populator responsible to populate the basic information of the RoomTypeData
 *
 * @param <SOURCE>
 * 		the AccommodationModel
 * @param <TARGET>
 * 		the RoomTypeData
 */
public class AccommodationBasicPopulator<SOURCE extends AccommodationModel, TARGET extends RoomTypeData>
		implements Populator<SOURCE, TARGET>
{
	private EnumerationService enumerationService;

	@Override
	public void populate(final SOURCE source, final TARGET target) throws ConversionException
	{
		target.setCode(source.getCode());
		target.setSizeMeasurement(source.getSize());
		target.setDescription(source.getDescription());
		target.setName(source.getName());
		target.setIgnoreRules(BooleanUtils.isNotFalse(source.getIgnoreRules()));

		if (source instanceof SleepingRoomModel)
		{
			final SleepingRoomModel sleepingRoomModel = (SleepingRoomModel) source;
			if (sleepingRoomModel.getViewType() == null)
			{
				return;
			}
			target.setRoomView(getEnumerationService().getEnumerationName(sleepingRoomModel.getViewType()));
		}
	}

	/**
	 * @return the enumerationService
	 */
	protected EnumerationService getEnumerationService()
	{
		return enumerationService;
	}

	/**
	 * @param enumerationService
	 * 		the enumerationService to set
	 */
	@Required
	public void setEnumerationService(EnumerationService enumerationService)
	{
		this.enumerationService = enumerationService;
	}
}
