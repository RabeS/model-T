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
package de.hybris.platform.travelcommercewebservices.v2.controller.transport;

import de.hybris.platform.commercefacades.travel.CabinClassData;
import de.hybris.platform.commercefacades.travel.FareSearchRequestData;
import de.hybris.platform.commercefacades.travel.FareSelectionData;
import de.hybris.platform.commercefacades.travel.OriginDestinationInfoData;
import de.hybris.platform.commercefacades.travel.PassengerTypeQuantityData;
import de.hybris.platform.commercefacades.travel.ancillary.data.OfferRequestData;
import de.hybris.platform.commercefacades.travel.ancillary.data.OfferResponseData;
import de.hybris.platform.commercefacades.travel.seatmap.data.SeatMapResponseData;
import de.hybris.platform.commercewebservicescommons.strategies.CartLoaderStrategy;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.travelcommercewebservices.dto.OfferResponseWsDTO;
import de.hybris.platform.travelcommercewebservices.dto.SeatMapResponseWsDTO;
import de.hybris.platform.travelcommercewebservices.v2.controller.travel.BaseController;
import de.hybris.platform.travelfacades.facades.CabinClassFacade;
import de.hybris.platform.travelfacades.facades.OffersFacade;
import de.hybris.platform.travelfacades.fare.search.FareSearchFacade;
import de.hybris.travelcommercewebservicescommons.dto.FareSearchRequestWsDTO;
import de.hybris.travelcommercewebservicescommons.dto.FareSelectionWsDTO;

import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
 * Search Controller
 */
@Controller
@RequestMapping(value = "/{baseSiteId}/search")
@Api(tags = "Search")
public class TransportSearchController extends BaseController
{
	private static final Logger LOG = LoggerFactory.getLogger(TransportSearchController.class);

	@Resource(name = "cartLoaderStrategy")
	private CartLoaderStrategy cartLoaderStrategy;

	@Resource(name = "offersFacade")
	private OffersFacade offersFacade;

	@Resource(name = "fareSearchFacade")
	private FareSearchFacade fareSearchFacade;

	@Resource(name = "cabinClassFacade")
	private CabinClassFacade cabinClassFacade;

	@Resource(name = "timeService")
	private TimeService timeService;

