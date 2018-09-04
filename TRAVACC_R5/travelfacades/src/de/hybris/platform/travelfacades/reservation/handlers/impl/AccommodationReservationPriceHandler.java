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

package de.hybris.platform.travelfacades.reservation.handlers.impl;

import de.hybris.platform.commercefacades.accommodation.AccommodationReservationData;
import de.hybris.platform.commercefacades.accommodation.RateData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.travel.TaxData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.travelfacades.facades.TravelCommercePriceFacade;
import de.hybris.platform.travelfacades.order.TravelCartFacade;
import de.hybris.platform.travelfacades.reservation.handlers.AccommodationReservationHandler;
import de.hybris.platform.travelfacades.util.PricingUtils;
import de.hybris.platform.travelservices.enums.OrderEntryType;
import de.hybris.platform.travelservices.strategies.payment.OrderTotalPaidForOrderEntryTypeCalculationStrategy;
import de.hybris.platform.travelservices.strategies.payment.TaxCompensationStrategy;
import de.hybris.platform.travelservices.strategies.payment.impl.AccommodationGlobalDiscountCalculationStrategy;
import de.hybris.platform.util.DiscountValue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Required;



/**
 * Handler responsible for populating Price(TotalRate and TotalToPay) in AccommodationReservationData
 */
public class AccommodationReservationPriceHandler implements AccommodationReservationHandler
{
	private OrderTotalPaidForOrderEntryTypeCalculationStrategy orderTotalPaidForOrderEntryTypeCalculationStrategy;
	private TravelCommercePriceFacade travelCommercePriceFacade;
	private TravelCartFacade cartFacade;
	private TaxCompensationStrategy taxCompensationStrategy;
	private AccommodationGlobalDiscountCalculationStrategy accommodationGlobalDiscountCalculationStrategy;

	/**
	 * @deprecated Deprecated since version 3.0.
	 */
	@Deprecated
	private PriceDataFactory priceDataFactory;

	@Override
	public void handle(final AbstractOrderModel abstractOrder, final AccommodationReservationData accommodationReservationData)
	{
		populateTotalRate(abstractOrder, accommodationReservationData);
		populateFullRate(abstractOrder, accommodationReservationData);
		populateTotalToPay(abstractOrder, accommodationReservationData);
	}

	/**
	 * Gets total rate.
	 *
	 * @param abstractOrder
	 * 		the abstract order
	 * @param accommodationReservationData
	 * 		the accommodation reservation data
	 * @return the total rate
	 */
	protected void populateTotalRate(final AbstractOrderModel abstractOrder,
			final AccommodationReservationData accommodationReservationData)
	{
		Double totalRate = 0d;
		for (final AbstractOrderEntryModel entry : abstractOrder.getEntries())
		{
			if (BooleanUtils.isTrue(entry.getActive()) && OrderEntryType.ACCOMMODATION.equals(entry.getType()))
			{
				totalRate = Double.sum(totalRate, entry.getTotalPrice());
				final boolean isNetPriceApplied = Optional.ofNullable(getCartFacade().isNetPrice(entry))
						.orElse(abstractOrder.getNet());
				if (isNetPriceApplied && CollectionUtils.isNotEmpty(entry.getTaxValues()))
				{
					totalRate = Double
							.sum(totalRate, entry.getTaxValues().stream().mapToDouble(taxValue -> taxValue.getAppliedValue()).sum());
				}
			}
		}
		final BigDecimal discount = getCartFacade().getGlobalDiscountForEntryType(abstractOrder, OrderEntryType.ACCOMMODATION);
		if (discount.doubleValue() > 0)
		{
			totalRate -= discount.doubleValue();
			final BigDecimal taxToCompensate = getTaxCompensationStrategy()
					.calculateTaxCompensationForEntries(abstractOrder, discount,
							abstractOrder.getEntries().stream().filter(entry -> OrderEntryType.ACCOMMODATION.equals(entry.getType()))
									.collect(Collectors.toList()));
			totalRate -= taxToCompensate.doubleValue();
		}
		final PriceData actualPrice = createPriceData(BigDecimal.valueOf(totalRate), abstractOrder.getCurrency().getIsocode());
		final RateData rate = new RateData();
		rate.setActualRate(actualPrice);
		accommodationReservationData.setTotalRate(rate);
	}


