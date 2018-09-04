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

import de.hybris.platform.ndcfacades.ndc.BaggageChargesRS;
import de.hybris.platform.ndcfacades.ndc.ErrorsType;

import org.apache.commons.collections.CollectionUtils;


/**
 * Implementation for {@link NCDAbstractRequestValidator} that validates {@link BaggageChargesRS}
 */
public class BaggageChargesRSValidator extends NCDAbstractRequestValidator<BaggageChargesRS>
{
	/**
	 * Validate an BaggageChargesRS
	 *
	 * @param baggageChargesRS
	 * 		the baggageChargesRS to validate
	 *
	 * @return boolean that indicates the presence of any error
	 */
	public boolean validateBaggageChargesRS(final BaggageChargesRS baggageChargesRS)
	{
		final ErrorsType errorsType = validateNDCRequest(baggageChargesRS);

		if (CollectionUtils.isNotEmpty(errorsType.getError()))
		{
			baggageChargesRS.setErrors(errorsType);
			return false;
		}
		return true;
	}
}
