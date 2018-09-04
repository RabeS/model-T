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

package de.hybris.platform.accommodationaddon.forms;

/**
 * The type Accommodation booking change date form.
 */
public class AccommodationBookingChangeDateForm
{
	private String checkInDateTime;

	private String checkOutDateTime;

	private String bookingReference;

	/**
	 * @return the checkInDateTime
	 */
	public String getCheckInDateTime()
	{
		return checkInDateTime;
	}

	/**
	 * @param checkInDateTime
	 *           the checkInDateTime to set
	 */
	public void setCheckInDateTime(final String checkInDateTime)
	{
		this.checkInDateTime = checkInDateTime;
	}

	/**
	 * @return the checkOutDateTime
	 */
	public String getCheckOutDateTime()
	{
		return checkOutDateTime;
	}

	/**
	 * @param checkOutDateTime
	 *           the checkOutDateTime to set
	 */
	public void setCheckOutDateTime(final String checkOutDateTime)
	{
		this.checkOutDateTime = checkOutDateTime;
	}

	/**
	 * @return the bookingReference
	 */
	public String getBookingReference()
	{
		return bookingReference;
	}

	/**
	 * @param bookingReference
	 *           the bookingReference to set
	 */
	public void setBookingReference(final String bookingReference)
	{
		this.bookingReference = bookingReference;
	}

	
}
