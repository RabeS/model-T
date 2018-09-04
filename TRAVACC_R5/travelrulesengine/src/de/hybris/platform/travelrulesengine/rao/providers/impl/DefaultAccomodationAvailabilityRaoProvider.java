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

import de.hybris.platform.commercefacades.accommodation.AccommodationAvailabilityRequestData;
import de.hybris.platform.ruleengineservices.rao.providers.RAOProvider;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.travelrulesengine.rao.AccommodationAvailabilityRequestRAO;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * RAO Provider which creates AccommodationAvailability facts to be used in rules evaluation
 */
public class DefaultAccomodationAvailabilityRaoProvider implements RAOProvider
{
	private Collection<String> defaultOptions;
	private Converter<AccommodationAvailabilityRequestData, AccommodationAvailabilityRequestRAO>
			accommodationAvailabilityRequestRAOConverter;

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
		return modelFact instanceof AccommodationAvailabilityRequestData ?
				expandRAO(createRAO((AccommodationAvailabilityRequestData) modelFact), options) :
				Collections.emptySet();
	}

	/**
	 * Create rao accommodation availability request rao.
	 *
	 * @param source
	 * 		the source
	 *
	 * @return the accommodation availability request rao
	 */
	protected AccommodationAvailabilityRequestRAO createRAO(final AccommodationAvailabilityRequestData source)
	{
		return getAccommodationAvailabilityRequestRAOConverter().convert(source);
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
	protected Set<Object> expandRAO(final AccommodationAvailabilityRequestRAO rao, final Collection<String> options)
	{
		final Set<Object> facts = new LinkedHashSet<>();

		options.forEach(option -> {
			if (("INCLUDE_ACCOMMODATION_AVAILABILITY").equals(option))
			{
				facts.add(rao);
			}
			if ("EXPAND_USER".equals(option))
			{
				facts.add(rao.getUser());
			}
			if ("EXPAND_USER_GROUP".equals(option))
			{
				facts.addAll(rao.getUser().getGroups());
			}
			if ("EXPAND_ACCOMMODATION_OFFERING".equals(option))
			{
				facts.add(rao.getAccommodationOffering());
			}
			if ("EXPAND_ACCOMMODATION_PROVIDER".equals(option))
			{
				if (Objects.nonNull(rao.getAccommodationOffering()) && Objects.nonNull(rao.getAccommodationOffering().getProvider()))
				{
					facts.add(rao.getAccommodationOffering().getProvider());
				}
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
	 * Gets accommodationAvailabilityRequestRAOConverter.
	 *
	 * @return the accommodationAvailabilityRequestRAOConverter
	 */
	protected Converter<AccommodationAvailabilityRequestData, AccommodationAvailabilityRequestRAO>
	getAccommodationAvailabilityRequestRAOConverter()
	{
		return accommodationAvailabilityRequestRAOConverter;
	}

	/**
	 * Sets accommodationAvailabilityRequestRAOConverter.
	 *
	 * @param accommodationAvailabilityRequestRAOConverter
	 * 		the accommodationAvailabilityRequestRAOConverter
	 */
	@Required
	public void setAccommodationAvailabilityRequestRAOConverter(
			final Converter<AccommodationAvailabilityRequestData, AccommodationAvailabilityRequestRAO>
					accommodationAvailabilityRequestRAOConverter)
	{
		this.accommodationAvailabilityRequestRAOConverter = accommodationAvailabilityRequestRAOConverter;
	}
}
