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

package de.hybris.platform.travelfacades.facades.accommodation.manager;

import de.hybris.platform.commercefacades.accommodation.AccommodationAvailabilityRequestData;
import de.hybris.platform.commercefacades.accommodation.AccommodationAvailabilityResponseData;


/**
 * Interface for Accommodation Details Pipeline Manager.
 */
public interface AccommodationDetailsPipelineManager
{
	/**
	 * Execute pipeline accommodation availability response data.
	 *
	 * @param availabilityRequestData
	 * 		the availability request data
	 * @return the accommodation availability response data
	 */
	AccommodationAvailabilityResponseData executePipeline(AccommodationAvailabilityRequestData availabilityRequestData);
}
