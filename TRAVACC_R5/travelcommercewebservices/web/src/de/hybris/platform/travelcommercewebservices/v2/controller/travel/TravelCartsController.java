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

import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.travel.AddBundleToCartRequestData;
import de.hybris.platform.commercefacades.travel.PassengerInformationData;
import de.hybris.platform.commercefacades.travel.TravellerData;
import de.hybris.platform.commercefacades.travel.order.PaymentOptionData;
import de.hybris.platform.commercefacades.travel.reservation.data.ReservationData;
import de.hybris.platform.commerceservices.order.CommerceCartMergingException;
import de.hybris.platform.commerceservices.order.CommerceCartRestorationException;
import de.hybris.platform.commercewebservicescommons.dto.order.CartWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.PaymentDetailsWsDTO;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.CartException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.travelcommercewebservices.cart.TravelCartWsFacade;
import de.hybris.platform.travelcommercewebservices.cart.data.AccommodationToCartRequestData;
import de.hybris.platform.travelcommercewebservices.cart.data.AddToCartRequestData;
import de.hybris.platform.travelcommercewebservices.cart.data.AddToCartResultData;
import de.hybris.platform.travelcommercewebservices.cart.impl.CommerceWebServicesCartFacade;
import de.hybris.platform.travelcommercewebservices.exceptions.InvalidPaymentInfoException;
import de.hybris.platform.travelcommercewebservices.exceptions.NoCheckoutCartException;
import de.hybris.platform.travelcommercewebservices.exceptions.UnsupportedRequestException;
import de.hybris.platform.travelcommercewebservices.request.support.impl.PaymentProviderRequestSupportedStrategy;
import de.hybris.platform.travelcommercewebservices.swagger.ApiBaseSiteIdAndUserIdParam;
import de.hybris.platform.travelfacades.facades.TravellerFacade;
import de.hybris.platform.travelfacades.facades.ReservationFacade;
import de.hybris.platform.travelfacades.order.TravelCartFacade;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.travelcommercewebservicescommons.dto.TravellersWsDTO;
import de.hybris.travelcommercewebservicescommons.dto.cart.AccommodationToCartWsDTO;
import de.hybris.travelcommercewebservicescommons.dto.cart.AddBundleToCartRequestWsDTO;
import de.hybris.travelcommercewebservicescommons.dto.cart.AddToCartResultWsDTO;
import de.hybris.travelcommercewebservicescommons.dto.cart.AncillaryToCartWsDTO;
import de.hybris.travelcommercewebservicescommons.dto.payment.PaymentOptionWsDTO;
import de.hybris.travelcommercewebservicescommons.dto.reservation.ReservationWsDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;


/**
 * Cart Controller
 */
@Controller
@RequestMapping(value = "/{baseSiteId}/users/{userId}/carts")
@Api(tags = "Cart")
public class TravelCartsController extends BaseCommerceController
{
	private static final Logger LOG = LoggerFactory.getLogger(TravelCartsController.class);

	@Resource(name = "travelCartWsFacade")
	private TravelCartWsFacade travelCartWsFacade;

	@Resource(name = "reservationFacade")
	private ReservationFacade reservationFacade;

	@Resource(name = "paymentProviderRequestSupportedStrategy")
	private PaymentProviderRequestSupportedStrategy paymentProviderRequestSupportedStrategy;

	@Resource(name = "travellerFacade")
	private TravellerFacade travellerFacade;

