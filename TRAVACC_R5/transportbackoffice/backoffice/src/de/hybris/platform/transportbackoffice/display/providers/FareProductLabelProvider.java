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

package de.hybris.platform.transportbackoffice.display.providers;

import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.travelservices.model.product.FareProductModel;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import com.hybris.cockpitng.labels.LabelProvider;


/**
 * Label Provider for the FareProductModel.
 */
public class FareProductLabelProvider implements LabelProvider<FareProductModel>
{
	private CommonI18NService commonI18NService;

	@Override
	public String getLabel(final FareProductModel fareProductModel)
	{
		if (Objects.isNull(fareProductModel) || StringUtils.isEmpty(fareProductModel.getCode())
				|| CollectionUtils.isEmpty(fareProductModel.getEurope1Prices()))
		{
			return null;
		}

		final String currentCurrency = getCommonI18NService().getBaseCurrency().getSymbol();

		final List<PriceRowModel> priceList = fareProductModel.getEurope1Prices().stream()
				.filter(priceRowModel -> priceRowModel.getCurrency().getSymbol().equals(currentCurrency))
				.sorted(new Comparator<PriceRowModel>()
				{
					@Override
					public int compare(final PriceRowModel o1, final PriceRowModel o2)
					{
						return o1.getPrice().compareTo(o2.getPrice());
					}
				}).collect(Collectors.toList());

		return CollectionUtils.isEmpty(priceList) ? fareProductModel.getCode()
				: fareProductModel.getBookingClass() + " - " + fareProductModel.getCode() + " - " + currentCurrency
						+ priceList.get(0).getPrice() + " - " + currentCurrency
						+ priceList.get(CollectionUtils.size(priceList) - 1).getPrice();
	}

	/**
	 * @return the commonI18NService
	 */
	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	/**
	 * @param commonI18NService
	 *           the commonI18NService to set
	 */
	@Required
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	@Override
	public String getDescription(final FareProductModel object)
	{
		return null;
	}

	@Override
	public String getIconPath(final FareProductModel object)
	{
		return null;
	}

}
