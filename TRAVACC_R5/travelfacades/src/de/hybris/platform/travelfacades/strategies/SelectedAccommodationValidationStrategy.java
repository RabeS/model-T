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

package de.hybris.platform.travelfacades.strategies;

import de.hybris.platform.core.model.order.CartModel;


/**
 * The interface Selected accommodation validation strategy.
 */
public interface SelectedAccommodationValidationStrategy
{
	/**
	 * Validate selected accommodation boolean.
	 *
	 * @param cartModel
	 * 		the cart model
	 * @return the boolean
	 */
	boolean validateSelectedAccommodation(CartModel cartModel);
}
