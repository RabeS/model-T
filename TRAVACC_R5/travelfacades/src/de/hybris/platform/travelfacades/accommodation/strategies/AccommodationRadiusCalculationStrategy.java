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

package de.hybris.platform.travelfacades.accommodation.strategies;

import java.util.List;


/**
 * Strategy to calculate the maximum radius based on Place Types.
 */
public interface AccommodationRadiusCalculationStrategy
{
	/**
	 * Method to calculate the maximum radius based on Place Type.
	 *
	 * @param placeType
	 * 		the place type
	 * @return radius double
	 */
	Double calculateRadius(List<String> placeType);

}
