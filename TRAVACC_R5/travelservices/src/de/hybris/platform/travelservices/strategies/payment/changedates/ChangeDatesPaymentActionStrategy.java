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

package de.hybris.platform.travelservices.strategies.payment.changedates;

import de.hybris.platform.travelservices.model.order.AccommodationOrderEntryGroupModel;

import java.util.List;


/**
 * Strategy providing implementation to take action in case of the Payment Type REFUND/SAME/PAYABLE for Change Dates
 */
public interface ChangeDatesPaymentActionStrategy
{
	/**
	 * Link accommodationOrderEntryGroup to new Entries of the amended Order based on the paymentType and the
	 * paymentMethod used during original method
	 *
	 * @param accommodationOrderEntryGroup
	 *           the accommodation order entry group
	 * @param entryNumbers
	 *           the entry numbers
	 * @return true/false
	 */
	boolean takeAction(AccommodationOrderEntryGroupModel accommodationOrderEntryGroup, List<Integer> entryNumbers);
}
