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
package de.hybris.platform.travelseatmapservices.seatmap.response.handlers;

import de.hybris.platform.travelseatmapservices.seatmap.response.SeatMapJSONObject;


/**
 * This is used to handle the populating logic for {@link=SeatMapJSONObject}
 */
public interface SeatmapJsonObjectHandler
{
	/**
	 * This populates the seatMapJsonObject with the SVG and CSS based on the files on seatMapPath
	 *
	 * @param seatMapPath
	 * @param seatMapJsonObject
	 */
	void handle(String seatMapPath, SeatMapJSONObject seatMapJsonObject);
}
