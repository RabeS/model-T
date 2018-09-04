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

package de.hybris.platform.ndcfacades.baggagelist;

import de.hybris.platform.ndcfacades.ndc.BaggageListRQ;
import de.hybris.platform.ndcfacades.ndc.BaggageListRS;


/**
 * Interface for the Baggage List Facade
 */
public interface BaggageListFacade
{

	/**
	 * Gets baggage list.
	 *
	 * @param baggageListRQ
	 * 		the baggage list rq
	 *
	 * @return baggage list
	 */
	BaggageListRS getBaggageList(BaggageListRQ baggageListRQ);

}
