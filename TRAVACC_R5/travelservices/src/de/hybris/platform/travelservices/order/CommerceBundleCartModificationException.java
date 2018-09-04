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
package de.hybris.platform.travelservices.order;

import de.hybris.platform.commerceservices.order.CommerceCartModificationException;


/**
 * The type Commerce bundle cart modification exception.
 */
public class CommerceBundleCartModificationException extends CommerceCartModificationException
{
	/**
	 * Instantiates a new Commerce bundle cart modification exception.
	 *
	 * @param message
	 * 		the message
	 */
	public CommerceBundleCartModificationException(final String message)
	{
		super(message);
	}

	/**
	 * Instantiates a new Commerce bundle cart modification exception.
	 *
	 * @param message
	 * 		the message
	 * @param cause
	 * 		the cause
	 */
	public CommerceBundleCartModificationException(final String message, final Throwable cause)
	{
		super(message, cause);
	}
}
