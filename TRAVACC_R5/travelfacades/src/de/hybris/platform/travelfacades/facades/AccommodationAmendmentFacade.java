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

package de.hybris.platform.travelfacades.facades;

/**
 * Interface to be used during accommodation amendment
 */
public interface AccommodationAmendmentFacade
{

	/**
	 * Creates a cart from order and attaches it to the session to start add accommodation amendment process
	 *
	 * @param orderCode
	 *           the order code
	 * @return boolean
	 */
	Boolean startAmendment(String orderCode);

}
