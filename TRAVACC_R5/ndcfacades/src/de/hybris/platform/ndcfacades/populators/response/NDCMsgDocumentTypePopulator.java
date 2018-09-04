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

package de.hybris.platform.ndcfacades.populators.response;

import de.hybris.platform.ndcfacades.constants.NdcfacadesConstants;
import de.hybris.platform.ndcfacades.ndc.MsgDocumentType;


/**
 * An abstract class for NDC Msg Document Type Populator
 */
public abstract class NDCMsgDocumentTypePopulator
{

	/**
	 * It returns the instance of MsgDocumentType
	 *
	 * @return the msg document type
	 */
	protected MsgDocumentType getMsgDocumentType()
	{
		final MsgDocumentType msgDocumentType = new MsgDocumentType();
		msgDocumentType.setReferenceVersion(NdcfacadesConstants.NDC_REFERENCE_VERSION);
		msgDocumentType.setName(NdcfacadesConstants.NDC_MESSAGE_NAME);
		return msgDocumentType;
	}

}
