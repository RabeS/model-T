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

package de.hybris.platform.travelservices.strategies.cart.validation;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.core.model.order.CartEntryModel;


/**
 * Interface for strategies validating cart entries according with their type
 */
public interface CartEntryValidationStrategyByEntryType
{
	/**
	 * Validates a cartEntry with a custom logic according with the entry type
	 *
	 * @param cartEntry
	 * 		the cart entry
	 * @return commerce cart modification
	 */
	CommerceCartModification validate(CartEntryModel cartEntry);
}
