/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All  rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.travelcommercewebservices.facades.oauth;

import de.hybris.platform.travelcommercewebservices.enums.OauthType;
import de.hybris.platform.travelcommercewebservices.oauth.data.AuthenticationResponseData;

import javax.servlet.http.HttpServletRequest;


/**
 * Facade that exposes oauth functionality
 */
public interface TravelOauthWsFacade
{

	/**
	 * Get auth token
	 *
	 * @param request
	 * 		request call
	 * @param userId
	 * 		userId
	 * @param userPassword
	 * 		userPassword
	 * @param oauthType
	 * 		oauthType
	 * @param baseSiteId
	 * 		baseSiteId
	 * @return {@link AuthenticationResponseData}
	 */

	public AuthenticationResponseData getToken(HttpServletRequest request, String userId, String userPassword, OauthType
			oauthType,
			String baseSiteId);
}
