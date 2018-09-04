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
package de.hybris.platform.ndcservices.exceptions;

/**
 * The type Ndc exception.
 */
public class NDCException extends Exception
{
	/**
	 * Default constructor
	 */
	public NDCException()
	{
		super();
	}

	/**
	 * Instantiates a new Ndc exception.
	 *
	 * @param message
	 * 		the message
	 * @param cause
	 * 		the cause
	 */
	public NDCException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * Instantiates a new Ndc exception.
	 *
	 * @param message
	 * 		the message
	 */
	public NDCException(final String message)
	{
		super(message);
	}

	/**
	 * Instantiates a new Ndc exception.
	 *
	 * @param cause
	 * 		the cause
	 */
	public NDCException(final Throwable cause)
	{
		super(cause);
	}
}
