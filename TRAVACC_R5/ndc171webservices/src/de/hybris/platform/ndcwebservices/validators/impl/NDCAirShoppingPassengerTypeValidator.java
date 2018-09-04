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

package de.hybris.platform.ndcwebservices.validators.impl;

import de.hybris.platform.ndcfacades.ndc.AirShoppingRQ;
import de.hybris.platform.ndcfacades.ndc.ErrorsType;


/**
 * Concrete class to validate passenger data for {@link AirShoppingRQ}
 */
public class NDCAirShoppingPassengerTypeValidator extends NDCAbstractPassengerTypeValidator<AirShoppingRQ>
{
	@Override
	public void validate(final AirShoppingRQ airShoppingRQ, final ErrorsType errorsType)
	{
		super.validate(airShoppingRQ.getDataLists().getPassengerList().getPassenger(), errorsType);
	}
}
