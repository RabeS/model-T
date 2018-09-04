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

package de.hybris.platform.travelbackoffice.utils;

import de.hybris.platform.travelbackoffice.constants.TravelbackofficeConstants;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Intbox;

/**
 * Class which provides Backoffice utility methods.
 */
public class TravelbackofficeUtils {
	
	/**
	 * Method append the constant string if it contains {@code TravelbackofficeConstants.TYPE_REFERENCE} otherwise not.
	 */
	public static String replaceTypeReference(final String stockLevelCode) {
		return new StringBuilder(stockLevelCode).append(TravelbackofficeConstants.TYPE_REFERENCE_CONSTANT).toString();
	}

	/**
	 * Method validates the input string and replace if it contains
	 * {@code TravelbackofficeConstants.TYPE_REFERENCE} otherwise returns input
	 * string
	 * 
	 * @param stockLevelCode
	 */
	public static String validateStockLevelCode(final String stockLevelCode) {
		if (StringUtils.equalsIgnoreCase(stockLevelCode, TravelbackofficeConstants.TYPE_REFERENCE)) {
			return replaceTypeReference(stockLevelCode);
		}
		return stockLevelCode;
	}
	
	/**
	 * Method validates the stock attributes used for adding stock for booking class
	 * @param children
	 */
	public static void validateStockAttributes(final List<Component> children)
	{
		final Integer availableQuantity = ((Intbox) children.get(1)).getValue();
		final Integer oversellingQuantity = ((Intbox) children.get(2)).getValue();

		if (Objects.nonNull(availableQuantity) && availableQuantity <= 0)
		{
			throw new WrongValueException(children.get(1), Labels.getLabel(TravelbackofficeConstants.AVAILABLE_QUANTITY_VALIDATION));
		}

		if (Objects.nonNull(oversellingQuantity) && oversellingQuantity < 0)
		{
			throw new WrongValueException(children.get(2), Labels.getLabel(TravelbackofficeConstants.NEGATIVE_VALUE_NOT_ALLOWED));
		}
	}

}
