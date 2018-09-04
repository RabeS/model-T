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

package de.hybris.platform.travelcommercewebservices.v2.controller.travel;



import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.travel.BookingActionData;
import de.hybris.platform.commercefacades.travel.BookingActionRequestData;
import de.hybris.platform.commercefacades.travel.BookingActionResponseData;
import de.hybris.platform.commercefacades.travel.CheckInRequestData;
import de.hybris.platform.commercefacades.travel.CheckInResponseData;
import de.hybris.platform.commercefacades.travel.PassengerInformationData;
import de.hybris.platform.commercefacades.travel.TravellerData;
import de.hybris.platform.commercefacades.travel.TravellerTransportOfferingInfoData;
import de.hybris.platform.commercefacades.travel.enums.ActionTypeOption;
import de.hybris.platform.commercefacades.travel.order.PaymentTransactionData;
import de.hybris.platform.commercefacades.travel.reservation.data.ReservationData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commercewebservicescommons.dto.product.PriceWsDTO;
import de.hybris.platform.commercewebservicescommons.strategies.CartLoaderStrategy;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.travelcommercewebservices.cart.TravelCartWsFacade;
import de.hybris.platform.travelcommercewebservices.exceptions.NoCheckoutCartException;
import de.hybris.platform.travelcommercewebservices.exceptions.PaymentAuthorizationException;
import de.hybris.platform.travelcommercewebservices.facades.booking.TravelBookingWsFacade;
import de.hybris.platform.travelcommercewebservices.facades.order.TravelOrderWsFacade;
import de.hybris.platform.travelfacades.facades.ActionFacade;
import de.hybris.platform.travelfacades.facades.CheckInFacade;
import de.hybris.platform.travelfacades.facades.TravelCommercePriceFacade;
import de.hybris.platform.travelfacades.facades.TravellerFacade;
import de.hybris.platform.webservicescommons.dto.error.ErrorWsDTO;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.travelcommercewebservicescommons.dto.CancelBookingResponseWsDTO;
import de.hybris.travelcommercewebservicescommons.dto.CheckInRequestWsDTO;
import de.hybris.travelcommercewebservicescommons.dto.CheckInResponseWsDTO;
import de.hybris.travelcommercewebservicescommons.dto.PlaceOrderRequestWsDTO;
import de.hybris.travelcommercewebservicescommons.dto.TravellerTransportOfferingInfoWsDTO;
import de.hybris.travelcommercewebservicescommons.dto.TravellerWsDTO;
import de.hybris.travelcommercewebservicescommons.dto.reservation.RemovedTravellerResponseWsDTO;
import de.hybris.travelcommercewebservicescommons.dto.reservation.ReservationWsDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


/**
 * Order Controller
 */
@Controller
@RequestMapping(value = "/{baseSiteId}/users/{userId}/orders")
@Api(tags = "Orders")
public class TravelOrdersController extends BaseCommerceController
{
	private static final Logger LOG = LoggerFactory.getLogger(TravelCartsController.class);
	private static final String NOT_ALLOWED_CHECK_IN_ERROR = "NotAllowedCheckInError";
	private static final String CHECK_IN_WINDOW_ERROR = "CheckInWindowError";
	private static final String INVALID_TRAVELLER_UID_ERROR = "InvalidTravellerUidError";
	private static final String INVALID_REQUEST_ERROR = "InvalidRequestError";

	@Resource(name = "travelBookingWsFacade")
	private TravelBookingWsFacade travelBookingWsFacade;

	@Resource(name = "cartLoaderStrategy")
	private CartLoaderStrategy cartLoaderStrategy;

	@Resource(name = "travelOrderWsFacade")
	private TravelOrderWsFacade travelOrderWsFacade;

	@Resource(name = "checkInFacade")
	private CheckInFacade checkInFacade;

	@Resource(name = "actionFacade")
	private ActionFacade actionFacade;

	@Resource(name = "travelCommercePriceFacade")
	private TravelCommercePriceFacade travelCommercePriceFacade;

	@Resource(name = "travellerFacade")
	private TravellerFacade travellerFacade;

	@Resource(name = "travelCartWsFacade")
	private TravelCartWsFacade travelCartWsFacade;

