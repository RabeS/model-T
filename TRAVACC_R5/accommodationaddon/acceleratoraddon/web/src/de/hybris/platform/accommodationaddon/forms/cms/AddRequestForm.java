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

package de.hybris.platform.accommodationaddon.forms.cms;

/**
 * The type Add request form.
 */
public class AddRequestForm
{
	private String requestMessage;
	private int roomStayRefNumber;

	public String getRequestMessage()
	{
		return requestMessage;
	}

	public void setRequestMessage(final String requestMessage)
	{
		this.requestMessage = requestMessage;
	}

	public int getRoomStayRefNumber()
	{
		return roomStayRefNumber;
	}

	public void setRoomStayRefNumber(final int roomStayRefNumber)
	{
		this.roomStayRefNumber = roomStayRefNumber;
	}


}