	@Resource(name = "cartFacade")
	private TravelCartFacade cartFacade;

	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	@ApiOperation(value = "Creates or restore a cart for a user.", notes = "Creates a new cart or restores an anonymous cart as a"
			+ " user's cart (if an old Cart Id is given in the request).")
	@ApiBaseSiteIdAndUserIdParam
	public CartWsDTO createCart(@ApiParam(value = "Anonymous cart GUID.") @RequestParam(required = false) final String oldCartId,
			@ApiParam(value = "User's cart GUID to merge anonymous cart to.") @RequestParam(required = false) final String
					toMergeCartGuid,
			@ApiParam(value = "Booking Journey Type", required = true) @RequestParam final String bookingJourneyType,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues =
					"BASIC, DEFAULT, FULL") @RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("create cart for user");
		}

		String evaluatedToMergeCartGuid = toMergeCartGuid;

		if (StringUtils.isNotEmpty(oldCartId))
		{
			if (getUserFacade().isAnonymousUser())
			{
				throw new CartException("Anonymous user is not allowed to copy cart!");
			}

			if (!isCartAnonymous(oldCartId))
			{
				throw new CartException("Cart is not anonymous", CartException.CANNOT_RESTORE, oldCartId);
			}

			if (StringUtils.isEmpty(evaluatedToMergeCartGuid))
			{
				evaluatedToMergeCartGuid = getSessionCart().getGuid();
			}
			else
			{
				if (!isUserCart(evaluatedToMergeCartGuid))
				{
					throw new CartException("Cart is not current user's cart", CartException.CANNOT_RESTORE,
							evaluatedToMergeCartGuid);
				}
			}

			try
			{
				getCartFacade().restoreAnonymousCartAndMerge(oldCartId, evaluatedToMergeCartGuid);
				return getDataMapper().map(getSessionCart(), CartWsDTO.class, fields);
			}
			catch (final CommerceCartMergingException e)
			{
				throw new CartException("Couldn't merge carts", CartException.CANNOT_MERGE, e);
			}
			catch (final CommerceCartRestorationException e)
			{
				throw new CartException("Couldn't restore cart", CartException.CANNOT_RESTORE, e);
			}
		}
		else
		{
			if (StringUtils.isNotEmpty(evaluatedToMergeCartGuid))
			{
				if (!isUserCart(evaluatedToMergeCartGuid))
				{
					throw new CartException("Cart is not current user's cart", CartException.CANNOT_RESTORE,
							evaluatedToMergeCartGuid);
				}

				try
				{
					getCartFacade().restoreSavedCart(evaluatedToMergeCartGuid);
					return getDataMapper().map(getSessionCart(), CartWsDTO.class, fields);
				}
				catch (final CommerceCartRestorationException e)
				{
					throw new CartException("Couldn't restore cart", CartException.CANNOT_RESTORE, oldCartId, e);
				}

			}
			if (StringUtils.isNotBlank(bookingJourneyType))
			{
				travelCartWsFacade.setBookingJourneyTypeToCart(bookingJourneyType);
			}
			return getDataMapper().map(getSessionCart(), CartWsDTO.class, fields);
		}
	}

	@RequestMapping(value = "/{cartId}/addBundle", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "Add Bundle To Cart", notes = "Add bundled products to cart", response = String.class, produces =
			"application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "Success", response = String.class),
			@ApiResponse(code = 204, message = "No Content", response = String.class),
			@ApiResponse(code = 422, message = "Unprocessable Entity", response = ResponseEntity.class) })
	@ApiImplicitParams({ @ApiImplicitParam(name = "baseSiteId", required = true, dataType = "String", paramType = "path"),
			@ApiImplicitParam(name = "userId", required = true, dataType = "String", paramType = "path"),
			@ApiImplicitParam(name = "cartId", required = true, dataType = "String", paramType = "path") })
	public ResponseEntity<AddToCartResultWsDTO> addToCartBundle(
			@ApiParam(value = "Cart code", required = true) @PathVariable final String cartId,
			@RequestBody final AddBundleToCartRequestWsDTO addBundleToCartRequestWsDTO, final Model model)
	{
		final AddBundleToCartRequestData addBundleToCartRequestData = getDataMapper()
				.map(addBundleToCartRequestWsDTO, AddBundleToCartRequestData.class);

		final AddToCartResultData addToCartResultData = travelCartWsFacade.addBundle(addBundleToCartRequestData);
		if (Objects.nonNull(addToCartResultData.getErrors()))
		{
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
		return new ResponseEntity<>(getDataMapper().map(addToCartResultData, AddToCartResultWsDTO.class), HttpStatus.OK);
	}

	@RequestMapping(value = "/{cartId}/addAncillary", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "Add Ancillary to Cart", notes = "Add Ancillary to Cart", response = AddToCartResultWsDTO.class,
			produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "Success", response = AddToCartResultWsDTO.class),
			@ApiResponse(code = 422, message = "Unprocessable Entity", response = ResponseEntity.class) })
	@ApiImplicitParams({ @ApiImplicitParam(name = "baseSiteId", required = true, dataType = "String", paramType = "path"),
			@ApiImplicitParam(name = "userId", required = true, dataType = "String", paramType = "path"),
			@ApiImplicitParam(name = "cartId", required = true, dataType = "String", paramType = "path") })
	public ResponseEntity<AddToCartResultWsDTO> addAncillary(
			@ApiParam(value = "Cart code", required = true) @PathVariable final String cartId,
			@RequestBody final AncillaryToCartWsDTO ancillaryToCartWsDTO) throws WebserviceValidationException
	{
		final AddToCartRequestData addToCartRequestData = getDataMapper().map(ancillaryToCartWsDTO, AddToCartRequestData.class);

		final AddToCartResultData addToCartResultData = travelCartWsFacade.addAncillary(addToCartRequestData);

		if (CollectionUtils.isNotEmpty(addToCartResultData.getErrors()))
		{
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
		return new ResponseEntity<>(getDataMapper().map(addToCartResultData, AddToCartResultWsDTO.class), HttpStatus.OK);
	}

	@RequestMapping(value = "/{cartId}/addAccommodation", method = RequestMethod.POST, produces = MediaType
			.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "Add Accommodation To Cart", notes = "Add Accommodation to cart", response = String.class, produces =
			"application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "Success", response = String.class),
			@ApiResponse(code = 204, message = "No Content", response = String.class),
			@ApiResponse(code = 422, message = "Unprocessable Entity", response = AddToCartResultWsDTO.class) })
	@ApiImplicitParams({ @ApiImplicitParam(name = "baseSiteId", required = true, dataType = "String", paramType = "path"),
			@ApiImplicitParam(name = "userId", required = true, dataType = "String", paramType = "path"),
			@ApiImplicitParam(name = "cartId", required = true, dataType = "String", paramType = "path") })
	public ResponseEntity<AddToCartResultWsDTO> addAccommodationToCart(
			@ApiParam(value = "Cart code", required = true) @PathVariable final String cartId,
			@RequestBody final AccommodationToCartWsDTO accommodationToCartWsDTO, final Model model)
	{
		final AccommodationToCartRequestData addAccommodationToCartRequestData = getDataMapper()
				.map(accommodationToCartWsDTO, AccommodationToCartRequestData.class);
		final AddToCartResultData addToCartResultData = travelCartWsFacade
				.addSelectedAccommodationsToCart(addAccommodationToCartRequestData);

		if (CollectionUtils.isNotEmpty(addToCartResultData.getErrors()))
		{
			return new ResponseEntity<>(getDataMapper().map(addToCartResultData, AddToCartResultWsDTO.class), HttpStatus.UNPROCESSABLE_ENTITY);
		}
		return new ResponseEntity<>(getDataMapper().map(addToCartResultData, AddToCartResultWsDTO.class), HttpStatus.OK);
	}

	@RequestMapping(value = "/{cartId}/reservationDetails", method = RequestMethod.POST, produces = MediaType
			.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "Get Reservation Details", notes = "Get Reservation Details", response = AddToCartResultWsDTO.class,
			produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "Success", response = AddToCartResultWsDTO.class),
			@ApiResponse(code = 204, message = "No Content", response = ResponseEntity.class) })
	@ApiImplicitParams({ @ApiImplicitParam(name = "baseSiteId", required = true, dataType = "String", paramType = "path"),
			@ApiImplicitParam(name = "userId", required = true, dataType = "String", paramType = "path"),
			@ApiImplicitParam(name = "cartId", required = true, dataType = "String", paramType = "path") })
	public ResponseEntity<ReservationWsDTO> getReservationDetails(
			@ApiParam(value = "Cart code", required = true) @PathVariable final String cartId)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("getReservationDetails: code=" + sanitize(cartId));
		}

		final ReservationData reservationData = reservationFacade.getCurrentReservationData();
		if (Objects.isNull(reservationData))
		{
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(getDataMapper().map(reservationData, ReservationWsDTO.class), HttpStatus.OK);
	}

	@Secured({ "ROLE_CUSTOMERGROUP", "ROLE_GUEST", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/{cartId}/paymentdetails", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Add Payment Details", notes = "Adds Payment details to cart", response = PaymentDetailsWsDTO.class,
			produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "Success", response = PaymentDetailsWsDTO.class),
			@ApiResponse(code = 204, message = "No Content", response = ResponseEntity.class) })
	@ApiImplicitParams({
			@ApiImplicitParam(name = "fields", value = DEFAULT_FIELD_SET, required = false, dataType = "String", paramType =
					"query"),
			@ApiImplicitParam(name = "baseSiteId", required = true, dataType = "String", paramType =
					"path"),
			@ApiImplicitParam(name = "userId", required = true, dataType = "String", paramType = "path"),
			@ApiImplicitParam(name = "cartId", required = true, dataType = "String", paramType = "path") })
	@ResponseBody
	public ResponseEntity<PaymentDetailsWsDTO> addPaymentDetails(@ApiParam(examples = @Example(value = {
			@ExampleProperty(mediaType = "application/json", value = "{\"paymentDetailsCodes\":[\"code1\",\"code2\",\"code3\","
					+ "\"code4\"]}") })) @RequestBody final PaymentDetailsWsDTO paymentDetailsWsDTO,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues =
					"BASIC, DEFAULT, FULL") @RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
			throws WebserviceValidationException, InvalidPaymentInfoException, NoCheckoutCartException, UnsupportedRequestException
	{
		CCPaymentInfoData paymentInfoData = null;
		try
		{
			paymentProviderRequestSupportedStrategy.checkIfRequestSupported("addPaymentDetails");
			validatePayment(paymentDetailsWsDTO);
			paymentInfoData = addPaymentDetailsInternal(getDataMapper().map(paymentDetailsWsDTO, CCPaymentInfoData.class, fields))
					.getPaymentInfo();
		}
		catch (final UnsupportedRequestException | NoCheckoutCartException ex)
		{
			LOG.debug("Exception Occurred while adding payment to cart ", ex);
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
		if (Objects.isNull(paymentInfoData))
		{
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(getDataMapper().map(paymentInfoData, PaymentDetailsWsDTO.class), HttpStatus.CREATED);
	}

	@Secured({ "ROLE_CUSTOMERGROUP", "ROLE_GUEST", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/{cartId}/addPassenger", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "Adds a Passenger", notes = "Creates passenger with the given details")
	@ApiResponses(
			{ @ApiResponse(code = 200, message = "Success", response = String.class),
					@ApiResponse(code = 422, message = "Unprocessable Entity", response = ResponseEntity.class) })
	@ApiImplicitParams(
			{ @ApiImplicitParam(name = "baseSiteId", required = true, dataType = "String", paramType = "path"),
					@ApiImplicitParam(name = "userId", required = true, dataType = "String", paramType = "path"),
					@ApiImplicitParam(name = "cartId", required = true, dataType = "String", paramType = "path") })
	public ResponseEntity<Object> addPassenger(@RequestBody final TravellersWsDTO travellerRequestWsDTOs, final Model model)
	{

		final List<TravellerData> travellers = new ArrayList<TravellerData>();

		travellerRequestWsDTOs.getTravellers().forEach(travellerWsDTO -> {
			final TravellerData travellerData = getDataMapper().map(travellerWsDTO, TravellerData.class);
			final PassengerInformationData travellerInfo = getDataMapper().map(travellerWsDTO.getTravellerInfo(),
					PassengerInformationData.class);
			travellerData.setTravellerInfo(travellerInfo);
			travellers.add(travellerData);
		});
		try
		{
			travellerFacade.updateTravellerDetails(travellers);
		}
		catch (final ModelSavingException mse)
		{
			LOG.debug("Exception occurred while updating traveller details", mse);
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Secured({ "ROLE_CUSTOMERGROUP", "ROLE_GUEST", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/{cartId}/paymentOptions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "Get payment options", notes = "Retrieve all available payment options")
	@ApiResponses(
			{ @ApiResponse(code = 200, message = "Success", response = PaymentOptionWsDTO.class),
					@ApiResponse(code = 204, message = "No Content", response = ResponseEntity.class) })
	@ApiImplicitParams(
			{ @ApiImplicitParam(name = "baseSiteId", required = true, dataType = "String", paramType = "path"),
					@ApiImplicitParam(name = "userId", required = true, dataType = "String", paramType = "path"),
					@ApiImplicitParam(name = "cartId", required = true, dataType = "String", paramType = "path") })
	public ResponseEntity<List<PaymentOptionWsDTO>> getPaymentOptions()
	{

		final List<PaymentOptionData> paymentOptionData = cartFacade.getPaymentOptions();
		if (CollectionUtils.isEmpty(paymentOptionData))
		{
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(getDataMapper().mapAsList(paymentOptionData, PaymentOptionWsDTO.class, null), HttpStatus.OK);
	}


	protected boolean isUserCart(final String toMergeCartGuid)
	{
		if (getCartFacade() instanceof CommerceWebServicesCartFacade)
		{
			return CommerceWebServicesCartFacade.class.cast(getCartFacade()).isCurrentUserCart(toMergeCartGuid);
		}
		return true;
	}

	protected boolean isCartAnonymous(final String cartGuid)
	{
		if (getCartFacade() instanceof CommerceWebServicesCartFacade)
		{
			return CommerceWebServicesCartFacade.class.cast(getCartFacade()).isAnonymousUserCart(cartGuid);
		}
		return true;
	}

	protected void validatePayment(final PaymentDetailsWsDTO paymentDetails) throws NoCheckoutCartException
	{
		if (!getCheckoutFacade().hasCheckoutCart())
		{
			throw new NoCheckoutCartException("Cannot add PaymentInfo. There was no checkout cart created yet!");
		}
		validate(paymentDetails, "paymentDetails", getPaymentDetailsDTOValidator());
	}
}
