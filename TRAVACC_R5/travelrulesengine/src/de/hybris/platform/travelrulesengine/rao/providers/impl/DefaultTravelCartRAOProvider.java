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

import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rao.providers.impl.DefaultCartRAOProvider;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * The type Default travel cart rao provider.
 */
public class DefaultTravelCartRAOProvider extends DefaultCartRAOProvider
{
	private Collection<String> travelOptions;

	@Override
	protected Set<Object> expandRAO(final CartRAO rao, final Collection<String> options)
	{
		final Set<Object> facts = new LinkedHashSet<>();

		getTravelOptions().forEach(option -> {
			if ("EXPAND_PASSENGER_TYPE_QUANTITIES".equals(option))
			{
				if (CollectionUtils.isNotEmpty(rao.getPassengerTypeQuantities()))
				{
					facts.addAll(rao.getPassengerTypeQuantities());
				}
			}
			if ("EXPAND_ACCOMMODATION_OFFERING".equals(option))
			{
				if (Objects.nonNull(rao.getAccommodationOffering()))
				{
					facts.add(rao.getAccommodationOffering());
				}
			}
			if ("EXPAND_ACCOMMODATION_PROVIDER".equals(option))
			{
				if (Objects.nonNull(rao.getAccommodationOffering()) && Objects.nonNull(rao.getAccommodationOffering().getProvider()))
				{
					facts.add(rao.getAccommodationOffering().getProvider());
				}
			}
			if ("EXPAND_ACCOMMODATION".equals(option))
			{
				if (CollectionUtils.isNotEmpty(rao.getAccommodations()))
				{
					facts.addAll(rao.getAccommodations());
				}
			}
		});

		facts.addAll(super.expandRAO(rao, options));

		return facts;
	}

	/**
	 * Gets travel options.
	 *
	 * @return the travel options
	 */
	public Collection<String> getTravelOptions()
	{
		return travelOptions;
	}

	/**
	 * Sets travel options.
	 *
	 * @param travelOptions
	 * 		the travel options
	 */
	@Required
	public void setTravelOptions(final Collection<String> travelOptions)
	{
		this.travelOptions = travelOptions;
	}
}
