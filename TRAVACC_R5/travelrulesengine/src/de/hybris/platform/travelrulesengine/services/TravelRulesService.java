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

package de.hybris.platform.travelrulesengine.services;

import de.hybris.platform.commercefacades.accommodation.AccommodationAvailabilityRequestData;
import de.hybris.platform.commercefacades.accommodation.AccommodationReservationData;
import de.hybris.platform.commercefacades.accommodation.AccommodationSearchResponseData;
import de.hybris.platform.commercefacades.accommodation.RoomStayData;
import de.hybris.platform.commercefacades.travel.FareProductData;
import de.hybris.platform.commercefacades.travel.FareSearchRequestData;
import de.hybris.platform.commercefacades.travel.TransportOfferingData;
import de.hybris.platform.commercefacades.travel.ancillary.data.OfferRequestData;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.travelrulesengine.enums.RefundActionType;

import java.util.List;


/**
 * Interface for Travel rules functionalities.
 */
public interface TravelRulesService
{

	/**
	 * Method to evaluate cart with rules to add fees and discounts.
	 *
	 * @param cart
	 * 		the cart
	 * @return cart entry
	 */
	List<CartEntryModel> evaluateCart(CartModel cart);

	/**
	 * Method to get total fee eg:admin fee after evaluating the rules.
	 *
	 * @return total fee
	 */
	double getTotalFee();

	/**
	 * Method to get refund action after rules evaluation
	 *
	 * @param orderModel
	 * 		the order model
	 * @return refund action type
	 */
	RefundActionType getRefundAction(OrderModel orderModel);

	/**
	 * Method to filter out the fare products that match rules
	 *
	 * @param fareProducts
	 * 		the fare products
	 * @param fareSearchRequest
	 * 		the fare search request
	 * @return list of fare products excluded by rules evaluation
	 */
	List<FareProductData> filterFareProducts(List<FareProductData> fareProducts, FareSearchRequestData fareSearchRequest);

	/**
	 * Method to filter out bundles excluded by rules evaluation
	 *
	 * @param fareSearchRequestData
	 * 		the fare search request data
	 * @param currentUser
	 * 		the current user
	 * @return list of bundle types excluded by rules evaluation
	 * @deprecated since version 4.0
	 */
	@Deprecated
	List<String> filterBundles(FareSearchRequestData fareSearchRequestData, UserModel currentUser);

	/**
	 * Method to show bundle templates returned from rule evaluation
	 *
	 * @param fareSearchRequestData
	 * 		the fare search request data
	 * @return list of bundle templates returned from rules evaluation
	 */
	List<String> showBundleTemplates(FareSearchRequestData fareSearchRequestData);

	/**
	 * Method to filter out transport offerings that matches rules
	 *
	 * @param transportOfferings
	 * 		the transport offerings
	 * @param fareSearchRequest
	 * 		the fare search request
	 * @return
	 */
	void filterTransportOfferings(List<TransportOfferingData> transportOfferings, FareSearchRequestData fareSearchRequest);

	/**
	 * Method to show products returned after evaluating rules
	 *
	 * @param offerRequestData
	 * 		the offer request data
	 * @return list of product codes to be shown as per the rules
	 */
	List<String> showProducts(OfferRequestData offerRequestData);

	/**
	 * Method to show product categories returned after evaluating rules
	 *
	 * @param offerRequestData
	 * 		the offer request data
	 * @return list of product codes to be shown as per the rules
	 */
	List<String> showProductCategories(OfferRequestData offerRequestData);

	/**
	 * Show extra products list.
	 *
	 * @param reservationData
	 * 		the reservation data
	 * @return the list
	 */
	List<String> showExtraProducts(AccommodationReservationData reservationData );

	/**
	 * Show accommodation offerings.
	 *
	 * @param accommodationSearchResponseData
	 * 		the accommodation search response data
	 * @return the list
	 */
	List<String> showAccommodationOfferings(AccommodationSearchResponseData accommodationSearchResponseData);

	/**
	 * Show accommodation categories list.
	 *
	 * @param availabilityRequestData
	 * 		the availability request data
	 * @return the list
	 */
	List<String> showAccommodationCategories(AccommodationAvailabilityRequestData availabilityRequestData);

	/**
	 * Show Rate Plans.
	 *
	 * @param availabilityRequestData
	 * 		the accommodation availability request data
	 * @param roomStayData
	 * 		the room stay data
	 * @return the list of rate plans
	 */
	List<String> showRatePlans(AccommodationAvailabilityRequestData availabilityRequestData, RoomStayData roomStayData);

	/**
	 * Show accommodations list.
	 *
	 * @param accommodationAvailabilityRequestData
	 * 		the accommodation availability request data
	 * @param roomStayData
	 * 		the room stay data
	 * @return the list
	 */
	List<String> showAccommodations(AccommodationAvailabilityRequestData accommodationAvailabilityRequestData, RoomStayData roomStayData);
}
