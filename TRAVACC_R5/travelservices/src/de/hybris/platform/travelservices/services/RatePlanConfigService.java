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

package de.hybris.platform.travelservices.services;

import de.hybris.platform.travelservices.model.accommodation.RatePlanConfigModel;


/**
 * RatePlanConfig Service interface which provides functionality to manage Rate Plan Config.
 */
public interface RatePlanConfigService
{
	/**
	 * Returns the RatePlanConfigModel for a given code
	 *
	 * @param ratePlanConfigCode
	 * 		the code of the ratePlanConfig to get
	 * @return the RatePlanConfigModel corresponding to the given code
	 */
	RatePlanConfigModel getRatePlanConfigForCode(String ratePlanConfigCode);
}
