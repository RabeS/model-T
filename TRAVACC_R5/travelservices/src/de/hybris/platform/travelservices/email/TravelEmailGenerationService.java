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

package de.hybris.platform.travelservices.email;


import de.hybris.platform.acceleratorservices.email.EmailGenerationService;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.model.email.EmailMessageModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;

import java.util.List;

/**
 * The interface Travel email generation service.
 */
public interface TravelEmailGenerationService extends EmailGenerationService
{
	/**
	 * Generate list of email messages.
	 *
	 * @param businessProcessModel
	 * 		the business process model
	 * @param emailPageModel
	 * 		the email page model
	 * @return the list
	 */
	List<EmailMessageModel> generateEmails(BusinessProcessModel businessProcessModel, EmailPageModel emailPageModel);
}