	/**
	 * Populate total to pay.
	 *
	 * @param abstractOrder
	 * 		the abstract order
	 * @param accommodationReservationData
	 * 		the accommodation reservation data
	 */
	protected void populateTotalToPay(final AbstractOrderModel abstractOrder,
			final AccommodationReservationData accommodationReservationData)
	{
		final Double totalPaid = getOrderTotalPaidForOrderEntryTypeCalculationStrategy()
				.calculate(abstractOrder, OrderEntryType.ACCOMMODATION).doubleValue();
		final Double totalRate = accommodationReservationData.getTotalRate().getActualRate().getValue().doubleValue();
		final PriceData totalToPay = createPriceData(BigDecimal.valueOf(totalRate - totalPaid),
				abstractOrder.getCurrency().getIsocode());
		accommodationReservationData.setTotalToPay(totalToPay);
	}


	private void populateFullRate(final AbstractOrderModel abstractOrder,
			final AccommodationReservationData accommodationReservationData)
	{
		final RateData rateData = accommodationReservationData.getTotalRate();

		final List<AbstractOrderEntryModel> activeEntries = abstractOrder.getEntries().stream()
				.filter(entry -> entry.getActive() && OrderEntryType.ACCOMMODATION.equals(entry.getType()))
				.collect(Collectors.toList());

		final Double totalBasePrice = activeEntries.stream().mapToDouble(entry -> entry.getBasePrice() * entry.getQuantity())
				.sum();
		rateData.setBasePrice(getTravelCommercePriceFacade().createPriceData(totalBasePrice, 2));

		Double discounts = activeEntries.stream().flatMap(entry -> entry.getDiscountValues().stream())
				.mapToDouble(DiscountValue::getAppliedValue).sum();
		discounts += getAccommodationGlobalDiscountCalculationStrategy().getGlobalDiscount(abstractOrder, activeEntries)
				.doubleValue();
		rateData.setTotalDiscount(getTravelCommercePriceFacade().createPriceData(discounts, 2));
		PricingUtils.changeFormattedValueAsNegative(rateData.getTotalDiscount());

		final TaxData taxData = new TaxData();
		final Double accommodationTaxes = activeEntries.stream().flatMap(entry -> entry.getTaxValues().stream())
				.mapToDouble(taxEntry -> taxEntry.getAppliedValue()).sum();
		taxData.setPrice(getTravelCommercePriceFacade().createPriceData(accommodationTaxes, 2));
		rateData.setTotalTax(taxData);
		rateData.setNet(abstractOrder.getNet());

		final Double wasRate = totalBasePrice - discounts + taxData.getPrice().getValue().doubleValue();
		rateData.setWasRate(getTravelCommercePriceFacade().createPriceData(wasRate, abstractOrder.getCurrency().getIsocode()));
	}


	/**
	 * Create price data price data.
	 *
	 * @param value
	 * 		the value
	 * @param isoCode
	 * 		the iso code
	 * @return the price data
	 */
	protected PriceData createPriceData(final BigDecimal value, final String isoCode)
	{
		return getTravelCommercePriceFacade().createPriceData(PriceDataType.BUY, value, isoCode);
	}

	/**
	 * Gets price data factory.
	 *
	 * @return the price data factory
	 * @deprecated Deprecated since version 3.0.
	 */
	@Deprecated
	protected PriceDataFactory getPriceDataFactory()
	{
		return priceDataFactory;
	}

