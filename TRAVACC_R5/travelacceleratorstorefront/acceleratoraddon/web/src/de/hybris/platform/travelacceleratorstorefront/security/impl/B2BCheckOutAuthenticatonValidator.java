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

package de.hybris.platform.travelacceleratorstorefront.security.impl;

import de.hybris.platform.acceleratorstorefrontcommons.checkout.steps.validation.ValidationResults;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.travelacceleratorstorefront.security.B2BUserGroupProvider;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;


/**
 * The type B2b check out authentication validator.
 */
public class B2BCheckOutAuthenticatonValidator
{
	private B2BUserGroupProvider b2bUserGroupProvider;

	public ValidationResults validate(final RedirectAttributes redirectAttributes)
	{
		if (!b2bUserGroupProvider.isCurrentUserAuthorizedToCheckOut())
		{
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
					"checkout.error.invalid.accountType");
			return ValidationResults.FAILED;
		}

		return ValidationResults.SUCCESS;
	}

	/**
	 * @return the b2bUserGroupProvider
	 */
	protected B2BUserGroupProvider getB2bUserGroupProvider()
	{
		return b2bUserGroupProvider;
	}

	/**
	 * @param b2bUserGroupProvider
	 *           the b2bUserGroupProvider to set
	 */
	public void setB2bUserGroupProvider(final B2BUserGroupProvider b2bUserGroupProvider)
	{
		this.b2bUserGroupProvider = b2bUserGroupProvider;
	}

}
