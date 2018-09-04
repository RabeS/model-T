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
package de.hybris.platform.travelrulesengine.rao.providers.impl;

import de.hybris.platform.commercefacades.accommodation.RoomStayData;
import de.hybris.platform.ruleengineservices.rao.providers.RAOProvider;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.travelrulesengine.rao.RoomStayRAO;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;


/**
 * RAO Provider which creates RoomStayRAO facts to be used in rules evaluation
 */
public class DefaultRoomStayRaoProvider implements RAOProvider
{
	private Collection<String> defaultOptions;
	private Converter<RoomStayData, RoomStayRAO> roomStayRAOConverter;

	@Override
	public Set expandFactModel(final Object modelFact)
	{
		return expandFactModel(modelFact, getDefaultOptions());
	}

	/**
	 * Expand fact model set.
	 *
	 * @param modelFact
	 * 		the model fact
	 * @param options
	 * 		the options
	 *
	 * @return the set
	 */
	protected Set<Object> expandFactModel(final Object modelFact, final Collection<String> options)
	{
		return modelFact instanceof RoomStayData ?
				expandRAO(createRAO((RoomStayData) modelFact), options) :
				Collections.emptySet();
	}

	/**
	 * Create rao room stay rao.
	 *
	 * @param source
	 * 		the source
	 *
	 * @return the room stay rao
	 */
	protected RoomStayRAO createRAO(final RoomStayData source)
	{
		return getRoomStayRAOConverter().convert(source);
	}

	/**
	 * Expand rao set.
	 *
	 * @param rao
	 * 		the rao
	 * @param options
	 * 		the options
	 *
	 * @return the set
	 */
	protected Set<Object> expandRAO(final RoomStayRAO rao, final Collection<String> options)
	{
		final Set<Object> facts = new LinkedHashSet<>();

		options.forEach(option -> {
			if (("INCLUDE_ROOM_STAY").equals(option))
			{
				facts.add(rao);
			}
		});

		return facts;
	}

	/**
	 * Gets default options.
	 *
	 * @return the default options
	 */
	protected Collection<String> getDefaultOptions()
	{
		return defaultOptions;
	}

	/**
	 * Sets default options.
	 *
	 * @param defaultOptions
	 * 		the default options
	 */
	@Required
	public void setDefaultOptions(final Collection<String> defaultOptions)
	{
		this.defaultOptions = defaultOptions;
	}

	/**
	 * Gets roomStayRAOConverter.
	 *
	 * @return the roomStayRAOConverter
	 */
	protected Converter<RoomStayData, RoomStayRAO> getRoomStayRAOConverter()
	{
		return roomStayRAOConverter;
	}

	/**
	 * Sets roomStayRAOConverter.
	 *
	 * @param roomStayRAOConverter
	 * 		the roomStayRAOConverter
	 */
	@Required
	public void setRoomStayRAOConverter(
			final Converter<RoomStayData, RoomStayRAO> roomStayRAOConverter)
	{
		this.roomStayRAOConverter = roomStayRAOConverter;
	}
}
