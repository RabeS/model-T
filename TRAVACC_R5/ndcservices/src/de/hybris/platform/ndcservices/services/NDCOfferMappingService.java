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

import de.hybris.platform.ndcservices.model.NDCOfferMappingModel;


/**
 * Interface that exposes {@link NDCOfferMappingModel} through his code or ndcOfferItemID
 */
public interface NDCOfferMappingService
{
	/**
	 * Retrieves NDCOfferMappingModel based on the ndcOfferItemID provided
	 *
	 * @param code that identifies the offer
	 * @return NDCOfferMappingModel with the mapping between the ndcOfferItemID and its code
	 */
	NDCOfferMappingModel getNDCOfferMappingFromCode(String code);

	/**
	 * Retrieves NDCOfferMappingModel based on the ndcOfferItemID provided
	 *
	 * @param ndcOfferItemID that identifies the offer
	 * @return NDCOfferMappingModel with the mapping between the ndcOfferItemID and its code
	 */
	NDCOfferMappingModel getNDCOfferMappingFromOfferItemID(String ndcOfferItemID);
}
