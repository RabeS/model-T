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
package de.hybris.platform.ndcservices.services;

import de.hybris.platform.ndcservices.model.NDCCreditCardTypeMappingModel;


/**
 * Interface that exposes NDCCreditCardTypeMappingModel through his cardTypeCode
 */
public interface NDCCreditCardTypeMappingService
{
	/**
	 * Retrieves NDCCreditCardTypeMappingModel based on the cardTypeCode provided
	 *
	 * @param cardTypeCode that identifies the CreditCardType
	 * @return NDCCreditCardTypeMappingModel for the specified cardTypeCode
	 */
	NDCCreditCardTypeMappingModel getNDCCreditCardTypeMapping(String cardTypeCode);
}
