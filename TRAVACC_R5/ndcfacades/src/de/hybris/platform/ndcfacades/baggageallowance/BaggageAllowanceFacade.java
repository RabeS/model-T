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

package de.hybris.platform.ndcfacades.baggageallowance;

import de.hybris.platform.ndcfacades.ndc.BaggageAllowanceRQ;
import de.hybris.platform.ndcfacades.ndc.BaggageAllowanceRS;


/**
 * Interface for the Baggage Allowance Facade
 */
public interface BaggageAllowanceFacade
{
	/**
	 * This method returns an instance of {@link BaggageAllowanceRS}, it provides checked and carry-on baggage allowance
	 * details
	 *
	 * @param baggageAllowanceRQ
	 * 		the baggage allowance rq
	 *
	 * @return the baggage allowance
	 */
	BaggageAllowanceRS getBaggageAllowance(BaggageAllowanceRQ baggageAllowanceRQ);
}
