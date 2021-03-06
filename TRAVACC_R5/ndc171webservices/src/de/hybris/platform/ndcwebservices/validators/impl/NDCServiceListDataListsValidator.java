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

package de.hybris.platform.ndcwebservices.validators.impl;

import de.hybris.platform.ndcfacades.ndc.DataListType;
import de.hybris.platform.ndcfacades.ndc.ErrorsType;
import de.hybris.platform.ndcfacades.ndc.ServiceListRQ;
import de.hybris.platform.ndcwebservices.constants.NdcwebservicesConstants;
import de.hybris.platform.ndcwebservices.validators.NDCRequestValidator;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Required;


/**
 * The Concrete class that validates {@link DataListType} element for {@link ServiceListRQ}
 */
public class NDCServiceListDataListsValidator implements NDCRequestValidator<ServiceListRQ>
{
	private ConfigurationService configurationService;

	@Override
	public void validate(final ServiceListRQ serviceListRQ, final ErrorsType errorsType)
	{
		if (Objects.isNull(serviceListRQ.getDataLists()))
		{
			addError(errorsType,
					getConfigurationService().getConfiguration().getString(NdcwebservicesConstants.DATALISTS_UNAVAILABLE));
			return;
		}

		if (Objects.isNull(serviceListRQ.getDataLists().getPassengerList()))
		{
			addError(errorsType,
					getConfigurationService().getConfiguration().getString(NdcwebservicesConstants.MISSING_DATALISTS_PASSENGERLIST));
			return;
		}
	}

	/**
	 * @return the configurationService
	 */
	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	@Required
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

}
