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


import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.travelcommercewebservices.constants.TravelcommercewebservicesConstants;
import de.hybris.platform.travelcommercewebservices.enums.OauthType;
import de.hybris.platform.travelcommercewebservices.facades.oauth.TravelOauthWsFacade;
import de.hybris.platform.travelcommercewebservices.facades.oauth.exceptions.TravelAuthenticationException;
import de.hybris.platform.travelcommercewebservices.oauth.data.AuthenticationResponseData;
import de.hybris.platform.webservicescommons.dto.error.ErrorWsDTO;
import de.hybris.travelcommercewebservicescommons.dto.oauth.CustomerWsDTO;
import de.hybris.travelcommercewebservicescommons.dto.oauth.OauthConfirmationWsDTO;
import de.hybris.travelcommercewebservicescommons.enums.ClientWsDTO;

import java.util.Objects;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
 * Controller to handle oauth authentication
 */
@Controller
@RequestMapping(value = "/{baseSiteId}/oauth")
@Api(tags = "Oauth")
public class TravelOauthController extends BaseController
{
	private static final Logger LOG = LoggerFactory.getLogger(TravelOauthController.class);

	@Resource(name = "travelOauthWsFacade")
	private TravelOauthWsFacade travelOauthWsFacade;

	/**
	 * Oauth authentication for a user
	 *
	 * @param customerWsDTO
	 *           credentials from the user
	 * @param request
	 *           request objects
	 * @return {@link OauthConfirmationWsDTO}
	 * @throws TravelAuthenticationException
	 */
	@RequestMapping(value = "/user/token", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@ApiOperation(value = "Customer authentication", notes = "Retrieving token for a specific customer", response = OauthConfirmationWsDTO.class, produces = "application/json")
	@ApiResponses(
	{ @ApiResponse(code = 200, message = "Success", response = ResponseEntity.class),
			@ApiResponse(code = 422, message = "Unprocessable Entity", response = ResponseEntity.class),
			@ApiResponse(code = 400, message = "Bad Request", response = ResponseEntity.class) })
	@ApiImplicitParams(
	{ @ApiImplicitParam(name = "baseSiteId", required = true, dataType = "String", paramType = "path") })
	public ResponseEntity<OauthConfirmationWsDTO> authenticateCustomer(@ApiParam(examples = @Example(value =
	{ @ExampleProperty(mediaType = "application/json", value = "{\"uid\":\"customerName\","
			+ "\"password\":\"customerPassword\"}") })) @RequestBody final CustomerWsDTO customerWsDTO,
			@ApiParam(value = "Base Site Id", required = true) @PathVariable final String baseSiteId,
			final HttpServletRequest request)
	{
		AuthenticationResponseData authenticationResponseData = null;
		try
		{
			authenticationResponseData = travelOauthWsFacade.getToken(request, customerWsDTO.getUid(), customerWsDTO.getPassword(),
					OauthType.USER, baseSiteId);
		}
		catch (RequestParameterException | TravelAuthenticationException ex)
		{
			LOG.debug("Exception occurred while getting user authentication token", ex);
			return new ResponseEntity<>(getOauthConfirmationError(TravelcommercewebservicesConstants.AUTHENTICATION_EXCEPTION),
					HttpStatus.BAD_REQUEST);
		}
		if (Objects.isNull(authenticationResponseData))
		{
			return new ResponseEntity<>(getOauthConfirmationError(TravelcommercewebservicesConstants.AUTHENTICATION_NO_RESULT),
					HttpStatus.UNPROCESSABLE_ENTITY);
		}
		return new ResponseEntity<>(getDataMapper().map(authenticationResponseData, OauthConfirmationWsDTO.class), HttpStatus.OK);
	}


	/**
	 * Oauth authentication for a client
	 *
	 * @param clientWsDTO
	 *           credentials for the oauth client
	 * @param request
	 *           request objects
	 * @return {@link OauthConfirmationWsDTO}
	 * @throws TravelAuthenticationException
	 */
	@RequestMapping(value = "/client/token", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@ApiOperation(value = "Client authentication", notes = "Retrieving token for a specific client", response = OauthConfirmationWsDTO.class, produces = "application/json")
	@ApiResponses(
	{ @ApiResponse(code = 200, message = "Success", response = ResponseEntity.class),
			@ApiResponse(code = 422, message = "Unprocessable Entity", response = ResponseEntity.class),
			@ApiResponse(code = 400, message = "Bad Request", response = ResponseEntity.class) })
	@ApiImplicitParams(
	{ @ApiImplicitParam(name = "baseSiteId", required = true, dataType = "String", paramType = "path") })
	public ResponseEntity<OauthConfirmationWsDTO> authenticateWithClient(@ApiParam(examples = @Example(value =
	{ @ExampleProperty(mediaType = "application/json", value = "{\"clientId\":\"client_id\","
			+ "\"clientSecret\":\"client_secret\"}") })) @RequestBody final ClientWsDTO clientWsDTO,
			@ApiParam(value = "Base Site Id", required = true) @PathVariable final String baseSiteId,
			final HttpServletRequest request)
	{
		AuthenticationResponseData authenticationResponseData = null;
		try
		{
			authenticationResponseData = travelOauthWsFacade.getToken(request, clientWsDTO.getClientId(),
					clientWsDTO.getClientSecret(), OauthType.CLIENT, baseSiteId);
		}
		catch (RequestParameterException | TravelAuthenticationException ex)
		{
			LOG.debug("Exception occurred while getting client authentication token", ex);
			return new ResponseEntity<>(getOauthConfirmationError(TravelcommercewebservicesConstants.AUTHENTICATION_EXCEPTION),
					HttpStatus.BAD_REQUEST);
		}
		if (Objects.isNull(authenticationResponseData))
		{
			return new ResponseEntity<>(getOauthConfirmationError(TravelcommercewebservicesConstants.AUTHENTICATION_NO_RESULT),
					HttpStatus.UNPROCESSABLE_ENTITY);
		}
		return new ResponseEntity<>(getDataMapper().map(authenticationResponseData, OauthConfirmationWsDTO.class), HttpStatus.OK);
	}

	protected OauthConfirmationWsDTO getOauthConfirmationError(final String errorMsg)
	{
		final OauthConfirmationWsDTO oauthConfirmationWsDTO = new OauthConfirmationWsDTO();
		final ErrorWsDTO errorWsDTO = new ErrorWsDTO();
		errorWsDTO.setMessage(errorMsg);
		oauthConfirmationWsDTO.setError(errorWsDTO);
		return oauthConfirmationWsDTO;
	}
}
