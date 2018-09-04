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

package de.hybris.platform.travelfacades.facades.accommodation.handlers.impl;

import de.hybris.platform.commercefacades.accommodation.AccommodationAvailabilityRequestData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.travel.TaxData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.TaxRowModel;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.travelfacades.facades.TravelCommercePriceFacade;
import de.hybris.platform.travelfacades.facades.accommodation.handlers.AccommodationDetailsHandler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Abstract handler for {@link AccommodationPriceHandler} and {@link DealAccommodationPriceHandler}
 */
public abstract class AbstractAccommodationPriceHandler implements AccommodationDetailsHandler
{
	private CommonI18NService commonI18NService;
	private TravelCommercePriceFacade travelCommercePriceFacade;
	private BaseStoreService baseStoreService;

	/**
	 * @deprecated Deprecated since version 3.0.
	 */
	@Deprecated
	private PriceDataFactory priceDataFactory;

	protected String getCurrentCurrency(final AccommodationAvailabilityRequestData availabilityRequestData)
	{
		if (StringUtils.isNotBlank(availabilityRequestData.getCriterion().getCurrencyIso()))
		{
			return availabilityRequestData.getCriterion().getCurrencyIso();
		}
		else
		{
			return getCommonI18NService().getCurrentCurrency().getIsocode();
		}
	}

	protected Double getTaxesValue(final List<TaxData> taxes)
	{
		if (CollectionUtils.isEmpty(taxes))
		{
			return 0d;
		}

		Double result = 0d;
		for (final TaxData tax : taxes)
		{
			result += tax.getPrice().getValue().doubleValue();
		}
		return result;
	}

	protected List<TaxData> getTaxes(final Collection<TaxRowModel> taxes, final String currencyIso, final Double roomRateBasePrice)
	{
		final List<TaxData> result = new ArrayList<>();

		final List<TaxRowModel> taxRowModels = taxes.stream().filter(
				tax -> Objects.isNull(tax.getCurrency()) || StringUtils.equalsIgnoreCase(tax.getCurrency().getIsocode(), currencyIso))
				.collect(Collectors.toList());

		result.addAll(taxRowModels.stream()
				.map(tax -> createTaxData(
						Objects.isNull(tax.getCurrency()) ? ((roomRateBasePrice / 100) * tax.getValue()) : tax.getValue(), currencyIso))
				.collect(Collectors.toList()));

		return result;
	}

	protected List<TaxData> getTaxes(final Collection<TaxRowModel> taxes, final String currencyIso, final Double roomRateBasePrice,
			final boolean isNet)
	{
		final List<TaxData> result = new ArrayList<>();

		final List<TaxRowModel> taxRowModels = taxes.stream().filter(
				tax -> Objects.isNull(tax.getCurrency()) || StringUtils.equalsIgnoreCase(tax.getCurrency().getIsocode(), currencyIso))
				.collect(Collectors.toList());

		result.addAll(taxRowModels.stream()
				.map(tax -> createTaxData(isNet ?
						calculateNetTaxValueForRoomRate(tax, roomRateBasePrice) :
						calculateGrossTaxValueForRoomRate(tax, roomRateBasePrice), currencyIso))
				.collect(Collectors.toList()));

		return result;
	}

	protected Double calculateNetTaxValueForRoomRate(final TaxRowModel tax, final Double roomRateBasePrice)
	{
		final Double value = tax.getValue();
		return Objects.isNull(tax.getCurrency()) ? roomRateBasePrice / 100 * value : value;
	}

	protected Double calculateGrossTaxValueForRoomRate(final TaxRowModel tax, final Double roomRateBasePrice)
	{
		final Double value = tax.getValue();
		return Objects.isNull(tax.getCurrency()) ? (roomRateBasePrice * value) / (100 + value) : value;
	}

	protected Boolean isNetPrice(final ProductModel product)
	{
		return Optional.ofNullable(getTravelCommercePriceFacade().isNetPriceProduct(product))
				.orElse(getBaseStoreService().getCurrentBaseStore().isNet());
	}

	protected TaxData createTaxData(final Double value, final String currencyIso)
	{
		final TaxData taxData = new TaxData();
		taxData.setPrice(getTravelCommercePriceFacade().createPriceData(PriceDataType.BUY, BigDecimal.valueOf(value), currencyIso));
		return taxData;
	}

	protected Double getPriceValue(final List<PriceInformation> priceInformations)
	{
		Double value = null;

		if (CollectionUtils.isNotEmpty(priceInformations))
		{
			value = priceInformations.get(0).getPriceValue().getValue();
		}

		return value != null ? value : 0d;
	}

	/**
	 * @return the commonI18NService
	 */
	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	/**
	 * @param commonI18NService
	 * 		the commonI18NService to set
	 */
	@Required
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	/**
	 * @return the priceDataFactory
	 * @deprecated Deprecated since version 3.0.
	 */
	@Deprecated
	protected PriceDataFactory getPriceDataFactory()
	{
		return priceDataFactory;
	}

	/**
	 * @param priceDataFactory
	 * 		the priceDataFactory to set
	 * @deprecated Deprecated since version 3.0.
	 */
	@Deprecated
	@Required
	public void setPriceDataFactory(final PriceDataFactory priceDataFactory)
	{
		this.priceDataFactory = priceDataFactory;
	}

	/**
	 * @return the travelCommercePriceFacade
	 */
	protected TravelCommercePriceFacade getTravelCommercePriceFacade()
	{
		return travelCommercePriceFacade;
	}

	/**
	 * @param travelCommercePriceFacade
	 * 		the travelCommercePriceFacade to set
	 */
	@Required
	public void setTravelCommercePriceFacade(final TravelCommercePriceFacade travelCommercePriceFacade)
	{
		this.travelCommercePriceFacade = travelCommercePriceFacade;
	}

	/**
	 * @return the baseStoreService
	 */
	protected BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	/**
	 * @param baseStoreService
	 * 		baseStoreService to set
	 */
	@Required
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}
}
