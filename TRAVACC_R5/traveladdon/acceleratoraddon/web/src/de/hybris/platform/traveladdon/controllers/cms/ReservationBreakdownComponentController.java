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

package de.hybris.platform.traveladdon.controllers.cms;

import de.hybris.platform.commercefacades.travel.TravellerData;
import de.hybris.platform.commercefacades.travel.reservation.data.ReservationData;
import de.hybris.platform.commercefacades.travel.reservation.data.ReservationItemData;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.travelacceleratorstorefront.controllers.cms.SubstitutingCMSAddOnComponentController;
import de.hybris.platform.traveladdon.constants.TraveladdonWebConstants;
import de.hybris.platform.traveladdon.controllers.TraveladdonControllerConstants;
import de.hybris.platform.traveladdon.model.components.ReservationBreakdownComponentModel;
import de.hybris.platform.travelfacades.constants.TravelfacadesConstants;
import de.hybris.platform.travelfacades.facades.BookingFacade;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * The type Reservation breakdown component controller.
 */
@Controller("ReservationBreakdownComponentController")
@RequestMapping(value = TraveladdonControllerConstants.Actions.Cms.ReservationBreakdownComponent)
public class ReservationBreakdownComponentController
		extends SubstitutingCMSAddOnComponentController<ReservationBreakdownComponentModel>
{
	protected final String GROUP_BOOKING = "isGroupBooking";

	@Resource(name = "bookingFacade")
	private BookingFacade bookingFacade;

	@Resource(name = "sessionService")
	private SessionService sessionService;

	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final ReservationBreakdownComponentModel component)
	{
		final String bookingReference = sessionService.getAttribute("bookingConfirmationReference");
		if (StringUtils.isNotBlank(bookingReference))
		{
			final ReservationData reservationData = bookingFacade.getBookingByBookingReferenceAndAmendingOrder(bookingReference);
			if (Objects.nonNull(reservationData))
			{
				final Optional<ReservationItemData> optionalReservationItem = reservationData.getReservationItems().stream()
						.findFirst();
				if (optionalReservationItem.isPresent())
				{
					final List<TravellerData> travellers = optionalReservationItem.get().getReservationItinerary().getTravellers();
					model.addAttribute(GROUP_BOOKING, CollectionUtils.size(travellers) >= getConfigurationService().getConfiguration()
							.getInt(TravelfacadesConstants.GROUP_BOOKING_THRESHOLD));
				}

				model.addAttribute(TraveladdonWebConstants.ADDITIONAL_SECURITY,
						bookingFacade.isAdditionalSecurityActive(bookingReference));
			}
			model.addAttribute(TraveladdonWebConstants.RESERVATION, reservationData);
			model.addAttribute(TraveladdonWebConstants.AMEND, bookingFacade.isAmendment(bookingReference));
			model.addAttribute(TraveladdonWebConstants.HIDE_BUTTON, "HIDE");
			model.addAttribute(TraveladdonWebConstants.DISABLE_CURRENCY_SELECTOR, Boolean.TRUE);
		}
	}
}
