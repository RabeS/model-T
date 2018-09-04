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
package de.hybris.platform.travelseatmapservices.jsonresponse.manager;

import de.hybris.platform.travelseatmapservices.seatmap.response.SeatMapJSONObject;


/**
 * Pipeline Manager class that will return a {@link SeatMapJSONObject} after executing a list of handlers.
 */
public interface SeatMapJsonResponsePipelineManager
{
	/**
	 * Execute pipeline seatMapJSONObject.
	 */
	SeatMapJSONObject executePipeline(String vehicleCode);

}
