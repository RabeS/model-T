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

package de.hybris.platform.traveladdon.forms;

import java.util.List;

import javax.validation.Valid;


/**
 * The type Traveller details.
 */
public class TravellerDetails
{
	@Valid
	private List<TravellerForm> travellerForms;

	private Boolean additionalSecurityActive;

	public List<TravellerForm> getTravellerForms()
	{
		return travellerForms;
	}

	public void setTravellerForms(final List<TravellerForm> travellerForms)
	{
		this.travellerForms = travellerForms;
	}

	public Boolean getAdditionalSecurityActive()
	{
		return additionalSecurityActive;
	}

	public void setAdditionalSecurityActive(final Boolean additionalSecurityActive)
	{
		this.additionalSecurityActive = additionalSecurityActive;
	}
}
