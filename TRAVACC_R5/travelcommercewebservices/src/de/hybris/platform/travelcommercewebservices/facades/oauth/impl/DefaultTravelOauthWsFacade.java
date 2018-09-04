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

package de.hybris.platform.travelcommercewebservices.facades.oauth.impl;

import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.travelcommercewebservices.enums.OauthType;
import de.hybris.platform.travelcommercewebservices.facades.oauth.TravelOauthWsFacade;
import de.hybris.platform.travelcommercewebservices.facades.oauth.exceptions.TravelAuthenticationException;
import de.hybris.platform.travelcommercewebservices.facades.oauth.exceptions.TravelBadRequestException;
import de.hybris.platform.travelcommercewebservices.facades.oauth.resolver.TravelOauthUrlResolver;
import de.hybris.platform.travelcommercewebservices.oauth.config.TravelOauthConfig;
import de.hybris.platform.travelcommercewebservices.oauth.config.TravelOauthUrlConfig;
import de.hybris.platform.travelcommercewebservices.oauth.data.AuthenticationResponseData;
import de.hybris.platform.travelcommercewebservices.oauth.data.OAuthData;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Default implementation of {@link TravelOauthWsFacade}
 */
public class DefaultTravelOauthWsFacade implements TravelOauthWsFacade
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultTravelOauthWsFacade.class);

	private UserService userService;
	private BaseSiteService baseSiteService;
	private ProviderManager authenticationManager;
	private ObjectMapper objectMapper;
	private TravelOauthConfig travelOauthConfig;
	private TravelOauthUrlResolver travelOauthUrlResolver;
	private Converter<OAuthData, AuthenticationResponseData> travelOauthConverter;

	@Override
	public AuthenticationResponseData getToken(final HttpServletRequest request, final String userId, final String userPassword,
			final OauthType oauthType, final String baseSiteId)
	{

		if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(userPassword))
		{
			throw new RequestParameterException("Credentials are not informed.", RequestParameterException.MISSING,
					"credentialInfo");
		}
		getBaseSiteService().setCurrentBaseSite(baseSiteId, false);
		final Optional<AuthenticationResponseData> authenticationResponseData = createToken(buildUrl(request), oauthType, userId,
				userPassword);
		if (!authenticationResponseData.isPresent())
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Not getting any auth token from the system");
			}
			throw new TravelAuthenticationException("Authentication fail. Contact with administrator.");
		}
		return authenticationResponseData.get();
	}

	/**
	 * Create auth token.
	 *
	 * @param host
	 * 		the host
	 * @param oauthType
	 * 		the oauthType like client/user/refresh
	 * @param credentials
	 * 		the credentials of the user
	 * @return {@link AuthenticationResponseData}
	 */
	public Optional<AuthenticationResponseData> createToken(final String host, final OauthType oauthType,
			final String... credentials)
	{
		if (isOauthUser(oauthType))
		{
			authenticate(credentials[0], credentials[1]);
		}
		return getToken(host, oauthType, credentials);
	}

	protected Optional<AuthenticationResponseData> getToken(final String host, final OauthType oauthType,
			final String... credentials)
	{
		final Optional<OAuthData> oAuthData = generateToken(host, oauthType, credentials);
		final AuthenticationResponseData authenticationResponseData = getAuthenticationResponseData(oAuthData,
				!isOauthUser(oauthType) ? StringUtils.EMPTY : credentials[0]);
		return Optional.ofNullable(authenticationResponseData);
	}

	protected Optional<OAuthData> generateToken(final String host, final OauthType oauthType, final String... credentials)
	{
		Optional<OAuthData> oAuthData = Optional.empty();
		try
		{
			final CloseableHttpClient httpClient = getHttpClient();
			final String url = new StringBuffer(host).append(getTravelOauthConfig().getKeyTokenPath()).toString();
			final HttpPost httpPostRequest = new HttpPost(url);
			httpPostRequest.setHeader(getTravelOauthConfig().getOauthAccept(), getTravelOauthConfig().getOauthContentType());

			final List<NameValuePair> params = new ArrayList<>();
			params.add(new BasicNameValuePair(getTravelOauthConfig().getOauthClientId(),
					isOauthClient(oauthType) ? credentials[0] : getTravelOauthConfig().getKeyClientId()));
			params.add(new BasicNameValuePair(getTravelOauthConfig().getOauthClientSecret(),
					isOauthClient(oauthType) ? credentials[1] : getTravelOauthConfig().getKeyClientSecret()));

			if (Objects.equals(OauthType.REFRESH, oauthType))
			{
				params.add(new BasicNameValuePair(getTravelOauthConfig().getOauthGrandType(),
						getTravelOauthConfig().getOauthGrandTypeRefreshToken()));
				params.add(new BasicNameValuePair(getTravelOauthConfig().getOauthRefresh(), credentials[0]));
			}
			else if (isOauthUser(oauthType) && StringUtils.isNotEmpty(credentials[0]) && StringUtils.isNotEmpty(credentials[1]))
			{
				params.add(new BasicNameValuePair(getTravelOauthConfig().getOauthGrandType(),
						getTravelOauthConfig().getOauthGrandTypePassword()));
				params.add(new BasicNameValuePair(getTravelOauthConfig().getOauthUserName(), credentials[0]));
				params.add(new BasicNameValuePair(getTravelOauthConfig().getOauthPassword(), credentials[1]));
			}
			else
			{
				params.add(new BasicNameValuePair(getTravelOauthConfig().getOauthGrandType(),
						getTravelOauthConfig().getOauthGrandTypeClientCredentials()));
			}
			httpPostRequest.setEntity(new UrlEncodedFormEntity(params));
			httpPostRequest.setEntity(new UrlEncodedFormEntity(params, getTravelOauthConfig().getOauthEncoding()));

			final HttpResponse response = httpClient.execute(httpPostRequest);

			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
			{
				LOG.error("Token not generated. Http status got: " + response.getStatusLine().getStatusCode());
				throw new TravelBadRequestException("Access token can not be generated");
			}

			final String jsonResponse = EntityUtils.toString(response.getEntity());
			oAuthData = Optional.of(getObjectMapper().readValue(jsonResponse, OAuthData.class));

		}
		catch ( final ParseException | IOException ex )
		{
			if(LOG.isDebugEnabled())
			{
				LOG.debug("Auth token can not be generated", ex);
			}
			else
			{
				LOG.error("Auth token can not be generated");
			}
			throw new TravelBadRequestException("Auth token can not be generated");
		}
		return oAuthData;
	}

	protected boolean isOauthUser(final OauthType oauthType)
	{
		return OauthType.USER.name().equalsIgnoreCase(oauthType.name());
	}

	protected void authenticate(final String uid, final String password)
	{
		try
		{
			if (StringUtils.isNotEmpty(uid) && StringUtils.isNotEmpty(password))
			{
				getUserService().getUserForUID(uid);
				final Authentication authResult = getAuthenticationManager()
						.authenticate(new UsernamePasswordAuthenticationToken(uid, password));
				if (Objects.isNull(authResult) || !authResult.isAuthenticated())
				{
					throw new TravelAuthenticationException("Wrong credentials.Authentication error");
				}
			}
		}
		catch ( final AmbiguousIdentifierException | AuthenticationException | UnknownIdentifierException ex )
		{
			LOG.error("Error getting access credentials provided", ex);
			throw new TravelAuthenticationException("Wrong credentials. Check user and password information");
		}
	}

	protected AuthenticationResponseData getAuthenticationResponseData(final Optional<OAuthData> oAuthData, final String uid)
	{
		AuthenticationResponseData authenticationResponseData = null;
		if (!oAuthData.isPresent())
		{
			return authenticationResponseData;
		}
		authenticationResponseData = getTravelOauthConverter().convert(oAuthData.get());
		if (Objects.nonNull(authenticationResponseData) && StringUtils.isNotEmpty(uid))
		{
			authenticationResponseData.setCustomerId(uid);
		}

		return authenticationResponseData;
	}

	protected CloseableHttpClient getHttpClient()
	{
		final boolean isDisable = getTravelOauthUrlResolver().isHttpClientDisable();
		if (isDisable)
		{
			return getHttpClientForUntrustedCerts();
		}
		else
		{
			return getHttpClientForTrustedCerts();
		}
	}

	protected CloseableHttpClient getHttpClientForTrustedCerts()
	{
		return HttpClientBuilder.create().build();
	}

	protected CloseableHttpClient getHttpClientForUntrustedCerts()
	{
		final HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

		SSLContext sslContext = null;
		try
		{
			sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy()
			{
				@Override
				public boolean isTrusted(final X509Certificate[] arg0, final String arg1) throws CertificateException
				{
					return true;
				}
			}).build();
		}
		catch ( final NoSuchAlgorithmException | KeyManagementException | KeyStoreException ex )
		{
			LOG.error("Error getting untrustedCerts ", ex);
		}
		httpClientBuilder.setSslcontext(sslContext);

		final HostnameVerifier hostnameVerifier = (urlHostname, sslSession) -> true;

		final SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
		final Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", sslSocketFactory).build();

		final PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
		httpClientBuilder.setConnectionManager(connMgr);

		return httpClientBuilder.build();
	}

	/**
	 * Build URL to make request to the server
	 *
	 * @param request
	 * @return
	 */
	public String buildUrl(final HttpServletRequest request)
	{
		TravelOauthUrlConfig travelOauthUrlConfig = new TravelOauthUrlConfig();
		try
		{
			travelOauthUrlConfig = getTravelOauthUrlResolver().resolveProperties(getBaseSiteService().getCurrentBaseSite().getUid
					());

			final URL url = new URL(request.getScheme(), travelOauthUrlConfig.getHost(),
					request.isSecure() ? travelOauthUrlConfig.getSslPort() : travelOauthUrlConfig.getHttpPort(), StringUtils.EMPTY);
			return url.toString();
		}
		catch ( final MalformedURLException e )
		{
			return travelOauthUrlConfig.getHttpUrl();
		}
	}

	protected boolean isOauthClient(final OauthType oauthType)
	{
		return oauthType.name().equalsIgnoreCase(OauthType.CLIENT.name());
	}

	/**
	 * Gets the user service.
	 *
	 * @return user service
	 */
	protected UserService getUserService()
	{
		return userService;
	}

	/**
	 * Sets the user service.
	 *
	 * @param userService
	 * 		the userService
	 */
	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	/**
	 * Gets the authentication manager.
	 *
	 * @return authentication manager
	 */
	protected ProviderManager getAuthenticationManager()
	{
		return authenticationManager;
	}

	/**
	 * Sets the authentication manager.
	 *
	 * @param authenticationManager
	 * 		the authentication manager
	 */
	@Required
	public void setAuthenticationManager(final ProviderManager authenticationManager)
	{
		this.authenticationManager = authenticationManager;
	}

	/**
	 * Gets the travel oauth converter.
	 *
	 * @return travel oauth converter
	 */
	protected Converter<OAuthData, AuthenticationResponseData> getTravelOauthConverter()
	{
		return travelOauthConverter;
	}

	/**
	 * Sets the travel oauth converter.
	 *
	 * @param travelOauthConverter
	 * 		the travel oauth converter.
	 */
	@Required
	public void setTravelOauthConverter(final Converter<OAuthData, AuthenticationResponseData> travelOauthConverter)
	{
		this.travelOauthConverter = travelOauthConverter;
	}

	/**
	 * Gets the object mapper.
	 *
	 * @return object mapper
	 */
	protected ObjectMapper getObjectMapper()
	{
		return objectMapper;
	}

	/**
	 * Sets the object mapper.
	 *
	 * @param objectMapper
	 * 		the object mapper.
	 */
	@Required
	public void setObjectMapper(final ObjectMapper objectMapper)
	{
		this.objectMapper = objectMapper;
	}

	/**
	 * Gets the base site service.
	 *
	 * @return baseSiteService
	 */
	public BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	/**
	 * Sets the base site service.
	 *
	 * @param baseSiteService
	 * 		the baseSiteService.
	 */
	@Required
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	/**
	 * Gets the travel oauth config.
	 *
	 * @return travelOauthConfig
	 */
	public TravelOauthConfig getTravelOauthConfig()
	{
		return travelOauthConfig;
	}

	/**
	 * Sets the travel oauth config.
	 *
	 * @param travelOauthConfig
	 * 		the travelOauthConfig.
	 */
	@Required
	public void setTravelOauthConfig(final TravelOauthConfig travelOauthConfig)
	{
		this.travelOauthConfig = travelOauthConfig;
	}

	/**
	 * Gets the travel oauth url resolver.
	 *
	 * @return travelOauthUrlResolver
	 */
	public TravelOauthUrlResolver getTravelOauthUrlResolver()
	{
		return travelOauthUrlResolver;
	}

	/**
	 * Sets the travel oauth url resolver.
	 *
	 * @param travelOauthUrlResolver
	 * 		the travelOauthUrlResolver.
	 */
	@Required
	public void setTravelOauthUrlResolver(final TravelOauthUrlResolver travelOauthUrlResolver)
	{
		this.travelOauthUrlResolver = travelOauthUrlResolver;
	}
}
