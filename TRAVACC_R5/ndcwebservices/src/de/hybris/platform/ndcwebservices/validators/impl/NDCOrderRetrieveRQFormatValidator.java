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

import de.hybris.platform.ndcfacades.ndc.ErrorsType;
import de.hybris.platform.ndcfacades.ndc.OrderRetrieveRQ;

/**
 * NDC Order Retrieve RQ Format Validator validate an OrderRetrieveRQ based on the constraints defined in its xsd
 * @deprecated since version 4.0 use {@link NDCRQFormatValidator}
 */
@Deprecated
public class NDCOrderRetrieveRQFormatValidator extends NDCRQFormatValidator<OrderRetrieveRQ>
{
	@Override
	public void validate(final OrderRetrieveRQ orderRetrieveRQ, final ErrorsType errorsType)
	{
		super.validate(orderRetrieveRQ, errorsType);
	}
}
