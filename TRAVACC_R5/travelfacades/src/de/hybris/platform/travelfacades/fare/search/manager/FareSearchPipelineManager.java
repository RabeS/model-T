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
package de.hybris.platform.travelfacades.fare.search.manager;


import java.util.List;

import de.hybris.platform.commercefacades.travel.FareSearchRequestData;
import de.hybris.platform.commercefacades.travel.FareSelectionData;
import de.hybris.platform.commercefacades.travel.ScheduledRouteData;


/**
 * Pipeline Manager class that will return a {@link FareSelectionData} after executing a list of handlers on the given list of
 * {@link ScheduledRouteData} and {@link FareSearchRequestData} given as inputs
 */
public interface FareSearchPipelineManager
{

	/**
	 * Execute pipeline fare selection data.
	 *
	 * @param scheduledRoutes
	 * 		the scheduled routes
	 * @param fareSearchRequestData
	 * 		the fare search request data
	 * @return the fare selection data
	 */
	FareSelectionData executePipeline(List<ScheduledRouteData> scheduledRoutes, FareSearchRequestData fareSearchRequestData);
}
