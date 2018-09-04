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

package de.hybris.platform.travelcommercewebservices.facades.oauth.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.travelcommercewebservices.oauth.data.AuthenticationResponseData;
import de.hybris.platform.travelcommercewebservices.oauth.data.OAuthData;

import org.apache.commons.lang.StringUtils;


/**
 * Converter implementation for {@link OAuthData} as source and {@link AuthenticationResponseData} as target type.
 */
public class TravelOauthPopulator implements Populator<OAuthData, AuthenticationResponseData>
{

	@Override
	public void populate(final OAuthData oAuthData, final AuthenticationResponseData authenticationResponseData)
			throws ConversionException
	{

		authenticationResponseData.setAccessToken(oAuthData.getAccess_token());
		authenticationResponseData.setExpiresIn(oAuthData.getExpires_in());
		if (StringUtils.isNotEmpty(oAuthData.getRefresh_token()))
		{
			authenticationResponseData.setRefreshToken(oAuthData.getRefresh_token());
		}
		if (StringUtils.isNotEmpty(oAuthData.getScope()))
		{
			authenticationResponseData.setScope(oAuthData.getScope());
		}
		if (StringUtils.isNotEmpty(oAuthData.getToken_type()))
		{
			authenticationResponseData.setTokenType(oAuthData.getToken_type());
		}
	}
}
