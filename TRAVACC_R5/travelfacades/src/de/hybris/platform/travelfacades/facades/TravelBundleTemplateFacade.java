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

package de.hybris.platform.travelfacades.facades;

import de.hybris.platform.commercefacades.travel.ItineraryPricingInfoData;
import de.hybris.platform.commercefacades.travel.PricedItineraryData;
import de.hybris.platform.commercefacades.travel.TravelBundleTemplateData;
import de.hybris.platform.commercefacades.travel.ancillary.data.UpgradeOptionData;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import java.math.BigDecimal;


/**
 * Facade for upgrade bundle section
 */
public interface TravelBundleTemplateFacade
{

	/**
	 * Creates Upgradable Options Data for each itinerary on an ancillary page
	 *
	 * @param pricedItinerary
	 * 		the priced itinerary
	 * @param bundleType
	 * 		the bundle type
	 *
	 * @deprecated Deprecated since version 3.0.
	 *
	 * @return the upgrade option data
	 */
	@Deprecated
	UpgradeOptionData createUpgradableOptionsData(PricedItineraryData pricedItinerary, String bundleType);

	/**
	 * finds the selected itinerary pricing info data from the list using the selected attribute
	 *
	 * @param pricedItineraryData
	 * 		the priced itinerary data
	 * @return selected itinerary pricing info data
	 */
	ItineraryPricingInfoData getSelectedItineraryPricingInfoData(PricedItineraryData pricedItineraryData);

	/**
	 * used to find out the sequence number of any bundle
	 *
	 * @param bundleType
	 * 		the bundle type
	 * @return sequence number
	 */
	int getSequenceNumber(String bundleType);

	/**
	 * Method to update only the total price for the ItineraryPricingInfo respective to selected ItineraryPricingInfo.
	 *
	 * @param selectedItineraryPricingInfoData
	 *           the ItineraryPricingInfo
	 * @param availableUpgradeItineraryPricingInfoData
	 *           availableUpgradeItineraryPricingInfoData
	 */
	void createUpgradeItineraryPricingInfoTotalPriceData(BigDecimal selectedItineraryPricingInfoData,
			ItineraryPricingInfoData availableUpgradeItineraryPricingInfoData);

	/**
	 * Returns the master bundle template id of the specified template id
	 *
	 * @param bundleTemplateId
	 * @return
	 */
	String getMasterBundleTemplateId(String bundleTemplateId);
	
	/**
	 * Returns the bundle template for a given bundle id and catalog
	 * @param bundleTemplateId
	 * @param catalogId
	 * @return
	 * @throws ModelNotFoundException
	 * @throws AmbiguousIdentifierException
	 */
	TravelBundleTemplateData getBundleTemplateByCodeAndCatalogVersion(String bundleTemplateId, String catalogId) throws ModelNotFoundException,AmbiguousIdentifierException;

}
