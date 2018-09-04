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

import de.hybris.platform.commercefacades.travel.TransportOfferingData;
import de.hybris.platform.travelcommercewebservices.transportoffering.TransportOfferingWsFacade;
import de.hybris.platform.travelcommercewebservices.v2.controller.travel.BaseController;
import de.hybris.platform.travelservices.constants.TravelservicesConstants;
import de.hybris.travelcommercewebservicescommons.dto.TransportOfferingStatusSearchRequestWsDTO;
import de.hybris.travelcommercewebservicescommons.dto.TransportOfferingStatusSearchWsDTO;
import de.hybris.travelcommercewebservicescommons.dto.TransportOfferingWsDTO;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
 * TransportOffering Controller
 */
@Controller
@RequestMapping(value = "/{baseSiteId}/transportOfferings")
@Api(tags = "Transport Offerings")
public class TransportOfferingController extends BaseController
{
	private static final Logger LOG = LoggerFactory.getLogger(TransportOfferingController.class);

	@Resource(name = "transportOfferingWsFacade")
	private TransportOfferingWsFacade transportOfferingWsFacade;

	/**
	 * This method gets the transportoffering for given code
	 *
	 * @param code
	 * @param fields
	 * @return
	 */
	@RequestMapping(value = "/{code}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	@ApiOperation(value = "Returns the Transport Offering for the given code", produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "Success", response = TransportOfferingWsDTO.class),
			@ApiResponse(code = 204, message = "No Content", response = String.class) })
	@ApiImplicitParams({
			@ApiImplicitParam(name = "fields", value = DEFAULT_FIELD_SET, required = false, dataType = "String", paramType =
					"query"),
			@ApiImplicitParam(name = "code", required = true, dataType = "String", paramType = "path"),
			@ApiImplicitParam(name = "baseSiteId", required = true, dataType = "String", paramType = "path") })
	public ResponseEntity<TransportOfferingWsDTO> getTransportOfferingByCode(
			@ApiParam(value = "Transport Offering code", required = true) @PathVariable final String code,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues =
					"BASIC, DEFAULT, FULL") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("getTransportOfferingByCode: code=" + sanitize(code));
		}
		final TransportOfferingData transportOfferingData = transportOfferingWsFacade.getTransportOffering(code);
		if (Objects.isNull(transportOfferingData))
		{
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(getDataMapper().map(transportOfferingData, TransportOfferingWsDTO.class), HttpStatus.OK);
	}

	/**
	 * Gets transport offering status.
	 *
	 * @param transportOfferingStatusSearchRequestWsDTO
	 * 		the transport offering status search request ws dto
	 * @return the transport offering status
	 */
	@RequestMapping(value = "/status", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	@ApiOperation(value = "Get the status of the transport offering", produces = "application/json")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Success", response = TransportOfferingStatusSearchWsDTO.class, responseContainer =
					"List"),
			@ApiResponse(code = 204, message = "No Content", response = String.class),
			@ApiResponse(code = 400, message = "Bad Request") })
	@ApiImplicitParams({
			@ApiImplicitParam(name = "fields", value = DEFAULT_FIELD_SET, required = false, dataType = "String", paramType =
					"query"),
			@ApiImplicitParam(name = "baseSiteId", required = true, dataType = "String", paramType = "path") })
	public ResponseEntity<List<TransportOfferingStatusSearchWsDTO>> getTransportOfferingStatus(
			@ApiParam(examples = @Example(value = {
					@ExampleProperty(mediaType = "application/json", value = "transport offering status search request ws dto") }), required = true)
			@RequestBody final TransportOfferingStatusSearchRequestWsDTO transportOfferingStatusSearchRequestWsDTO)
	{
		final String departureDate = transportOfferingStatusSearchRequestWsDTO.getDepartureDate();

		if (!isDateValid(departureDate))
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		final List<TransportOfferingData> transportOfferingDataList = transportOfferingWsFacade
				.getTransportOfferings(transportOfferingStatusSearchRequestWsDTO.getTransportOfferingNumber(), departureDate);
		if (CollectionUtils.isEmpty(transportOfferingDataList))
		{
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(
				getDataMapper().mapAsList(transportOfferingDataList, TransportOfferingStatusSearchWsDTO.class, null), HttpStatus.OK);
	}

	/**
	 * Is date valid.
	 *
	 * @param departureDate
	 * 		the departure date
	 * @return the boolean
	 */
	protected boolean isDateValid(final String departureDate)
	{
		final DateFormat dateFormat = new SimpleDateFormat(TravelservicesConstants.DATE_PATTERN);
		dateFormat.setLenient(false);
		try
		{
			dateFormat.parse(departureDate);
		}
		catch (final ParseException e)
		{
			return false;
		}

		return true;
	}
}
