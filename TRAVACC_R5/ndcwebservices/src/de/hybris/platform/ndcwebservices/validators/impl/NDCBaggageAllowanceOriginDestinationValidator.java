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

import de.hybris.platform.ndcfacades.ndc.BaggageAllowanceRQ;
import de.hybris.platform.ndcfacades.ndc.BaggageAllowanceRQ.Query.OriginDestination;
import de.hybris.platform.ndcfacades.ndc.ErrorsType;
import de.hybris.platform.ndcwebservices.constants.NdcwebservicesConstants;

import java.util.List;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;


/**
 * The Concrete class that validates origin destination for {@link BaggageAllowanceRQ}
 */
public class NDCBaggageAllowanceOriginDestinationValidator extends NDCOriginDestinationValidator<BaggageAllowanceRQ>
{
	@Override
	public void validate(final BaggageAllowanceRQ baggageAllowanceRQ, final ErrorsType errorsType)
	{
		final List<OriginDestination> originDestinations = baggageAllowanceRQ.getQuery().getOriginDestination();
		if (CollectionUtils.isNotEmpty(originDestinations))
		{
			if (!validateFlightNumber(originDestinations.size(), errorsType))
			{
				return;
			}
			validateODKey(originDestinations, errorsType);
		}
	}

	/**
	 * This method validates OriginDestinationKey for every {@link OriginDestination}, add error if it is not present.
	 *
	 * @param originDestinations
	 * 		the origin destinations
	 * @param errorsType
	 * 		the errors type
	 *
	 * @return the boolean
	 */
	protected boolean validateODKey(final List<OriginDestination> originDestinations, final ErrorsType errorsType)
	{
		if (originDestinations.stream().anyMatch(originDestination -> Objects.isNull(originDestination.getOriginDestinationKey())))
		{
			addError(errorsType,
					getConfigurationService().getConfiguration().getString(NdcwebservicesConstants.MISSING_ORIGIN_DESTINATION_KEY));
			return false;
		}
		return true;
	}
}
