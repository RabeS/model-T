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

import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.configurablebundlefacades.order.BundleCartFacade;
import de.hybris.platform.travelservices.enums.OrderEntryType;

import javax.annotation.Nonnull;


/**
 * Facade to perform cart operations on bundles
 */
public interface TravelBundleCommerceCartFacade extends BundleCartFacade
{
	/**
	 * Starts new bundle in cart based on the given bundle template and add the product to it. Assigns an {@link OrderEntryType} to every
	 * entry created during the process.
	 *
	 * @param bundleTemplateId
	 * 		a component to add the product to. He whole bundle structure - starting from the root of the component - will be added to
	 * 		cart groups
	 * @param productCode
	 * 		a product which will be added to the component
	 * @param quantity
	 * 		quantity for the product. Is limited by selection criteria of the component
	 * @param entryType
	 * 		order entry type to be set against each created entry
	 * @return information about the new cart entry
	 * @throws CommerceCartModificationException
	 * 		if the operation is not possible
	 * @see de.hybris.platform.core.order.EntryGroup
	 * @see de.hybris.platform.configurablebundleservices.jalo.BundleSelectionCriteria
	 */
	CartModificationData startBundle(@Nonnull String bundleTemplateId, @Nonnull String productCode, long quantity,
			OrderEntryType entryType)
			throws CommerceCartModificationException;
}
