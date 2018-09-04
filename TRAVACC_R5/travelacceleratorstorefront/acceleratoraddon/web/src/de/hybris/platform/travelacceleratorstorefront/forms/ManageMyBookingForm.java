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
/**
 * Form class to hold the details of ManageMyBookingForm.
 */
package de.hybris.platform.travelacceleratorstorefront.forms;

/**
 * The type Manage my booking form.
 */
public class ManageMyBookingForm
{
	private String bookingReference;

	private String lastName;

	public String getBookingReference()
	{
		return bookingReference;
	}

	public void setBookingReference(final String bookingReference)
	{
		this.bookingReference = bookingReference;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(final String lastName)
	{
		this.lastName = lastName;
	}

}
