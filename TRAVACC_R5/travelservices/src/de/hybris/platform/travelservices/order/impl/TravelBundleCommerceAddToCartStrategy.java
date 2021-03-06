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

package de.hybris.platform.travelservices.order.impl;

import de.hybris.platform.commerceservices.order.impl.BundleCommerceAddToCartStrategy;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.travelservices.enums.OrderEntryType;

import org.springframework.beans.factory.annotation.Required;


/**
 * The type Travel bundle commerce add to cart strategy.
 */
public class TravelBundleCommerceAddToCartStrategy extends BundleCommerceAddToCartStrategy
{
	private SessionService sessionService;
	private CalculationService calculationService;

	@Override
	public CommerceCartModification addToCart(final CommerceCartParameter parameter) throws CommerceCartModificationException
	{
		final CommerceCartModification modification = doAddToCart(parameter);
		getCommerceCartCalculationStrategy().calculateCart(parameter);
		afterAddToCart(parameter, modification);
		return modification;
	}

	/**
	 * Creates and adds a new cart entry, setting proper {@link OrderEntryType} against it
	 * @param parameter
	 * @param actualAllowedQuantityChange
	 * @return
	 * @throws CommerceCartModificationException
	 */
	@Override
	protected CartEntryModel addCartEntry(final CommerceCartParameter parameter, final long actualAllowedQuantityChange)
			throws CommerceCartModificationException
	{
		if (parameter.getUnit() == null)
		{
			parameter.setUnit(getUnit(parameter));
		}

		final CartEntryModel cartEntryModel = getCartService().addNewEntry(parameter.getCart(), parameter.getProduct(),
				actualAllowedQuantityChange, parameter.getUnit(), APPEND_AS_LAST, false);
		cartEntryModel.setDeliveryPointOfService(parameter.getPointOfService());
		cartEntryModel.setType(parameter.getOrderEntryType());

		return cartEntryModel;
	}

	/**
	 * Gets session service.
	 *
	 * @return the session service
	 */
	protected SessionService getSessionService()
	{
		return sessionService;
	}

	/**
	 * Sets session service.
	 *
	 * @param sessionService
	 * 		the session service
	 */
	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	/**
	 * Gets calculation service.
	 *
	 * @return the calculation service
	 */
	protected CalculationService getCalculationService()
	{
		return calculationService;
	}

	/**
	 * Sets calculation service.
	 *
	 * @param calculationService
	 * 		the calculation service
	 */
	@Required
	public void setCalculationService(final CalculationService calculationService)
	{
		this.calculationService = calculationService;
	}
}
