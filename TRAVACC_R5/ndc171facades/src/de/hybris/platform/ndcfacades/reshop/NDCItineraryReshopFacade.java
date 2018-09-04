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
package de.hybris.platform.ndcfacades.reshop;

import de.hybris.platform.ndcfacades.ndc.ItinReshopRQ;
import de.hybris.platform.ndcfacades.ndc.ItinReshopRS;


/**
 * Interface for the Itinerary Reshop Facade
 */
public interface NDCItineraryReshopFacade
{

	/**
	 * Generate an {@link ItinReshopRS} based on the information provided in the {@link ItinReshopRQ}
	 *
	 * @param itinReshopRQ
	 * 		the itin reshop rq
	 *
	 * @return itin reshop rs
	 */
	ItinReshopRS reshop(ItinReshopRQ itinReshopRQ);
}
