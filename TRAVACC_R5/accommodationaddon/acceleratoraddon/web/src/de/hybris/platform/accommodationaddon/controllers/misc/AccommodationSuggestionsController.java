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

package de.hybris.platform.accommodationaddon.controllers.misc;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.AbstractController;
import de.hybris.platform.accommodationaddon.controllers.AccommodationaddonControllerConstants;
import de.hybris.platform.commercefacades.accommodation.GlobalSuggestionData;
import de.hybris.platform.travelfacades.accommodation.autosuggestion.wrapper.AccommodationAutoSuggestWrapper;
import de.hybris.platform.travelfacades.facades.accommodation.AccommodationSuggestionFacade;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClientException;

/**
 * Controller for accommodation AutoSuggestion functionality
 */
@Controller
public class AccommodationSuggestionsController extends AbstractController
{

	@Resource(name = "accommodationSuggestionFacade")
	private AccommodationSuggestionFacade accommodationSuggestionFacade;

	@Resource(name = "accommodationAutoSuggestWrapper")
	private AccommodationAutoSuggestWrapper accommodationAutoSuggestWrapper;

	private static final Logger LOG = LoggerFactory.getLogger(AccommodationSuggestionsController.class);
	private static final String SUGGESTION_RESULT = "suggestionResult";
	private static final String IS_GOOGLE_RESULT = "isGoogleResult";
	private static final String IS_REST_CLIENT_EXCEPTION = "isRestClientException";

	/**
	 * @param text
	 *           the search text
	 * @param model
	 *
	 * @return the location of the jsp with the json response for the autosuggestion
	 */
	@RequestMapping(value = "/accommodation-suggestions", method = RequestMethod.GET, produces = "application/json")
	public String locationSuggestion(@RequestParam(value = "text") final String text, final Model model)
	{
		List<GlobalSuggestionData> suggestionResults = accommodationSuggestionFacade.getLocationSuggestions(text);

		if (CollectionUtils.isEmpty(suggestionResults))
		{
			try
			{
				// google search
				suggestionResults = accommodationAutoSuggestWrapper.getAutoCompleteResults(text);
			}
			catch (RestClientException e)
			{
				LOG.debug(e.getCause().toString(), e);
				LOG.warn("GoogleMapAutosuggestWrapper RestClientException. No connection to Google Map AutoSuggest API. ");
				model.addAttribute(IS_REST_CLIENT_EXCEPTION, Boolean.TRUE);
			}
			model.addAttribute(IS_GOOGLE_RESULT, Boolean.TRUE);
		}

		model.addAttribute(SUGGESTION_RESULT, suggestionResults);


		return AccommodationaddonControllerConstants.Views.Pages.Suggestions.SuggestionsSearchJsonResponse;
	}

}
