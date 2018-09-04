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
package de.hybris.platform.travelservices.search.strategies;

import java.util.Date;

/**
 * The Solr Date range strategy.
 * 
 * @spring.bean solrDateRangeStrategy
 */
public interface SolrDateRangeStrategy
{

    /**
     * The input date will be converted to solr specific format.
     * 
     * @param date
     *           the date to be formatted
     * @returns string
     *           the Solr formatted date.
     */
    String getSolrFormattedDate(Date date);
}