	/**
	 * Search fares products
	 *
	 * @param fareSearchRequestWsDTO
	 * 		Body request
	 * @return {@link FareSelectionWsDTO}
	 */
	@RequestMapping(value = "/fares", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	@ApiOperation(value = "Searches for all available priced itineraries", produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "Success", response = FareSelectionWsDTO.class),
			@ApiResponse(code = 204, message = "No Content", response = ResponseEntity.class),
			@ApiResponse(code = 422, message = "Unprocessable Entity", response = ResponseEntity.class) })
	@ApiImplicitParams({
			@ApiImplicitParam(name = "fields", value = DEFAULT_FIELD_SET, required = false, dataType = "String", paramType =
					"query"),
			@ApiImplicitParam(name = "baseSiteId", required = true, dataType = "String", paramType = "path") })
	public ResponseEntity<FareSelectionWsDTO> doSearch(@ApiParam(examples = @Example(value = {
			@ExampleProperty(mediaType = "application/json", value = "fare search request ws dto") }), required = true) @RequestBody final
	FareSearchRequestWsDTO fareSearchRequestWsDTO)
	{
		final FareSearchRequestData fareSearchRequest = getDataMapper().map(fareSearchRequestWsDTO, FareSearchRequestData.class);

		if (!validatePassengerQuantity(fareSearchRequest) || !validateCabinClass(fareSearchRequest))
		{
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		if (!validateDepartureDate(fareSearchRequest))
		{
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}

		final FareSelectionData fareSelectionData = fareSearchFacade.doSearch(fareSearchRequest);
		if (CollectionUtils.isEmpty(fareSelectionData.getPricedItineraries()))
		{
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(getDataMapper().map(fareSelectionData, FareSelectionWsDTO.class), HttpStatus.OK);

	}

	/**
	 * Search ancillary products
	 *
	 * @param cartId
	 * 		cart id
	 * @return {@link OfferResponseWsDTO}
	 */
	@RequestMapping(value = "/{cartId}/ancillaries", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	@ApiOperation(value = "Searches for all available priced itineraries", produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "Success", response = OfferResponseWsDTO.class),
			@ApiResponse(code = 204, message = "No Content", response = ResponseEntity.class) })
	@ApiImplicitParams({ @ApiImplicitParam(name = "baseSiteId", required = true, dataType = "String", paramType = "path"),
			@ApiImplicitParam(name = "cartId", required = true, dataType = "String", paramType = "path") })
	public ResponseEntity<OfferResponseWsDTO> doSearch(@PathVariable final String cartId)
	{
		cartLoaderStrategy.loadCart(cartId, true);

		final OfferRequestData offerRequest = offersFacade.getOffersRequest();
		final OfferResponseData offerResponseData = offersFacade.getOffers(offerRequest);
		if (Objects.isNull(offerResponseData))
		{
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(getDataMapper().map(offerResponseData, OfferResponseWsDTO.class), HttpStatus.OK);
	}

	/**
	 * Gets accommodation map.
	 *
	 * @param cartId
	 * 		the cart id
	 * @return the accommodation map
	 */
	@RequestMapping(value = "/{cartId}/accommodationMap", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	@ApiOperation(value = "Returns the Accommodation Map for the given Id", produces = "application/json")
	@ApiResponses(
			{ @ApiResponse(code = 200, message = "Success", response = SeatMapResponseWsDTO.class),
					@ApiResponse(code = 204, message = "No Content", response = ResponseEntity.class),
					@ApiResponse(code = 422, message = "Unprocessable Entity", response = ResponseEntity.class) })
	@ApiImplicitParams(
			{ @ApiImplicitParam(name = "fields", value = DEFAULT_FIELD_SET, required = false, dataType = "String", paramType = "query"),
					@ApiImplicitParam(name = "baseSiteId", required = true, dataType = "String", paramType = "path"),
					@ApiImplicitParam(name = "userId", required = true, dataType = "String", paramType = "path"),
					@ApiImplicitParam(name = "cartId", required = true, dataType = "String", paramType = "path") })
	public ResponseEntity<SeatMapResponseWsDTO> getAccommodationMap(@PathVariable final String cartId)
	{
		cartLoaderStrategy.loadCart(cartId, true);
		SeatMapResponseData seatMapResponseData;
		try
		{
			seatMapResponseData = offersFacade.getAccommodations(offersFacade.getOffersRequest()).getSeatMap();

			if (Objects.isNull(seatMapResponseData))
			{
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
		}
		catch (final Exception e)
		{
			LOG.debug("Exception occurred while getting accommodation map", e);
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
		return new ResponseEntity<>(getDataMapper().map(seatMapResponseData, SeatMapResponseWsDTO.class), HttpStatus.OK);
	}

	/**
	 * Validate departure arrival date.
	 *
	 * @param fareSearchRequestData
	 * 		the fare search request data
	 * @return the boolean
	 */
	protected boolean validateDepartureDate(final FareSearchRequestData fareSearchRequestData)
	{
		for (final OriginDestinationInfoData originDestination : fareSearchRequestData.getOriginDestinationInfo())
		{
			if (originDestination.getDepartureTime().before(timeService.getCurrentTime()))
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Validate passenger quantity.
	 *
	 * @param fareSearchRequestData
	 * 		the fare search request data
	 * @return the boolean
	 */
	protected boolean validatePassengerQuantity(final FareSearchRequestData fareSearchRequestData)
	{

		if (CollectionUtils.isEmpty(fareSearchRequestData.getPassengerTypes()))
		{
			return false;
		}
		for (final PassengerTypeQuantityData passengerTypeQuantity : fareSearchRequestData.getPassengerTypes())
		{
			if (passengerTypeQuantity.getQuantity() <= 0)
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Validate cabin class.
	 *
	 * @param fareSearchRequestData
	 * 		the fare search request data
	 * @return the boolean
	 */
	protected boolean validateCabinClass(final FareSearchRequestData fareSearchRequestData)
	{

		if (StringUtils.isBlank(fareSearchRequestData.getTravelPreferences().getCabinPreference()))
		{
			return false;
		}
		final List<CabinClassData> cabinClasses = cabinClassFacade.getCabinClasses();
		return cabinClasses.stream()
				.anyMatch(cc -> cc.getCode().equalsIgnoreCase(fareSearchRequestData.getTravelPreferences().getCabinPreference()));
	}
}
