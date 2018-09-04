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

package de.hybris.platform.travelfacades.facades.impl;

import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.configurablebundlefacades.order.impl.DefaultBundleCommerceCartFacade;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.travelfacades.facades.TravelBundleCommerceCartFacade;
import de.hybris.platform.travelservices.enums.OrderEntryType;

import java.util.Collections;
import java.util.HashSet;
import javax.annotation.Nonnull;


/**
 * Concrete implementation of {@link TravelBundleCommerceCartFacade}
 */
public class DefaultTravelBundleCommerceCartFacade extends DefaultBundleCommerceCartFacade implements
		TravelBundleCommerceCartFacade
{
	@Override
	public CartModificationData startBundle(@Nonnull final String bundleTemplateId, @Nonnull final String productCode,
			final long quantity,
			final OrderEntryType entryType) throws CommerceCartModificationException
	{
		final CommerceCartParameter parameter = new CommerceCartParameter();
		parameter.setEnableHooks(true);
		parameter.setCart(getCartService().getSessionCart());
		parameter.setEntryGroupNumbers(Collections.emptySet());
		parameter.setBundleTemplate(getBundleTemplateService().getBundleTemplateForCode(bundleTemplateId));
		parameter.setProduct(getProductService().getProductForCode(productCode));
		parameter.setQuantity(quantity);
		parameter.setOrderEntryType(entryType);

		return getCartModificationConverter().convert(getCommerceCartService().addToCart(parameter));
	}

	@Override
	public CartModificationData addToCart(@Nonnull final String productCode, final long quantity, final int groupNumber)
			throws CommerceCartModificationException
	{
		final CartModel cartModel = getCartService().getSessionCart();
		final CommerceCartParameter parameter = new CommerceCartParameter();
		parameter.setEnableHooks(true);
		parameter.setCart(cartModel);
		parameter.setEntryGroupNumbers(new HashSet<>(Collections.singletonList(Integer.valueOf(groupNumber))));
		parameter.setBundleTemplate(null);
		parameter.setProduct(getProductService().getProductForCode(productCode));
		parameter.setQuantity(quantity);
		parameter.setOrderEntryType(
				cartModel.getEntries().stream().filter(entry -> entry.getEntryGroupNumbers().contains(groupNumber)).findAny().get()
						.getType());

		return getCartModificationConverter().convert(getCommerceCartService().addToCart(parameter));
	}
}
