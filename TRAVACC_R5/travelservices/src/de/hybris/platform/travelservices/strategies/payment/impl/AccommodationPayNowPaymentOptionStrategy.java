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

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.travelservices.enums.OrderEntryType;
import de.hybris.platform.travelservices.model.AbstractOrderEntryGroupModel;
import de.hybris.platform.travelservices.model.order.AccommodationOrderEntryGroupModel;
import de.hybris.platform.travelservices.order.data.EntryTypePaymentInfo;
import de.hybris.platform.travelservices.order.data.PaymentOptionInfo;
import de.hybris.platform.travelservices.strategies.payment.PaymentOptionCreationStrategy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;


/**
 * The type Accommodation pay now payment option strategy.
 */
public class AccommodationPayNowPaymentOptionStrategy extends AbstractPaymentInfoStrategy implements PaymentOptionCreationStrategy
{

	@Override
	public PaymentOptionInfo create(final AbstractOrderModel abstractOrder)
	{
		final PaymentOptionInfo paymentOption = new PaymentOptionInfo();
		paymentOption.setEntryTypeInfos(getPaymentInfos(abstractOrder));

		return paymentOption;
	}

	protected List<EntryTypePaymentInfo> getPaymentInfos(final AbstractOrderModel abstractOrder)
	{
		final List<AccommodationOrderEntryGroupModel> entryGroups = getBookingService()
				.getAccommodationOrderEntryGroups(abstractOrder);
		if (CollectionUtils.isEmpty(entryGroups))
		{
			return Collections.emptyList();
		}

		final List<EntryTypePaymentInfo> paymentInfos = new ArrayList<>();
		entryGroups.forEach(group -> {
			final EntryTypePaymentInfo paymentInfo = new EntryTypePaymentInfo();
			paymentInfo.setEntryType(OrderEntryType.ACCOMMODATION);
			paymentInfo.setBookingTimeAmount(
					getTotalWithDiscounts(abstractOrder, getTotalAmountToPay(abstractOrder, group), group.getEntries()));
			paymentInfo.setPrePaymentRequested(0.0d);
			paymentInfo.setCheckInPayOff(0.0d);
			paymentInfo.setEntries(group.getEntries());
			paymentInfos.add(paymentInfo);
		});
		return paymentInfos;
	}

	protected Double getTotalAmountToPay(final AbstractOrderModel abstractOrder, final AbstractOrderEntryGroupModel entryGroup)
	{
		final BigDecimal totalToPay = BigDecimal.valueOf(getEntriesAmount(entryGroup.getEntries()));
		return totalToPay.subtract(getBookingService().getOrderTotalPaidByEntryGroup(abstractOrder.getOriginalOrder(), entryGroup))
				.doubleValue();
	}

}
