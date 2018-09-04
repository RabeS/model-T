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

import de.hybris.platform.commercefacades.accommodation.AccommodationSearchResponseData;
import de.hybris.platform.ruleengineservices.rao.providers.RAOProvider;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.travelrulesengine.rao.AccommodationSearchResponseRAO;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;


/**
 * The type Default accommodation offering rao provider.
 */
public class DefaultAccommodationSearchResponseRaoProvider implements RAOProvider
{
	private Converter<AccommodationSearchResponseData, AccommodationSearchResponseRAO> accommodationSearchResponseRaoConverter;
	private Collection<String> defaultOptions;

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
	 * @return the set
	 */
	protected Set<Object> expandFactModel(final Object modelFact, final Collection<String> options)
	{
		return modelFact instanceof AccommodationSearchResponseData ?
				expandRAO(createRAO((AccommodationSearchResponseData) modelFact), options) :
				Collections.emptySet();
	}

	/**
	 * Create rao accommodation reservation rao.
	 *
	 * @param source
	 * 		the source
	 * @return the accommodation reservation rao
	 */
	protected AccommodationSearchResponseRAO createRAO(final AccommodationSearchResponseData source)
	{
		return getAccommodationSearchResponseRaoConverter().convert(source);
	}

	/**
	 * Expand rao set.
	 *
	 * @param rao
	 * 		the rao
	 * @param options
	 * 		the options
	 * @return the set
	 */
	protected Set<Object> expandRAO(final AccommodationSearchResponseRAO rao, final Collection<String> options)
	{
		final Set<Object> facts = new LinkedHashSet<>();

		options.forEach(option -> {
			if ("INCLUDE_ACCOMMODATION_SEARCH_RESPONSE".equals(option))
			{
				facts.add(rao);
			}
			if ("EXPAND_ACCOMMODATION_OFFERING".equals(option))
			{
				facts.add(rao.getAccommodationOffering());
			}
			if ("EXPAND_USER".equals(option))
			{
				facts.add(rao.getUser());
			}
			if ("EXPAND_USER_GROUP".equals(option))
			{
				facts.addAll(rao.getUser().getGroups());
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
	 * Gets accommodation search response rao converter.
	 *
	 * @return the accommodation search response rao converter
	 */
	protected Converter<AccommodationSearchResponseData, AccommodationSearchResponseRAO>
	getAccommodationSearchResponseRaoConverter()
	{
		return accommodationSearchResponseRaoConverter;
	}

	/**
	 * Sets accommodation search response rao converter.
	 *
	 * @param accommodationSearchResponseRaoConverter
	 * 		the accommodation search response rao converter
	 */
	@Required
	public void setAccommodationSearchResponseRaoConverter(
			final Converter<AccommodationSearchResponseData, AccommodationSearchResponseRAO>
					accommodationSearchResponseRaoConverter)
	{
		this.accommodationSearchResponseRaoConverter = accommodationSearchResponseRaoConverter;
	}
}
