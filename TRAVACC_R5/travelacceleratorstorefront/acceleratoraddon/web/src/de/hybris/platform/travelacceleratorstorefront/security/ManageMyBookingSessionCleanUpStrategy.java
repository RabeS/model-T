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

package de.hybris.platform.travelacceleratorstorefront.security;

import javax.servlet.http.HttpServletRequest;


/**
 * Strategy to remove ManageMyBooking session attributes if the URL of the page does not belong to ManageMyBooking flow.
 */
public interface ManageMyBookingSessionCleanUpStrategy
{

	/**
	 * Removes manageMyBooking sessionAttributes if the URL of the page being accessed does not matches with
	 * ManageMyBooking URL Pattern.
	 * 
	 * @param request
	 *           HttpServletRequest object.
	 */
	void manageMyBookingCleanUp(HttpServletRequest request);

}
