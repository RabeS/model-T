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

package de.hybris.platform.travelfacades.fare.search.handlers.impl;

import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.travel.FareSearchRequestData;
import de.hybris.platform.commercefacades.travel.FareSelectionData;
import de.hybris.platform.commercefacades.travel.ItineraryPricingInfoData;
import de.hybris.platform.commercefacades.travel.ScheduledRouteData;
import de.hybris.platform.travelfacades.facades.TravelBundleTemplateFacade;
import de.hybris.platform.travelfacades.fare.search.handlers.FareSearchHandler;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Required;


/**
 * Concrete implementation of the {@link FareSearchHandler} interface. Handler is responsible for populating the total
 * prices for ItineraryPricingInfoData with respect to selected ItineraryPricingInfoData of each pricedItineraryData
 */
public class UpdateUpgradeBundlePricesHandler implements FareSearchHandler
{
	private static final String PLUS_SIGN = "+";
	private TravelBundleTemplateFacade travelBundleTemplateFacade;

	@Override
	public void handle(final List<ScheduledRouteData> scheduledRoutes, final FareSearchRequestData fareSearchRequestData,
			final FareSelectionData fareSelectionData)
	{
		fareSelectionData.getPricedItineraries().forEach(pricedItineraryData -> {
			final ItineraryPricingInfoData selectedItineraryPricingInfoData = getTravelBundleTemplateFacade()
					.getSelectedItineraryPricingInfoData(pricedItineraryData);
			if (Objects.nonNull(selectedItineraryPricingInfoData))
			{
				final BigDecimal selectedItineraryTotalPrice = selectedItineraryPricingInfoData.getTotalFare().getTotalPrice()
						.getValue();
				pricedItineraryData.getItineraryPricingInfos().forEach(itineraryPricingInfo -> {
					getTravelBundleTemplateFacade().createUpgradeItineraryPricingInfoTotalPriceData(selectedItineraryTotalPrice,
							itineraryPricingInfo);
					updateFormattedValue(itineraryPricingInfo.getTotalFare().getPriceDifference());
				});
			}
		});
	}

	/**
	 * Updates the formattedValue property of the given {@link PriceData} to display a PLUS SIGN for non negative values
	 *
	 * @param priceData
	 * 		as the priceData to update
	 */
	protected void updateFormattedValue(final PriceData priceData)
	{
		if (priceData.getValue().compareTo(BigDecimal.ZERO) >= 0)
		{
			priceData.setFormattedValue(PLUS_SIGN + priceData.getFormattedValue());
		}
	}


	/**
	 * @return the travelBundleTemplateFacade
	 */
	protected TravelBundleTemplateFacade getTravelBundleTemplateFacade()
	{
		return travelBundleTemplateFacade;
	}

	/**
	 * @param travelBundleTemplateFacade
	 *           the travelBundleTemplateFacade to set
	 */
	@Required
	public void setTravelBundleTemplateFacade(final TravelBundleTemplateFacade travelBundleTemplateFacade)
	{
		this.travelBundleTemplateFacade = travelBundleTemplateFacade;
	}
}
