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

package de.hybris.platform.travelservices.strategies.payment.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.travelservices.enums.AmendStatus;
import de.hybris.platform.travelservices.enums.OrderEntryType;
import de.hybris.platform.travelservices.order.TravelCartService;
import de.hybris.platform.travelservices.order.data.EntryTypePaymentInfo;
import de.hybris.platform.travelservices.services.BookingService;
import de.hybris.platform.travelservices.strategies.payment.GlobalDiscountByEntryTypeCalculationStrategy;
import de.hybris.platform.travelservices.strategies.payment.TaxCompensationStrategy;
import de.hybris.platform.util.TaxValue;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;



/**
 * Handles common method for pay in advance strategies
 */
public abstract class AbstractPaymentInfoStrategy
{
	private TravelCartService travelCartService;
	private TaxCompensationStrategy taxCompensationStrategy;
	private Map<OrderEntryType, GlobalDiscountByEntryTypeCalculationStrategy> globalDiscountCalculationStrategyMap;
	private BookingService bookingService;

	protected Double calculateTaxes(final List<AbstractOrderEntryModel> entries)
	{
		final List<AbstractOrderEntryModel> netEntries = entries.stream().filter(getTravelCartService()::isNetPricedEntry).collect(
				Collectors.toList());
		if (CollectionUtils.isEmpty(netEntries))
		{
			return 0.0d;
		}
		final List<TaxValue> taxValues = netEntries.stream().flatMap(entry -> entry.getTaxValues().stream())
				.collect(Collectors.toList());
		return CollectionUtils.isNotEmpty(taxValues) ? taxValues.stream().mapToDouble(taxValue -> taxValue.getAppliedValue()).sum()
				: 0.0d;
	}

	protected Double getBookingTimeAmount(final List<AbstractOrderEntryModel> entries, final AbstractOrderModel abstractOrder)
	{
		return getEntriesAmount(
				entries.stream().filter(
						entry -> entry.getActive() && Arrays.asList(AmendStatus.NEW, AmendStatus.SAME).contains(entry.getAmendStatus()))
						.collect(Collectors.toList()));
	}

	protected Double getEntriesAmount(final List<AbstractOrderEntryModel> entries)
	{
		return CollectionUtils.isEmpty(entries) ? 0.0d
				: Double.sum(entries.stream().mapToDouble(AbstractOrderEntryModel::getTotalPrice).sum(), calculateTaxes(entries));
	}

	protected Double getEntriesAmountByEntryType(final List<AbstractOrderEntryModel> entries, final OrderEntryType type)
	{
		return getEntriesAmount(entries.stream().filter(entry -> type.equals(entry.getType())).collect(Collectors.toList()));
	}

	protected List<EntryTypePaymentInfo> createPayInAdvancePaymentInfoForEntryType(final AbstractOrderModel abstractOrder,
			final OrderEntryType entryType)
	{
		final List<AbstractOrderEntryModel> entries = abstractOrder.getEntries().stream()
				.filter(entry -> entryType.equals(entry.getType())).collect(Collectors.toList());
		if (CollectionUtils.isEmpty(entries))
		{
			return Collections.emptyList();
		}
		final boolean isAmendedCart = Objects.nonNull(abstractOrder.getOriginalOrder());
		final EntryTypePaymentInfo paymentInfo = new EntryTypePaymentInfo();
		paymentInfo.setEntryType(entryType);
		paymentInfo.setEntries(entries);
		final Double paymentTotal = isAmendedCart
				? getAmendedTotalToPay(abstractOrder.getEntries(), abstractOrder.getOriginalOrder().getEntries(), entryType)
				: getBookingTimeAmount(entries, abstractOrder);
		paymentInfo.setBookingTimeAmount(getTotalWithDiscounts(abstractOrder, paymentTotal, entries));
		paymentInfo.setPrePaymentRequested(0.0d);
		paymentInfo.setCheckInPayOff(0.0d);
		return Collections.singletonList(paymentInfo);
	}

