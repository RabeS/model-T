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
package de.hybris.platform.travelcommercewebservices.constants;

@SuppressWarnings({"deprecation","PMD","squid:CallToDeprecatedMethod"})
public class TravelcommercewebservicesConstants extends GeneratedTravelcommercewebservicesConstants
{
	public static final String MODULE_NAME = "travelcommercewebservices";

	public static final String MODULE_WEBROOT = ("travel" + "commercewebservices").equals(MODULE_NAME) ? "rest" : MODULE_NAME;
	public static final String CONTINUE_URL = "session_continue_url";
	public static final String CONTINUE_URL_PAGE = "session_continue_url_page";
	public static final String OPTIONS_SEPARATOR = ",";

	public static final String HTTP_REQUEST_PARAM_LANGUAGE = "lang";
	public static final String HTTP_REQUEST_PARAM_CURRENCY = "curr";

	public static final String ROOT_CONTEXT_PROPERTY = "commercewebservices.rootcontext";
	public static final String V2_ROOT_CONTEXT = "/" + MODULE_WEBROOT + "/v2";
	public static final String URL_SPECIAL_CHARACTERS_PROPERTY = "commercewebservices.url.special.characters";
	public static final String DEFAULT_URL_SPECIAL_CHARACTERS = "?,/";
	public static final String LOCATION = "Location";
	public static final String SLASH = "/";

	//SWAGGER
	public static final String DOCUMENTATION_DESC_PROPERTY = EXTENSIONNAME + ".documentation.desc";
	public static final String DOCUMENTATION_TITLE_PROPERTY = EXTENSIONNAME + ".documentation.title";
	public static final String SEARCH_DOCUMENTATION_DESC_PROPERTY = EXTENSIONNAME + ".search.documentation.desc";
	public static final String SEARCH_DOCUMENTATION_TITLE_PROPERTY = EXTENSIONNAME + ".search.documentation.title";

	public static final String APIKEY_NAME_PROPERTY = EXTENSIONNAME + ".apikey.name";
	public static final String APIKEY_KEYNAME_PROPERTY = EXTENSIONNAME + ".apikey.keyname";
	public static final String APIKEY_PASSAS_PROPERTY = EXTENSIONNAME + ".apikey.passas";
	public static final String API_VERSION = EXTENSIONNAME + ".api.version";
	public static final String HTTPS_PROTOCOL = "https";

	public static final String NOT_VALIDATED = "Not validated to add in cart";
	public static final String AUTHENTICATION_EXCEPTION = "Exception occurred while getting authentication token";
	public static final String AUTHENTICATION_NO_RESULT = "No results found";

	public static final String PAYMENT_TRANSACTIONS = "paymentTransactions";
}
