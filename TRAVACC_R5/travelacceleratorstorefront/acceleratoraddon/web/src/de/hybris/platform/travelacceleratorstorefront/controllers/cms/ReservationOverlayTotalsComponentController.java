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

package de.hybris.platform.travelacceleratorstorefront.controllers.cms;

import de.hybris.platform.travelacceleratorstorefront.constants.TravelacceleratorstorefrontControllerConstants;
import de.hybris.platform.travelacceleratorstorefront.constants.TravelacceleratorstorefrontWebConstants;
import de.hybris.platform.travelacceleratorstorefront.model.components.ReservationOverlayTotalsComponentModel;
import de.hybris.platform.travelfacades.order.TravelCartFacade;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * The type Reservation overlay totals component controller.
 */
@Controller("ReservationOverlayTotalsComponentController")
@RequestMapping(value = TravelacceleratorstorefrontControllerConstants.Actions.Cms.ReservationOverlayTotalsComponent)
public class ReservationOverlayTotalsComponentController
		extends SubstitutingCMSAddOnComponentController<ReservationOverlayTotalsComponentModel>
{
	private static final Logger LOGGER = Logger.getLogger(ReservationOverlayTotalsComponentController.class);

	@Resource(name = "cartFacade")
	private TravelCartFacade travelCartFacade;

	@Override
	protected void fillModel(final HttpServletRequest request, final Model model,
			final ReservationOverlayTotalsComponentModel component)
	{
		// Model is only populated when the component is shown
	}

	/**
	 * This method is responsible for populating itinerary component after see full reservation button is clicked
	 *
	 * @param componentUid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/load", method = RequestMethod.GET)
	protected String getComponent(@RequestParam final String componentUid, final HttpServletRequest request,
			final HttpServletResponse response, final Model model)
	{
		request.setAttribute(COMPONENT_UID, componentUid);
		try
		{
			final String view = handleGet(request, response, model);
			populateModel(model);
			return view;
		}
		catch (final Exception e)
		{
			LOGGER.error("Exception loading the component", e);
		}
		return StringUtils.EMPTY;
	}

	protected void populateModel(final Model model)
	{
		final String originalOrderCode = travelCartFacade.getOriginalOrderCode();
		model.addAttribute(TravelacceleratorstorefrontWebConstants.RESERVATION_TOTAL,
				travelCartFacade.getTotalsForReservation(originalOrderCode));

		final boolean isAmendmentCart = travelCartFacade.isAmendmentCart();
		model.addAttribute(TravelacceleratorstorefrontWebConstants.RESERVATION_CODE, isAmendmentCart ?
				originalOrderCode : travelCartFacade.getCurrentCartCode());
		model.addAttribute(TravelacceleratorstorefrontWebConstants.IS_AMENDED_CART, isAmendmentCart);
	}


}