	/**
	 * Sets price data factory.
	 *
	 * @param priceDataFactory
	 * 		the price data factory
	 * @deprecated Deprecated since version 3.0.
	 */
	@Deprecated
	@Required
	public void setPriceDataFactory(final PriceDataFactory priceDataFactory)
	{
		this.priceDataFactory = priceDataFactory;
	}

	/**
	 * Gets order total paid for order entry type calculation strategy.
	 *
	 * @return the orderTotalPaidForOrderEntryTypeCalculationStrategy
	 */
	protected OrderTotalPaidForOrderEntryTypeCalculationStrategy getOrderTotalPaidForOrderEntryTypeCalculationStrategy()
	{
		return orderTotalPaidForOrderEntryTypeCalculationStrategy;
	}

	/**
	 * Sets order total paid for order entry type calculation strategy.
	 *
	 * @param orderTotalPaidForOrderEntryTypeCalculationStrategy
	 * 		the orderTotalPaidForOrderEntryTypeCalculationStrategy to set
	 */
	@Required
	public void setOrderTotalPaidForOrderEntryTypeCalculationStrategy(
			final OrderTotalPaidForOrderEntryTypeCalculationStrategy orderTotalPaidForOrderEntryTypeCalculationStrategy)
	{
		this.orderTotalPaidForOrderEntryTypeCalculationStrategy = orderTotalPaidForOrderEntryTypeCalculationStrategy;
	}

	/**
	 * Gets travel commerce price facade.
	 *
	 * @return the travelCommercePriceFacade
	 */
	protected TravelCommercePriceFacade getTravelCommercePriceFacade()
	{
		return travelCommercePriceFacade;
	}

	/**
	 * Sets travel commerce price facade.
	 *
	 * @param travelCommercePriceFacade
	 * 		the travelCommercePriceFacade to set
	 */
	@Required
	public void setTravelCommercePriceFacade(final TravelCommercePriceFacade travelCommercePriceFacade)
	{
		this.travelCommercePriceFacade = travelCommercePriceFacade;
	}

	/**
	 * Gets cart facade.
	 *
	 * @return the cartFacade
	 */
	protected TravelCartFacade getCartFacade()
	{
		return cartFacade;
	}

	/**
	 * Sets cart facade.
	 *
	 * @param cartFacade
	 * 		the cartFacade to set
	 */
	@Required
	public void setCartFacade(final TravelCartFacade cartFacade)
	{
		this.cartFacade = cartFacade;
	}

	/**
	 * Gets tax compensation strategy.
	 *
	 * @return the taxCompensationStrategy
	 */
	protected TaxCompensationStrategy getTaxCompensationStrategy()
	{
		return taxCompensationStrategy;
	}

	/**
	 * Sets tax compensation strategy.
	 *
	 * @param taxCompensationStrategy
	 * 		the taxCompensationStrategy to set
	 */
	@Required
	public void setTaxCompensationStrategy(final TaxCompensationStrategy taxCompensationStrategy)
	{
		this.taxCompensationStrategy = taxCompensationStrategy;
	}

	/**
	 * Gets accommodation global discount calculation strategy.
	 *
	 * @return the accommodation global discount calculation strategy
	 */
	protected AccommodationGlobalDiscountCalculationStrategy getAccommodationGlobalDiscountCalculationStrategy()
	{
		return accommodationGlobalDiscountCalculationStrategy;
	}

	/**
	 * Sets accommodation global discount calculation strategy.
	 *
	 * @param accommodationGlobalDiscountCalculationStrategy
	 * 		the accommodation global discount calculation strategy
	 */
	@Required
	public void setAccommodationGlobalDiscountCalculationStrategy(
			final AccommodationGlobalDiscountCalculationStrategy accommodationGlobalDiscountCalculationStrategy)
	{
		this.accommodationGlobalDiscountCalculationStrategy = accommodationGlobalDiscountCalculationStrategy;
	}
}
