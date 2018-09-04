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

import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.user.data.RegisterData;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commercewebservicescommons.dto.user.UserSignUpWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.UserWsDTO;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.travelcommercewebservices.constants.TravelcommercewebservicesConstants;
import de.hybris.platform.travelcommercewebservices.swagger.ApiBaseSiteIdParam;
import de.hybris.platform.webservicescommons.cache.CacheControl;
import de.hybris.platform.webservicescommons.cache.CacheControlDirective;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.UriUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;


@Controller
@RequestMapping(value = "/{baseSiteId}/users")
@CacheControl(directive = CacheControlDirective.PRIVATE)
@Api(tags = "Users")
public class TravelUsersController extends BaseCommerceController
{
	protected static final Logger LOG = Logger.getLogger(TravelUsersController.class);

	@Resource(name = "customerFacade")
	private CustomerFacade customerFacade;

	@Resource(name = "userSignUpDTOValidator")
	private Validator userSignUpDTOValidator;

	protected final String userProperties = "login,password,titleCode,firstName,lastName";

	@Secured({ "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType
			.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.CREATED)
	@ResponseBody
	@ApiBaseSiteIdParam
	public UserWsDTO registerUser(@ApiParam(value = "User's object.", required = true) @RequestBody final UserSignUpWsDTO user,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues =
					"BASIC, DEFAULT, FULL") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields,
			final HttpServletRequest httpRequest, final HttpServletResponse httpResponse)
			throws DuplicateUidException, UnknownIdentifierException, //NOSONAR
			IllegalArgumentException, WebserviceValidationException, UnsupportedEncodingException //NOSONAR
	{
		validate(user, "user", userSignUpDTOValidator);

		final RegisterData registration = getDataMapper().map(user, RegisterData.class, userProperties);
		customerFacade.register(registration);
		final String userId = user.getUid();
		httpResponse.setHeader(TravelcommercewebservicesConstants.LOCATION, getAbsoluteLocationURL(httpRequest, userId)); //NOSONAR
		return getDataMapper().map(customerFacade.getUserForUID(userId), UserWsDTO.class, fields);
	}

	protected String getAbsoluteLocationURL(final HttpServletRequest httpRequest, final String uid)
			throws UnsupportedEncodingException
	{
		final String requestURL = httpRequest.getRequestURL().toString();
		final StringBuilder absoluteURLSb = new StringBuilder(requestURL);
		if (!requestURL.endsWith(TravelcommercewebservicesConstants.SLASH))
		{
			absoluteURLSb.append(TravelcommercewebservicesConstants.SLASH);
		}
		absoluteURLSb.append(UriUtils.encodePathSegment(uid, StandardCharsets.UTF_8.name()));
		return absoluteURLSb.toString();
	}
}
