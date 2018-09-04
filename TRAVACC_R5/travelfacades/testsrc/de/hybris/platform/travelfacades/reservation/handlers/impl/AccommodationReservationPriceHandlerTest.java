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

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.accommodation.AccommodationReservationData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.travelfacades.facades.TravelCommercePriceFacade;
import de.hybris.platform.travelfacades.order.TravelCartFacade;
import de.hybris.platform.travelservices.enums.OrderEntryType;
import de.hybris.platform.travelservices.strategies.payment.OrderTotalPaidForOrderEntryTypeCalculationStrategy;
import de.hybris.platform.travelservices.strategies.payment.TaxCompensationStrategy;
import de.hybris.platform.travelservices.strategies.payment.impl.AccommodationGlobalDiscountCalculationStrategy;
import de.hybris.platform.util.TaxValue;

import java.math.BigDecimal;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


/**
 * unit test for Accommodation reservation price handler.
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class AccommodationReservationPriceHandlerTest
{
	@InjectMocks
	AccommodationReservationPriceHandler handler;

	@Mock
	private AbstractOrderModel orderModel;
	@Mock
	private AbstractOrderEntryModel orderEntry;
	@Mock
	private OrderTotalPaidForOrderEntryTypeCalculationStrategy orderTotalPaidForOrderEntryTypeCalculationStrategy;
	@Mock
	private TravelCommercePriceFacade travelCommercePriceFacade;
	@Mock
	private TravelCartFacade travelCartFacade;
	@Mock
	private TaxCompensationStrategy taxCompensationStrategy;
	@Mock
	private AccommodationGlobalDiscountCalculationStrategy accommodationGlobalDiscountCalculationStrategy;

	@Before
	public void setUp()
	{
		final CurrencyModel currencyModel = new CurrencyModel();
		currencyModel.setIsocode("GBP");
		given(orderModel.getCurrency()).willReturn(currencyModel);
	}

	@Test
	public void testPopulateWithoutTaxes()
	{
		given(orderModel.getEntries()).willReturn(Stream.of(orderEntry).collect(Collectors.toList()));
		given(orderEntry.getType()).willReturn(OrderEntryType.ACCOMMODATION);
		given(orderEntry.getActive()).willReturn(true);
		given(orderEntry.getTotalPrice()).willReturn(Double.valueOf(30));
		given(orderModel.getNet()).willReturn(true);
		given(orderEntry.getOrder()).willReturn(orderModel);
		given(travelCartFacade.isNetPrice(orderEntry)).willReturn(true);
		given(taxCompensationStrategy
				.calculateTaxCompensationForEntries(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyList()))
				.willReturn(BigDecimal.valueOf(10));
		given(accommodationGlobalDiscountCalculationStrategy.getGlobalDiscount(Matchers.anyObject(), Matchers.anyObject()))
				.willReturn(BigDecimal.ZERO);
		final PriceData priceData1 = new PriceData();
		priceData1.setValue(BigDecimal.valueOf(Double.valueOf(30)));
		when(travelCommercePriceFacade.createPriceData(PriceDataType.BUY, BigDecimal.valueOf(30d), "GBP")).thenReturn(priceData1);
		final PriceData priceData2 = new PriceData();
		priceData2.setValue(BigDecimal.valueOf(Double.valueOf(20)));
		final PriceData discount = new PriceData();
		discount.setValue(BigDecimal.valueOf(Double.valueOf(0)));
		final PriceData tax = new PriceData();
		tax.setValue(BigDecimal.valueOf(Double.valueOf(2)));
		final PriceData actualPriceData = new PriceData();
		actualPriceData.setValue(BigDecimal.valueOf(Double.valueOf(10)));
		final PriceData totalToPay = new PriceData();
		totalToPay.setValue(BigDecimal.valueOf(Double.valueOf(20)));
		given(travelCommercePriceFacade.createPriceData(0, 2)).willReturn(discount);
		given(travelCommercePriceFacade.createPriceData(orderModel.getTotalTax(), 2)).willReturn(tax);
		given(travelCommercePriceFacade.createPriceData(PriceDataType.BUY, BigDecimal.valueOf(10d), "GBP"))
				.willReturn(actualPriceData);
		given(travelCommercePriceFacade.createPriceData(PriceDataType.BUY, BigDecimal.valueOf(0d), "GBP")).willReturn(totalToPay);
		when(travelCommercePriceFacade.createPriceData(PriceDataType.BUY, BigDecimal.valueOf(20d), "GBP")).thenReturn(priceData2);
		when(orderTotalPaidForOrderEntryTypeCalculationStrategy.calculate(orderModel, OrderEntryType.ACCOMMODATION))
				.thenReturn(BigDecimal.valueOf(Double.valueOf(10)));
		when(travelCartFacade.getGlobalDiscountForEntryType(Matchers.anyObject(), Matchers.anyObject()))
				.thenReturn(BigDecimal.valueOf(10));
		final AccommodationReservationData reservationData = new AccommodationReservationData();
		handler.handle(orderModel, reservationData);
		Assert.assertEquals(BigDecimal.valueOf(20d), reservationData.getTotalToPay().getValue());

	}

	@Test
	public void testPopulateWithTaxes()
	{
		given(orderModel.getEntries()).willReturn(Stream.of(orderEntry).collect(Collectors.toList()));
		given(orderEntry.getType()).willReturn(OrderEntryType.ACCOMMODATION);
		given(orderEntry.getActive()).willReturn(true);
		given(orderEntry.getTotalPrice()).willReturn(Double.valueOf(30));
		given(orderModel.getNet()).willReturn(true);
		given(orderEntry.getOrder()).willReturn(orderModel);
		given(travelCartFacade.isNetPrice(orderEntry)).willReturn(true);
		given(taxCompensationStrategy
				.calculateTaxCompensationForEntries(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyList()))
				.willReturn(BigDecimal.valueOf(10));
		given(accommodationGlobalDiscountCalculationStrategy.getGlobalDiscount(Matchers.anyObject(), Matchers.anyObject()))
				.willReturn(BigDecimal.ZERO);
		final TaxValue tax = new TaxValue("APD", 5d, false, 5d, "GB");
		given(orderEntry.getTaxValues()).willReturn(Stream.of(tax).collect(Collectors.toList()));
		final PriceData priceData1 = new PriceData();
		priceData1.setValue(BigDecimal.valueOf(Double.valueOf(35)));
		when(travelCommercePriceFacade.createPriceData(PriceDataType.BUY, BigDecimal.valueOf(35d), "GBP")).thenReturn(priceData1);
		final PriceData priceData2 = new PriceData();
		priceData2.setValue(BigDecimal.valueOf(Double.valueOf(25)));
		final PriceData priceData3 = new PriceData();
		priceData3.setValue(BigDecimal.valueOf(Double.valueOf(5)));
		when(travelCommercePriceFacade.createPriceData(5d, 2)).thenReturn(priceData3);
		final PriceData discount = new PriceData();
		discount.setValue(BigDecimal.valueOf(Double.valueOf(0)));
		final PriceData actualPriceData = new PriceData();
		actualPriceData.setValue(BigDecimal.valueOf(Double.valueOf(20)));
		final PriceData totalToPay = new PriceData();
		totalToPay.setValue(BigDecimal.valueOf(Double.valueOf(20)));
		given(travelCommercePriceFacade.createPriceData(0, 2)).willReturn(discount);
		given(travelCommercePriceFacade.createPriceData(PriceDataType.BUY, BigDecimal.valueOf(15d), "GBP"))
				.willReturn(actualPriceData);
		given(travelCommercePriceFacade.createPriceData(PriceDataType.BUY, BigDecimal.valueOf(10d), "GBP")).willReturn(totalToPay);
		when(travelCommercePriceFacade.createPriceData(PriceDataType.BUY, BigDecimal.valueOf(25d), "GBP")).thenReturn(priceData2);
		when(orderTotalPaidForOrderEntryTypeCalculationStrategy.calculate(orderModel, OrderEntryType.ACCOMMODATION))
				.thenReturn(BigDecimal.valueOf(Double.valueOf(10)));
		when(travelCartFacade.getGlobalDiscountForEntryType(Matchers.anyObject(), Matchers.anyObject()))
				.thenReturn(BigDecimal.valueOf(10));
		final AccommodationReservationData reservationData = new AccommodationReservationData();
		handler.handle(orderModel, reservationData);
		Assert.assertEquals(BigDecimal.valueOf(20d), reservationData.getTotalToPay().getValue());

	}

}