	/**
	 * Gets reservation details.
	 *
	 * @param bookingReference
	 * 		the booking reference
	 * @return the reservation details
	 */
	@RequestMapping(value = "/{bookingReference}/reservationDetails", method = RequestMethod.POST, produces = MediaType
			.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "Get Reservation Details", notes = "Get Reservation Details", response = ReservationWsDTO.class,
			produces = "application/json")
	@ApiResponses(@ApiResponse(code = 200, message = "Success", response = ReservationWsDTO.class))
	@ApiImplicitParams({ @ApiImplicitParam(name = "baseSiteId", required = true, dataType = "String", paramType = "path"),
			@ApiImplicitParam(name = "userId", required = true, dataType = "String", paramType = "path"),
			@ApiImplicitParam(name = "bookingReference", required = true, dataType = "String", paramType = "path") })
	public ResponseEntity<ReservationWsDTO> getReservationDetails(
			@ApiParam(value = "Booking Reference", required = true) @PathVariable final String bookingReference)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("getReservationDetails: code=" + sanitize(bookingReference));
		}

		final ReservationData reservationData = travelBookingWsFacade.getBookingByBookingReference(bookingReference);

		if (Objects.isNull(reservationData))
		{
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(getDataMapper().map(reservationData, ReservationWsDTO.class), HttpStatus.OK);
	}

	@Secured({ "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/users/{bookingReference}/orders", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Create Order", notes = "Creates a new customer order", response = ReservationWsDTO.class, produces =
			"application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "Success", response = ReservationWsDTO.class) })
	@ApiImplicitParams({ @ApiImplicitParam(name = "cartId", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "securityCode", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "baseSiteId", required = true, dataType = "String", paramType = "path"),
			@ApiImplicitParam(name = "userId", required = true, dataType = "String", paramType = "path"),
			@ApiImplicitParam(name = "placeOrderRequestWsDTO", required = false, dataType = "PlaceOrderRequestWsDTO", paramType = "body") })

	@ResponseBody
	public ResponseEntity<ReservationWsDTO> placeOrder(@RequestParam(required = true) final String cartId,
			@RequestParam(required = false) final String securityCode,
			@RequestBody(required = false) final PlaceOrderRequestWsDTO placeOrderRequestWsDTO)
			throws PaymentAuthorizationException, InvalidCartException, WebserviceValidationException, NoCheckoutCartException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.info("placeOrder for cart " + cartId);
		}

		if (Objects.nonNull(placeOrderRequestWsDTO) && CollectionUtils
				.isNotEmpty(placeOrderRequestWsDTO.getAssociatedTransactions()))
		{
			travelBookingWsFacade
					.addPaymentTransactionsToSession(getDataMapper().mapAsList(placeOrderRequestWsDTO.getAssociatedTransactions(),
							PaymentTransactionData.class, null));
		}

		cartLoaderStrategy.loadCart(cartId, false);

		boolean isCartValid = false;
		// Validates cart
		try
		{
			isCartValid = isValidCart();
		}
		catch (final InvalidCartException e)
		{
			LOG.error("Invalid cart exception ", e);
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}

		final ReservationData reservationData = isCartValid ? travelOrderWsFacade.placeOrder(securityCode) : null;

		if (Objects.isNull(reservationData))
		{
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(getDataMapper().map(reservationData, ReservationWsDTO.class), HttpStatus.CREATED);
	}

	/**
	 * Cancel the booking.
	 *
	 * @param bookingReference
	 * 		the order id
	 * @param userId
	 * 		the user id
	 * @return the response entity
	 */
	@Secured({ "ROLE_CUSTOMERGROUP", "ROLE_GUEST", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/{bookingReference}/cancelBooking", method = RequestMethod.POST, produces = MediaType
			.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "Cancel the booking", notes = "Cancel the booking", response = CancelBookingResponseWsDTO.class,
			produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "Success", response = CancelBookingResponseWsDTO.class),
			@ApiResponse(code = 422, message = "Unprocessable Entity", response = ResponseEntity.class),
			@ApiResponse(code = 204, message = "No Content", response = ResponseEntity.class) })
	@ApiImplicitParams({ @ApiImplicitParam(name = "baseSiteId", required = true, dataType = "String", paramType = "path"),
			@ApiImplicitParam(name = "userId", required = true, dataType = "String", paramType = "path"),
			@ApiImplicitParam(name = "bookingReference", required = true, dataType = "String", paramType = "path") })
	public ResponseEntity<CancelBookingResponseWsDTO> cancelBooking(
			@ApiParam(value = "Booking Reference", required = true) @PathVariable final String bookingReference,
			@ApiParam(value = "User Id", required = true) @PathVariable final String userId)
	{
		try
		{
			final ReservationData reservationData = travelBookingWsFacade.getBookingByBookingReference(bookingReference);
			if (Objects.isNull(reservationData))
			{
				final CancelBookingResponseWsDTO cancelBookingResponseWsDTO = buildCancelBookingResponse(bookingReference, null,
						Collections.singletonList("No Order For bookingReference: " + bookingReference));
				return new ResponseEntity<>(cancelBookingResponseWsDTO, HttpStatus.NO_CONTENT);
			}
			final BookingActionResponseData bookingActionResponseData = validateBookingAction(reservationData, userId,
					ActionTypeOption.CANCEL_BOOKING);

			if (!bookingActionResponseData.getBookingActions().stream().allMatch(BookingActionData::isEnabled))
			{
				final CancelBookingResponseWsDTO cancelBookingResponseWsDTO = buildCancelBookingResponse(bookingReference, null,
						Collections.singletonList("Cancellation Not Allowed"));
				return new ResponseEntity<>(cancelBookingResponseWsDTO, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			if (!travelBookingWsFacade.isCancelPossible(bookingReference))
			{
				final CancelBookingResponseWsDTO cancelBookingResponseWsDTO = buildCancelBookingResponse(bookingReference, null,
						Collections.singletonList("Cancellation Not Possible"));
				return new ResponseEntity<>(cancelBookingResponseWsDTO, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			if (!travelBookingWsFacade.cancelOrder(bookingReference))
			{
				final CancelBookingResponseWsDTO cancelBookingResponseWsDTO = buildCancelBookingResponse(bookingReference, null,
						Collections.singletonList("Error Occurred During Cancellation"));
				return new ResponseEntity<>(cancelBookingResponseWsDTO, HttpStatus.UNPROCESSABLE_ENTITY);
			}

			final PriceData priceData = travelBookingWsFacade.getRefundTotal(bookingReference);
			final CancelBookingResponseWsDTO cancelBookingResponseWsDTO = buildCancelBookingResponse(bookingReference, priceData,
					null);
			return new ResponseEntity<>(cancelBookingResponseWsDTO, HttpStatus.OK);

		}
		catch (final ModelNotFoundException e)
		{
			LOG.error("Order with bookingReference " + bookingReference + " not found for current user in current BaseStore");
		}

		final CancelBookingResponseWsDTO cancelBookingResponseWsDTO = buildCancelBookingResponse(bookingReference, null,
				Collections.singletonList("No Order For bookingReference: " + bookingReference));
		return new ResponseEntity<>(cancelBookingResponseWsDTO, HttpStatus.NO_CONTENT);
	}

	/**
	 * Check in travellers for a given booking reference and bound code.
	 *
	 * @param bookingReference
	 * 		the booking reference
	 * @param originDestinationRefNumber
	 * 		the origin destination ref number
	 * @param userId
	 * 		the user id
	 * @param checkInRequestWsDTO
	 * 		the check in request ws dto
	 * @return the response entity
	 */
	@Secured({ "ROLE_CUSTOMERGROUP", "ROLE_GUEST", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/{bookingReference}/check-in/{originDestinationRefNumber}", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "Check in", notes = "Check in", response = CheckInResponseWsDTO.class, produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "Success", response = CheckInResponseWsDTO.class),
			@ApiResponse(code = 204, message = "No Content", response = ResponseEntity.class),
			@ApiResponse(code = 422, message = "Unprocessable Entity", response = ResponseEntity.class) })
	@ApiImplicitParams({ @ApiImplicitParam(name = "baseSiteId", required = true, dataType = "String", paramType = "path"),
			@ApiImplicitParam(name = "userId", required = true, dataType = "String", paramType = "path"),
			@ApiImplicitParam(name = "bookingReference", required = true, dataType = "String", paramType = "path"),
			@ApiImplicitParam(name = "originDestinationRefNumber", required = true, dataType = "int", paramType = "path") })
	public ResponseEntity<CheckInResponseWsDTO> doCheckin(
			@ApiParam(value = "Booking Reference", required = true) @PathVariable final String bookingReference,
			@ApiParam(value = "Origin Destination Reference Number", required = true) @PathVariable final int
					originDestinationRefNumber,
			@ApiParam(value = "User Id", required = true) @PathVariable final String userId,
			@RequestBody final CheckInRequestWsDTO checkInRequestWsDTO)
	{
		final CheckInRequestData checkInRequestData = getDataMapper().map(checkInRequestWsDTO, CheckInRequestData.class);

		if (LOG.isDebugEnabled())
		{
			LOG.debug("doCheckin: bookingReference=" + sanitize(bookingReference) + ", originDestinationRefNumber="
					+ originDestinationRefNumber);
		}

		if (CollectionUtils.isEmpty(checkInRequestData.getTravellerTransportOfferingInfos()))
		{
			return new ResponseEntity<>(getDataMapper().map(createCheckInResponse(bookingReference, INVALID_REQUEST_ERROR,
					Collections.singletonList("Traveller Transport Offering Info cannot be empty")),
					CheckInResponseWsDTO.class), HttpStatus.UNPROCESSABLE_ENTITY);
		}

		final ReservationData reservationData = travelBookingWsFacade.getBookingByBookingReference(bookingReference);

		final BookingActionResponseData bookingActionResponseData = validateBookingAction(reservationData, userId,
				ActionTypeOption.CHECK_IN);
		if (Objects.isNull(bookingActionResponseData))
		{
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		for (final TravellerTransportOfferingInfoData travellerTransportOfferingInfoData : checkInRequestData
				.getTravellerTransportOfferingInfos())
		{

			if (!bookingActionResponseData.getBookingActions().stream()
					.filter(action -> action.getOriginDestinationRefNumber() == originDestinationRefNumber && StringUtils
							.equals(action.getTraveller().getUid(), travellerTransportOfferingInfoData.getTraveller().getUid()))
					.allMatch(BookingActionData::isEnabled))
			{
				return new ResponseEntity<>(getDataMapper().map(createCheckInResponse(bookingReference, NOT_ALLOWED_CHECK_IN_ERROR,
						Collections.singletonList("Check in not allowed for booking reference:" + bookingReference)),
						CheckInResponseWsDTO.class), HttpStatus.UNPROCESSABLE_ENTITY);
			}
		}
		if (!checkInFacade.isCheckInPossible(reservationData, originDestinationRefNumber))
		{
			return new ResponseEntity<>(getDataMapper().map(createCheckInResponse(bookingReference, CHECK_IN_WINDOW_ERROR,
					Collections.singletonList("Booking reference[" + bookingReference + "] not in valid Check In Window")),
					CheckInResponseWsDTO.class), HttpStatus.UNPROCESSABLE_ENTITY);
		}
		populateRequestDataWithTravellerInfo(checkInRequestData, checkInRequestWsDTO);

		final CheckInResponseData checkInResponseData = checkInFacade.doCheckin(checkInRequestData);
		checkInResponseData.setBookingReference(bookingReference);

		if (MapUtils.isNotEmpty(checkInResponseData.getErrors()))
		{
			return new ResponseEntity<>(getDataMapper().map(checkInResponseData, CheckInResponseWsDTO.class),
					HttpStatus.UNPROCESSABLE_ENTITY);
		}

		// Start check-in process
		final List<String> travellersToCheckIn = checkInRequestData.getTravellerTransportOfferingInfos().stream()
				.map(info -> info.getTraveller().getUid()).distinct().collect(Collectors.toList());
		checkInFacade.startCheckInProcess(bookingReference, originDestinationRefNumber, travellersToCheckIn);

		return new ResponseEntity<>(getDataMapper().map(checkInResponseData, CheckInResponseWsDTO.class), HttpStatus.OK);
	}

	/**
	 * Remove single traveller from a travel booking, given booking reference, UserId and traveller uid.
	 *
	 * @param bookingReference
	 * 		the order id
	 * @param travellerUid
	 * 		the traveller uid
	 * @param userId
	 * 		the user id
	 * @return response entity
	 */
	@RequestMapping(value = "/{bookingReference}/removeTraveller/{travellerUid}", method = RequestMethod.POST, produces =
			MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "Remove Traveller From Order", notes = "Remove Traveller From Order", response =
			RemovedTravellerResponseWsDTO.class, produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "Success", response = RemovedTravellerResponseWsDTO.class),
			@ApiResponse(code = 422, message = "Unprocessable Entity", response = ResponseEntity.class) })
	@ApiImplicitParams({ @ApiImplicitParam(name = "baseSiteId", required = true, dataType = "String", paramType = "path"),
			@ApiImplicitParam(name = "userId", required = true, dataType = "String", paramType = "path"),
			@ApiImplicitParam(name = "bookingReference", required = true, dataType = "String", paramType = "path"),
			@ApiImplicitParam(name = "travellerUid", required = true, dataType = "String", paramType = "path") })
	public ResponseEntity<RemovedTravellerResponseWsDTO> removeTraveller(
			@ApiParam(value = "Booking Reference", required = true) @PathVariable final String bookingReference,
			@ApiParam(value = "Traveller Uid", required = true) @PathVariable final String travellerUid,
			@ApiParam(value = "User Id", required = true) @PathVariable final String userId)

	{
		final TravellerData traveller = travellerFacade.getTraveller(travellerUid);

		if (Objects.isNull(traveller))
		{
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		final BookingActionResponseData bookingActionResponseData = validateBookingAction(
				travelBookingWsFacade.getBookingByBookingReference(bookingReference), userId, ActionTypeOption.REMOVE_TRAVELLER);

		if (Objects.isNull(bookingActionResponseData))
		{
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		final boolean validActionType = bookingActionResponseData.getBookingActions().stream()
				.allMatch(bookingActionData -> bookingActionData.isEnabled());

		if (!validActionType)
		{
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}

		final boolean travellerCancellationStarted = travelBookingWsFacade
				.beginTravellerCancellation(bookingReference, traveller.getLabel(), traveller.getUid(),
						travelBookingWsFacade.getCurrentUserUid());

		if (!travelBookingWsFacade.atleastOneAdultTravellerRemaining(bookingReference, traveller.getLabel())
				|| !travellerCancellationStarted)
		{
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}

		final PriceData totalToPay = travelBookingWsFacade.getTotalToPay();
		final PriceData totalToRefund = travelBookingWsFacade.getRefundForCancelledTraveller();
		final boolean result = travelBookingWsFacade.cancelTraveller(totalToPay, totalToRefund, traveller);

		if (!result)
		{
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}

		final PriceData absTotalToRefund = travelCommercePriceFacade
				.createPriceData(totalToRefund.getValue().abs().doubleValue(), totalToRefund.getCurrencyIso());

		return new ResponseEntity<>(populateResponse(traveller, absTotalToRefund), HttpStatus.OK);
	}

	/**
	 * Build cancel booking response.
	 *
	 * @param bookingReference
	 * 		the order id
	 * @param amountToRefund
	 * 		the amount to refund
	 * @param errorMessages
	 * 		the error messages
	 * @return the cancel booking response ws dto
	 */
	protected CancelBookingResponseWsDTO buildCancelBookingResponse(final String bookingReference, final PriceData amountToRefund,
			final List<String> errorMessages)
	{
		final CancelBookingResponseWsDTO cancelBookingResponseWsDTO = new CancelBookingResponseWsDTO();
		cancelBookingResponseWsDTO.setBookingReference(bookingReference);
		if (Objects.nonNull(amountToRefund))
		{
			cancelBookingResponseWsDTO.setAmountToRefund(getDataMapper().map(amountToRefund, PriceWsDTO.class));
		}
		if (CollectionUtils.isNotEmpty(errorMessages))
		{
			final List<ErrorWsDTO> errorWsDTOList = new ArrayList<>();
			for (final String errorMessage : errorMessages)
			{
				final ErrorWsDTO errorWsDTO = new ErrorWsDTO();
				errorWsDTO.setMessage(errorMessage);
				errorWsDTOList.add(errorWsDTO);
			}
			cancelBookingResponseWsDTO.setErrors(errorWsDTOList);
		}
		return cancelBookingResponseWsDTO;
	}

	/**
	 * Populate request data with traveller info.
	 *
	 * @param checkInRequestData
	 * 		the check in request data
	 * @param checkInRequestWsDTO
	 * 		the check in request ws dto
	 */
	protected void populateRequestDataWithTravellerInfo(final CheckInRequestData checkInRequestData,
			final CheckInRequestWsDTO checkInRequestWsDTO)
	{
		checkInRequestData.getTravellerTransportOfferingInfos().forEach(info -> info.getTraveller().setTravellerInfo(
				mapTravellerInfo(info.getTraveller().getUid(), checkInRequestWsDTO.getTravellerTransportOfferingInfos().stream()
						.map(TravellerTransportOfferingInfoWsDTO::getTraveller).collect(Collectors.toList()))));
	}

	/**
	 * Populate request data for single traveller.
	 *
	 * @param checkInRequestData
	 * 		the check in request data
	 * @param travellerInfo
	 * 		the traveller info
	 * @param travellerUid
	 * 		the traveller uid
	 */
	protected void populateRequestDataForSingleTraveller(final CheckInRequestData checkInRequestData,
			final TravellerTransportOfferingInfoWsDTO travellerInfo, final String travellerUid)
	{
		checkInRequestData.getTravellerTransportOfferingInfos().stream()
				.filter(info -> StringUtils.equalsIgnoreCase(travellerUid, info.getTraveller().getUid())).findFirst().get()
				.getTraveller().setTravellerInfo(
				getDataMapper().map(travellerInfo.getTraveller().getTravellerInfo(), PassengerInformationData.class));
	}

	/**
	 * Map traveller info.
	 *
	 * @param travellerUid
	 * 		the traveller uid
	 * @param travellers
	 * 		the travellers
	 * @return the passenger information data
	 */
	protected PassengerInformationData mapTravellerInfo(final String travellerUid, final List<TravellerWsDTO> travellers)
	{
		for (final TravellerWsDTO traveller : travellers)
		{
			if (travellerUid.equalsIgnoreCase(traveller.getUid()))
			{
				return getDataMapper().map(traveller.getTravellerInfo(), PassengerInformationData.class);
			}
		}
		return null;
	}

	/**
	 * Create check in response data.
	 *
	 * @param bookingReference
	 * 		the booking reference
	 * @param errorKey
	 * 		the error key
	 * @param errorMessage
	 * 		the error message
	 * @return the check in response data
	 */
	protected CheckInResponseData createCheckInResponse(final String bookingReference, final String errorKey,
			final List<String> errorMessage)
	{
		final CheckInResponseData checkInResponseData = new CheckInResponseData();
		checkInResponseData.setBookingReference(bookingReference);

		final Map<String, List<String>> errorMap = new HashMap();
		errorMap.put(errorKey, errorMessage);
		checkInResponseData.setErrors(errorMap);
		return checkInResponseData;
	}

	/**
	 * Checks if given action type is valid
	 *
	 * @param reservationData
	 * 		the reservation data
	 * @param userId
	 * 		the user id
	 * @param actionType
	 * 		the action type
	 * @return true if action is valid, otherwise false
	 */
	protected BookingActionResponseData validateBookingAction(final ReservationData reservationData, final String userId,
			final ActionTypeOption actionType)
	{
		if (Objects.isNull(reservationData))
		{
			return null;
		}

		final BookingActionRequestData bookingActionRequestData = new BookingActionRequestData();
		bookingActionRequestData.setRequestActions(Collections.singletonList(actionType));
		bookingActionRequestData.setUserId(userId);
		return actionFacade.getBookingAction(bookingActionRequestData, reservationData);
	}

	/**
	 * @return True when session cart is valid otherwise false
	 * @throws InvalidCartException
	 */
	protected boolean isValidCart() throws InvalidCartException
	{
		List<CartModificationData> modifications;
		try
		{
			modifications = travelCartWsFacade.validateCartData();
		}
		catch (final CommerceCartModificationException e)
		{
			LOG.error("Failed to validate cart", e);
			throw new InvalidCartException("Failed to validate cart" + getSessionCart().getCode());
		}
		if (CollectionUtils.isNotEmpty(modifications))
		{
			modifications.forEach(cartModificationData -> {
				if (cartModificationData.getEntry() != null)
				{
					LOG.error("Cart Validation failed: " + cartModificationData.getStatusCode() + " for entry: " +
							cartModificationData
									.getEntry().getProduct().getCode() + " and quantity: " + cartModificationData.getEntry()
							.getQuantity());
				}
			});
			return false;
		}
		return travelCartWsFacade.validateSeatSelection();
	}


	protected RemovedTravellerResponseWsDTO populateResponse(final TravellerData traveller, final PriceData absTotalToRefund)
	{
		final RemovedTravellerResponseWsDTO response = new RemovedTravellerResponseWsDTO();
		response.setUid(traveller.getUid());
		response.setName(buildFormattedTravellerName((PassengerInformationData) traveller.getTravellerInfo()));
		response.setAmountToRefund(getDataMapper().map(absTotalToRefund, PriceWsDTO.class));
		return response;
	}

	/**
	 * Build formatted traveller name string.
	 *
	 * @param info
	 * 		the info
	 * @return the string
	 */
	protected String buildFormattedTravellerName(final PassengerInformationData info)
	{
		final StringJoiner formattedTravellerName = new StringJoiner(" ");
		formattedTravellerName.add(info.getTitle().getName()).add(info.getFirstName()).add(info.getSurname());
		return formattedTravellerName.toString();
	}
}



