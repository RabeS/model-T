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
package de.hybris.platform.ndcwebservices.validators;

import de.hybris.platform.ndcfacades.ndc.ErrorsType;
import de.hybris.platform.ndcfacades.ndc.OrderViewRS;

import org.apache.commons.collections.CollectionUtils;


/**
 * Validator class for OrderViewRSValidator
 */
public class OrderViewRSValidator extends NCDAbstractRequestValidator
{
	/**
	 * Validate an orderViewRS
	 *
	 * @param orderViewRS
	 * 		the orderViewRS to validate
	 *
	 * @return boolean that indicates the presence of any error
	 */
	public boolean validateOrderViewRS(final OrderViewRS orderViewRS)
	{
		final ErrorsType errorsType = validateNDCRequest(orderViewRS);

		if (CollectionUtils.isNotEmpty(errorsType.getError()))
		{
			orderViewRS.setErrors(errorsType);
			return false;
		}
		return true;
	}
}
