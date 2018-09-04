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

package de.hybris.platform.travelservices.exceptions;

/**
 * The type Accommodation pipeline exception.
 */
public class AccommodationPipelineException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public AccommodationPipelineException(final String message, final Throwable nested)
	{
		super(message, nested);
	}

	public AccommodationPipelineException(final String message)
	{
		super(message);
	}
}