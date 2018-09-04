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
package de.hybris.platform.travelfacades.populators;

import de.hybris.platform.commercefacades.user.converters.populator.CustomerPopulator;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.Collection;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;



/**
 * Converter implementation for {@link de.hybris.platform.core.model.user.UserModel} as source and
 * {@link de.hybris.platform.commercefacades.user.data.CustomerData} as target type.
 */
public class TravelCustomerPopulator extends CustomerPopulator
{
	private Converter<AddressModel, AddressData> addressConverter;

	@Override
	public void populate(final CustomerModel source, final CustomerData target)
	{
		super.populate(source, target);
		if (Objects.nonNull(source.getType()))
		{
			target.setType(source.getType());
		}
		// Home address : there will be only one home address for each customer
		final Collection<AddressModel> customerAddresses = source.getAddresses();
		if (CollectionUtils.isNotEmpty(customerAddresses))
		{
			for (final AddressModel address : customerAddresses)
			{
				if(address.getContactAddress())
				{
					target.setDefaultShippingAddress(getAddressConverter().convert(address));
					break;
				}
			}
		}
	}

	/**
	 * @return the addressConverter
	 */
	protected Converter<AddressModel, AddressData> getAddressConverter()
	{
		return addressConverter;
	}

	/**
	 * @param addressConverter
	 *           the addressConverter to set
	 */
	@Required
	public void setAddressConverter(final Converter<AddressModel, AddressData> addressConverter)
	{
		this.addressConverter = addressConverter;
	}
}
