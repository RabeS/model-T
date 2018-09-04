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

package de.hybris.platform.accommodationaddon.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.accommodationaddon.constants.AccommodationaddonWebConstants;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.accommodation.AccommodationReservationData;
import de.hybris.platform.commercefacades.accommodation.AvailableServiceData;
import de.hybris.platform.commercefacades.accommodation.ReservedRoomStayData;
import de.hybris.platform.commercefacades.accommodation.RoomPreferenceData;
import de.hybris.platform.commercefacades.accommodation.RoomStayData;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.travelacceleratorstorefront.constants.TravelacceleratorstorefrontWebConstants;
import de.hybris.platform.travelacceleratorstorefront.controllers.pages.TravelAbstractPageController;
import de.hybris.platform.travelfacades.constants.TravelfacadesConstants;
import de.hybris.platform.travelfacades.facades.BookingFacade;
import de.hybris.platform.travelfacades.facades.accommodation.AccommodationExtrasFacade;
import de.hybris.platform.travelfacades.facades.accommodation.RoomPreferenceFacade;
import de.hybris.platform.travelfacades.order.TravelCartFacade;
import de.hybris.platform.travelservices.enums.OrderEntryType;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller for Accommodation Extras page
 */
@Controller
@RequestMapping({ "/extras", "/manage-booking/extras" })
public class AccommodationExtrasPageController extends TravelAbstractPageController
{
	private static final String HOME_PAGE_PATH = "/";
	private static final String ACCOMMODATION_EXTRAS_PAGE = "accommodationExtrasPage";
	private static final String CHECKOUT_LOGIN = "/login/checkout";
	private static final String BOOKING_DETAILS_URL = "/manage-booking/booking-details/";
	private static final String NEXT_PAGE_URL = "nextPageURL";
	private static final String EXTRAS_PATH = "/extras/next";
	private static final String EXTRAS_AMENDMENT_PATH = "/manage-booking/extras/next";

	@Resource(name = "accommodationExtrasFacade")
	private AccommodationExtrasFacade accommodationExtrasFacade;

	@Resource(name = "userFacade")
	private UserFacade userFacade;

	@Resource(name = "bookingFacade")
	BookingFacade bookingFacade;

	@Resource(name = "roomPreferenceFacade")
	RoomPreferenceFacade roomPreferenceFacade;

	@Resource(name = "cartFacade")
	private TravelCartFacade travelCartFacade;

	@Resource(name = "sessionService")
	private SessionService sessionService;


	@RequestMapping(method = RequestMethod.GET)
	public String getAccommodationExtrasPage(
			@RequestParam(value = "roomStay", required = false) final Integer roomStayRefNumberToUpdate,
			final Model model, final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		if (!travelCartFacade.isCurrentCartValid())
		{
			return REDIRECT_PREFIX + HOME_PAGE_PATH;
		}

		if (!bookingFacade.isCurrentCartOfType(OrderEntryType.ACCOMMODATION.getCode()))
		{
			return REDIRECT_PREFIX + HOME_PAGE_PATH;
		}

		sessionService.removeAttribute(TravelacceleratorstorefrontWebConstants.ACCOMMODATION_OFFERING_CODE);

		final AccommodationReservationData accommodationReservationData = bookingFacade
				.getAccommodationReservationDataForGuestDetailsFromCart();
		if (travelCartFacade.isAmendmentCart())
		{
			// During amendments, if there are any new rooms added, it means that Add Room amendment is performed and therefore we
			// only want to allow users to modify details about these new rooms
			final List<ReservedRoomStayData> newReservedRoomStays = bookingFacade.getNewReservedRoomStays();
			if (CollectionUtils.isNotEmpty(newReservedRoomStays))
			{
				accommodationReservationData.setRoomStays(newReservedRoomStays);
			}
		}

		if (Objects.nonNull(roomStayRefNumberToUpdate))
		{
			final Optional<ReservedRoomStayData> roomStayToUpdate = accommodationReservationData.getRoomStays().stream()
					.filter(roomStay -> roomStayRefNumberToUpdate.equals(roomStay.getRoomStayRefNumber())).findAny();
			if (roomStayToUpdate.isPresent())
			{
				accommodationReservationData.setRoomStays(Collections.singletonList(roomStayToUpdate.get()));
			}
		}

		final List<AvailableServiceData> availableServices = accommodationExtrasFacade
				.getAvailableServices(accommodationReservationData);

		model.addAttribute(AccommodationaddonWebConstants.ACCOMMODATION_RESERVATION_DATA, accommodationReservationData);
		model.addAttribute(AccommodationaddonWebConstants.AVAILABLE_SERVICES, availableServices);

		final Map<Integer, List<RoomPreferenceData>> accommodationRoomPreferenceMap = createAccommodationRoomPreferenceMap(
				accommodationReservationData);
		model.addAttribute(AccommodationaddonWebConstants.ACCOMMODATION_ROOM_PREFERENCE_MAP, accommodationRoomPreferenceMap);

		final String sessionBookingJourney = sessionService
				.getAttribute(TravelacceleratorstorefrontWebConstants.SESSION_BOOKING_JOURNEY);
		if (sessionBookingJourney != null)
		{
			model.addAttribute(TravelacceleratorstorefrontWebConstants.BOOKING_JOURNEY, sessionBookingJourney);
		}
		else
		{
			sessionService.setAttribute(TravelacceleratorstorefrontWebConstants.SESSION_BOOKING_JOURNEY,
					TravelfacadesConstants.BOOKING_ACCOMMODATION_ONLY);
			model.addAttribute(TravelacceleratorstorefrontWebConstants.BOOKING_JOURNEY,
					TravelfacadesConstants.BOOKING_ACCOMMODATION_ONLY);
		}

		model.addAttribute(TravelacceleratorstorefrontWebConstants.AMEND, travelCartFacade.isAmendmentCart());
		model.addAttribute(NEXT_PAGE_URL, determineNextUrl());

		storeCmsPageInModel(model, getContentPageForLabelOrId(ACCOMMODATION_EXTRAS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ACCOMMODATION_EXTRAS_PAGE));
		return getViewForPage(model);

	}

