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

package de.hybris.platform.travelservices.strategies.stock;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.stock.exception.InsufficientStockLevelException;


/**
 * Strategy to reserve and release the stockLevel for an abstractOrderEntry
 */
public interface TravelManageStockStrategy
{

	/**
	 * Reserves products in the specified warehouse for a given abstractOrderEntry.
	 *
	 * @param abstractOrderEntry
	 * 		the abstract order entry
	 * @throws InsufficientStockLevelException
	 * 		the insufficient stock level exception
	 */
	void reserve(AbstractOrderEntryModel abstractOrderEntry) throws InsufficientStockLevelException;

	/**
	 * Release products for a given abstractOrderEntry.
	 *
	 * @param abstractOrderEntry
	 * 		the abstract order entry
	 */
	void release(AbstractOrderEntryModel abstractOrderEntry);

}
