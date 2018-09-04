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

package de.hybris.platform.travelcommons.forms.cms;

import de.hybris.platform.commercefacades.travel.ItineraryPricingInfoData;
import de.hybris.platform.commercefacades.travel.PassengerTypeQuantityData;

import java.util.List;


/**
 * The type Add deal to cart form.
 */
public class AddDealToCartForm
{
	private String bundleTemplateID;
	private List<ItineraryPricingInfoData> itineraryPricingInfos;
	private List<PassengerTypeQuantityData> passengerTypes;
	private String startingDate;
	private String endingDate;

	public String getBundleTemplateID()
	{
		return bundleTemplateID;
	}

	public void setBundleTemplateID(final String bundleTemplateID)
	{
		this.bundleTemplateID = bundleTemplateID;
	}

	public List<ItineraryPricingInfoData> getItineraryPricingInfos()
	{
		return itineraryPricingInfos;
	}

	public void setItineraryPricingInfos(final List<ItineraryPricingInfoData> itineraryPricingInfos)
	{
		this.itineraryPricingInfos = itineraryPricingInfos;
	}

	public List<PassengerTypeQuantityData> getPassengerTypes()
	{
		return passengerTypes;
	}

	public void setPassengerTypes(final List<PassengerTypeQuantityData> passengerTypes)
	{
		this.passengerTypes = passengerTypes;
	}

	public String getStartingDate()
	{
		return startingDate;
	}

	public void setStartingDate(final String startingDate)
	{
		this.startingDate = startingDate;
	}

	public String getEndingDate()
	{
		return endingDate;
	}

	public void setEndingDate(final String endingDate)
	{
		this.endingDate = endingDate;
	}


}