	/**
	 * @param abstractOrder
	 * @param paymentTotal
	 * @return
	 * @deprecated since version 5.0
	 */
	@Deprecated
	protected Double getTotalWithDiscounts(final AbstractOrderModel abstractOrder,
			final Double paymentTotal)
	{
		BigDecimal discount = BigDecimal.ZERO;
		if (Objects.nonNull(abstractOrder.getOriginalOrder()))
		{
			discount = discount.add(BigDecimal.valueOf(abstractOrder.getTotalDiscounts()))
					.subtract(BigDecimal.valueOf(abstractOrder.getOriginalOrder().getTotalDiscounts()));
			return paymentTotal - discount.doubleValue();
		}
		else
		{
			discount = discount.add(BigDecimal.valueOf(abstractOrder.getTotalDiscounts()));
			return paymentTotal - discount.doubleValue();
		}
	}

	protected Double getTotalWithDiscounts(final AbstractOrderModel abstractOrder,
			final Double paymentTotal, final List<AbstractOrderEntryModel> entries)
	{
		BigDecimal discount = BigDecimal.ZERO;
		if (CollectionUtils.isEmpty(entries))
		{
			return discount.doubleValue();
		}
		final OrderEntryType entryType = entries.get(0).getType();
		final GlobalDiscountByEntryTypeCalculationStrategy strategy = getGlobalDiscountCalculationStrategyMap()
				.getOrDefault(entryType, getGlobalDiscountCalculationStrategyMap().get(OrderEntryType.DEFAULT));
		discount = discount.add(strategy.getGlobalDiscount(abstractOrder, entries));

		if (discount.doubleValue() > 0 && abstractOrder.getNet())
		{
			discount = discount
					.add(getTaxCompensationStrategy().calculateTaxCompensationForEntries(abstractOrder, discount, entries));
		}
		final Double totalWithDiscounts = paymentTotal - discount.doubleValue();
		return totalWithDiscounts;
	}

	protected Double getAmendedTotalToPay(final List<AbstractOrderEntryModel> amendedEntries,
			final List<AbstractOrderEntryModel> originalEntries, final OrderEntryType entryType)
	{
		BigDecimal amendedTotalToPay = BigDecimal.ZERO;
		amendedTotalToPay = amendedTotalToPay
				.add(BigDecimal.valueOf(getEntriesAmountByEntryType(amendedEntries, entryType)))
				.subtract(getBookingService().getOrderTotalPaidForOrderEntryType(amendedEntries.get(0).getOrder(), entryType));
		return amendedTotalToPay.doubleValue();
	}

	/**
	 * @return the travelCartService
	 */
	protected TravelCartService getTravelCartService()
	{
		return travelCartService;
	}

	/**
	 * @param travelCartService
	 * 		the travelCartService to set
	 */
	@Required
	public void setTravelCartService(final TravelCartService travelCartService)
	{
		this.travelCartService = travelCartService;
	}

	/**
	 * @return the taxCompensationStrategy
	 */
	protected TaxCompensationStrategy getTaxCompensationStrategy()
	{
		return taxCompensationStrategy;
	}

	/**
	 * @param taxCompensationStrategy
	 * 		the taxCompensationStrategy to set
	 */
	@Required
	public void setTaxCompensationStrategy(
			final TaxCompensationStrategy taxCompensationStrategy)
	{
		this.taxCompensationStrategy = taxCompensationStrategy;
	}

	/**
	 * @return the globalDiscountCalculationStrategyMap
	 */
	protected Map<OrderEntryType, GlobalDiscountByEntryTypeCalculationStrategy> getGlobalDiscountCalculationStrategyMap()
	{
		return globalDiscountCalculationStrategyMap;
	}

	/**
	 * @param globalDiscountCalculationStrategyMap
	 * 		the globalDiscountCalculationStrategyMap to set
	 */
	@Required
	public void setGlobalDiscountCalculationStrategyMap(
			final Map<OrderEntryType, GlobalDiscountByEntryTypeCalculationStrategy> globalDiscountCalculationStrategyMap)
	{
		this.globalDiscountCalculationStrategyMap = globalDiscountCalculationStrategyMap;
	}

	/**
	 * @return the bookingService
	 */
	protected BookingService getBookingService()
	{
		return bookingService;
	}

	/**
	 * @param bookingService
	 * 		the bookingService to set
	 */
	@Required
	public void setBookingService(final BookingService bookingService)
	{
		this.bookingService = bookingService;
	}
}
