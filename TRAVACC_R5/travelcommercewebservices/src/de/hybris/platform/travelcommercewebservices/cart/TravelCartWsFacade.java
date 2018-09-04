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
package de.hybris.platform.travelcommercewebservices.cart;

import de.hybris.platform.commercefacades.travel.AddBundleToCartRequestData;
import de.hybris.platform.travelcommercewebservices.cart.data.AccommodationToCartRequestData;
import de.hybris.platform.travelcommercewebservices.cart.data.AddToCartRequestData;
import de.hybris.platform.travelcommercewebservices.cart.data.AddToCartResultData;
import de.hybris.platform.travelfacades.order.TravelCartFacade;


/**
 * Facade for add to cart cart functionality
 */
public interface TravelCartWsFacade extends TravelCartFacade
{
	/**
	 * Method to add bundle products to the cart.
	 *
	 * @param addBundleToCartRequestData
	 * @return {@link AddToCartResultData}
	 */
	public AddToCartResultData addBundle(AddBundleToCartRequestData addBundleToCartRequestData);

	/**
	 * Method to add ancillary products to the cart.
	 *
	 * @param addToCartRequestData
	 * @return {@link AddToCartResultData}
	 */
	public AddToCartResultData addAncillary(AddToCartRequestData addToCartRequestData);

	/**
	 * Method to add accommodation to the cart.
	 *
	 * @param accommodationToCartRequestData
	 * @return {@link AddToCartResultData}
	 */
	public AddToCartResultData addSelectedAccommodationsToCart(AccommodationToCartRequestData accommodationToCartRequestData);

	/**
	 * Sets booking type journey to cart.
	 *
	 * @param bookingJourney
	 * 		the booking journey type
	 */
	void setBookingJourneyTypeToCart(String bookingJourney);
}