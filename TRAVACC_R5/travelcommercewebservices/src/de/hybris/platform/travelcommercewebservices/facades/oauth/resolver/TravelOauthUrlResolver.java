package de.hybris.platform.travelcommercewebservices.facades.oauth.resolver;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.travelcommercewebservices.oauth.config.TravelOauthUrlConfig;

import java.util.StringJoiner;

import org.springframework.beans.factory.annotation.Required;

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


public class TravelOauthUrlResolver
{
	protected final String PREFIX = "authentication";
	protected final String HTTP = "http";
	protected final String URL = "url";
	protected final String SSL = "https";
	protected final String HOST = "host";
	protected final String HTTP_PORT = "applicationserver.http.port";
	protected final String SSL_PORT = "applicationserver.ssl.port";
	protected final String SSL_DISABLE_CLIENT_SIDE = "authentication.ssl.disable.clientside";
	protected final boolean DISABLING_CLIENT_SIDE_DEFAULT = true;

	private ConfigurationService configurationService;

	public TravelOauthUrlConfig resolveProperties(final String baseSiteId)
	{
		final TravelOauthUrlConfig travelOauthUrlConfig = new TravelOauthUrlConfig();

		final StringJoiner httpJoiner = new StringJoiner(".");
		httpJoiner.add(PREFIX).add(baseSiteId).add(HTTP).add(URL);

		final StringJoiner sslJoiner = new StringJoiner(".");
		sslJoiner.add(PREFIX).add(baseSiteId).add(SSL).add(URL);

		final StringJoiner hostJoiner = new StringJoiner(".");
		hostJoiner.add(PREFIX).add(baseSiteId).add(HOST);

		travelOauthUrlConfig.setHttpUrl(getConfigurationService().getConfiguration().getString(httpJoiner.toString()));
		travelOauthUrlConfig.setSslUrl(getConfigurationService().getConfiguration().getString(sslJoiner.toString()));
		travelOauthUrlConfig.setHost(getConfigurationService().getConfiguration().getString(hostJoiner.toString()));
		travelOauthUrlConfig.setHttpPort(getConfigurationService().getConfiguration().getInt(HTTP_PORT));
		travelOauthUrlConfig.setSslPort(getConfigurationService().getConfiguration().getInt(SSL_PORT));
		return travelOauthUrlConfig;
	}

	public boolean isHttpClientDisable()
	{
		return getConfigurationService().getConfiguration().getBoolean(SSL_DISABLE_CLIENT_SIDE, DISABLING_CLIENT_SIDE_DEFAULT);
	}

	/**
	 * Gets the configurationService.
	 *
	 * @return configurationService
	 */
	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	/**
	 * Sets the configurationService.
	 *
	 * @param configurationService
	 * 		the configurationService.
	 */
	@Required
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}
}
