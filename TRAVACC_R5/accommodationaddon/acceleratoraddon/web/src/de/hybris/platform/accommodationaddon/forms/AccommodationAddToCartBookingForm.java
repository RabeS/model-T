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

import de.hybris.platform.travelfacades.facades.accommodation.forms.AccommodationAddToCartForm;

import java.util.List;


/**
 * The type Accommodation add to cart booking form.
 */
public class AccommodationAddToCartBookingForm
{
	List<AccommodationAddToCartForm> accommodationAddToCartForms;

	/**
	 * @return the accommodationAddToCartForms
	 */
	public List<AccommodationAddToCartForm> getAccommodationAddToCartForms()
	{
		return accommodationAddToCartForms;
	}

	/**
	 * @param accommodationAddToCartForms
	 *           the accommodationAddToCartForms to set
	 */
	public void setAccommodationAddToCartForms(final List<AccommodationAddToCartForm> accommodationAddToCartForms)
	{
		this.accommodationAddToCartForms = accommodationAddToCartForms;
	}
}