	/**
	 * This method determines the next url of checkout flow. If the context is purchase flow, then the next url is
	 * "/extras/next" else if the context is amendment then the next url is "/manage-booking/extras/next".
	 *
	 * @return next url
	 */
	protected String determineNextUrl()
	{
		return travelCartFacade.isAmendmentCart() ? EXTRAS_AMENDMENT_PATH : EXTRAS_PATH;
	}
	
	/**
	 * Redirects user to the next checkout page which is guest details (or checkout login)
	 *
	 * @return next page
	 */
	@RequestMapping(value = "/next", method = RequestMethod.GET)
	public String nextPage()
	{
		if (userFacade.isAnonymousUser() && !travelCartFacade.isAmendmentCart())
		{
			return REDIRECT_PREFIX + CHECKOUT_LOGIN;
		}
		if (travelCartFacade.isAmendmentCart())
		{
			if (!travelCartFacade.hasCartBeenAmended())
			{
				return REDIRECT_PREFIX + BOOKING_DETAILS_URL + travelCartFacade.getOriginalOrderCode();
			}
			if (userFacade.isAnonymousUser())
			{
				getSessionService().setAttribute(WebConstants.ANONYMOUS_CHECKOUT, Boolean.TRUE);
			}
		}
		final String sessionBookingJourney = getSessionService()
				.getAttribute(TravelacceleratorstorefrontWebConstants.SESSION_BOOKING_JOURNEY);
		if (StringUtils.isNotEmpty(sessionBookingJourney) && StringUtils
				.equalsIgnoreCase(sessionBookingJourney, TravelfacadesConstants.BOOKING_TRANSPORT_ACCOMMODATION))
		{
			return REDIRECT_PREFIX + TravelacceleratorstorefrontWebConstants.TRAVELLER_DETAILS_PATH;
		}
		return REDIRECT_PREFIX + TravelacceleratorstorefrontWebConstants.GUEST_DETAILS_PATH;
	}

	protected Map<Integer, List<RoomPreferenceData>> createAccommodationRoomPreferenceMap(
			final AccommodationReservationData accommodationReservationData)
	{
		final Map<Integer, List<RoomPreferenceData>> baseAccommodationRoomPreferenceMap = accommodationReservationData
				.getRoomStays()
				.stream().collect(Collectors.toMap(RoomStayData::getRoomStayRefNumber, roomStay -> roomPreferenceFacade
						.getRoomPreferencesForTypeAndAccommodation(AccommodationaddonWebConstants.ACCOMMODATION_ROOM_PREFERENCE_TYPE,
								roomStay.getRoomTypes())));

		final OptionalInt min = baseAccommodationRoomPreferenceMap.keySet().stream().mapToInt(Integer::intValue).min();
		if (!min.isPresent())
		{
			return baseAccommodationRoomPreferenceMap;
		}

		final Map<Integer, List<RoomPreferenceData>> finalAccommodationRoomPreferenceMap = new HashMap<>();
		baseAccommodationRoomPreferenceMap
				.forEach((key, value) -> finalAccommodationRoomPreferenceMap.put(key - min.getAsInt(), value));

		return finalAccommodationRoomPreferenceMap;
	}

}
