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

import java.util.List;


/**
 * The type Guest details.
 */
public class GuestDetails
{
	private List<LeadGuestDetailsForm> leadForms;

	public List<LeadGuestDetailsForm> getLeadForms()
	{
		return leadForms;
	}

	public void setLeadForms(final List<LeadGuestDetailsForm> leadForms)
	{
		this.leadForms = leadForms;
	}
}
