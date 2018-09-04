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
import de.hybris.platform.ndcfacades.ndc.OrderCreateRQ;


/**
 * NDC OrderCreateRQ Format Validator validate an OrderCreateRQ based on the constraints defined in its xsd
 * @deprecated since version 4.0 use {@link NDCRQFormatValidator}
 */
@Deprecated
public class NDCOrderCreateRQFormatValidator extends NDCRQFormatValidator<OrderCreateRQ>
{
	@Override
	public void validate(final OrderCreateRQ orderCreateRQ, final ErrorsType errorsType)
	{
		super.validate(orderCreateRQ, errorsType);
	}
}
