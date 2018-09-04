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

package de.hybris.platform.travelbackofficeservices.dao.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.travelbackofficeservices.dao.TravelBackofficeGenericDao;

import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.MapUtils;


/**
 * Default implementation of {@link TravelBackofficeGenericDao}
 */
public class DefaultTravelBackofficeGenericDao<M extends ItemModel> extends DefaultGenericDao<M>
		implements TravelBackofficeGenericDao
{
	public DefaultTravelBackofficeGenericDao(final String typecode)
	{
		super(typecode);
	}

	@Override
	public Object getModelForAttribute(final Map<String, Object> attributes, final String typeCode)
			throws ModelNotFoundException, AmbiguousIdentifierException
	{
		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("SELECT {pk} FROM {").append(typeCode);
		stringBuilder.append("}");

		if (MapUtils.isNotEmpty(attributes))
		{
			stringBuilder.append(" WHERE ");
			final String queryString = attributes.keySet().stream()
					.map(attribute -> "{" + attribute + "} = ?" + attribute)
					.collect(Collectors.joining(" AND "));
			stringBuilder.append(queryString);
		}

		final FlexibleSearchQuery query = new FlexibleSearchQuery(stringBuilder.toString());

		if (MapUtils.isNotEmpty(attributes))
		{
			query.getQueryParameters().putAll(attributes);
		}

		return getFlexibleSearchService().searchUnique(query);
	}

}
