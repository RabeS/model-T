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

package de.hybris.platform.travelservices.accommodationmap.exception;

/**
 * The type Accommodation map data set up exception.
 */
public class AccommodationMapDataSetUpException extends RuntimeException
{

	/**
	 * Instantiates a new Accommodation map data set up exception.
	 */
	public AccommodationMapDataSetUpException()
	{
		super();
	}

	/**
	 * Instantiates a new Accommodation map data set up exception.
	 *
	 * @param message
	 * 		the message
	 */
	public AccommodationMapDataSetUpException(final String message)
	{
		super(message);
	}

}
