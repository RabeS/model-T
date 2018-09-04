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

package de.hybris.platform.travelservices.dao.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.travelservices.dao.SolrIndexSortDao;
import de.hybris.platform.travelservices.model.search.SolrIndexedTypeDefaultSortOrderMappingModel;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;


/**
 * Default implementation of {@link SolrIndexSortDao}
 */
public class DefaultSolrIndexSortDao extends DefaultGenericDao<SolrIndexedTypeDefaultSortOrderMappingModel>
		implements SolrIndexSortDao
{
	public DefaultSolrIndexSortDao(final String typecode)
	{
		super(typecode);
	}

	@Override
	public SolrIndexedTypeDefaultSortOrderMappingModel findIndexOrderSortMapping(final String indexedType)
	{
		validateParameterNotNull(indexedType, "Indexed Type must not be null!");

		final List<SolrIndexedTypeDefaultSortOrderMappingModel> indexedTypes = find(
				Collections.singletonMap("indexedType", indexedType));

		return CollectionUtils.isNotEmpty(indexedTypes) ? indexedTypes.get(0) : null;
	}
}
