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

package de.hybris.platform.travelrulesengine.converters.populator;

import de.hybris.platform.commercefacades.accommodation.AccommodationCategoryData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.travelrulesengine.rao.AccommodationOfferingRAO;
import de.hybris.platform.travelrulesengine.rao.AccommodationProviderRAO;
import de.hybris.platform.travelservices.enums.AccommodationOfferingType;
import de.hybris.platform.travelservices.model.accommodation.AccommodationProviderModel;
import de.hybris.platform.travelservices.model.order.AccommodationOrderEntryGroupModel;
import de.hybris.platform.travelservices.model.travel.AccommodationCategoryModel;
import de.hybris.platform.travelservices.model.travel.LocationModel;
import de.hybris.platform.travelservices.model.warehouse.AccommodationOfferingModel;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;



/**
 * The type Accommodation offering cart rao populator.
 */
public class AccommodationOfferingCartRaoPopulator implements Populator<AbstractOrderModel, CartRAO>
{
	private Converter<AccommodationCategoryModel, AccommodationCategoryData> accommodationCategoryConverter;

	@Override
	public void populate(final AbstractOrderModel source, final CartRAO target) throws ConversionException
	{
		if (Objects.isNull(source) || CollectionUtils.isEmpty(source.getEntries()))
		{
			return;
		}

		final AccommodationOfferingModel accommodationOfferingModel = source.getEntries().stream()
				.map(AbstractOrderEntryModel::getEntryGroup).filter(AccommodationOrderEntryGroupModel.class::isInstance)
				.map(AccommodationOrderEntryGroupModel.class::cast).map(AccommodationOrderEntryGroupModel::getAccommodationOffering)
				.findFirst().orElse(null);

		final List<AccommodationOrderEntryGroupModel> accommodationOrderEntryGroups = source.getEntries().stream()
				.filter(entry -> entry.getEntryGroup() instanceof AccommodationOrderEntryGroupModel)
				.map(entry -> (AccommodationOrderEntryGroupModel) entry.getEntryGroup()).collect(Collectors.toList());

		populateAccommodationOfferingRao(target, accommodationOfferingModel, accommodationOrderEntryGroups);

	}

	/**
	 * Populate accommodation offering rao.
	 *
	 * @param target
	 * 		the target
	 * @param accommodationOfferingModel
	 * 		the accommodation offering model
	 * @param accommodationOrderEntryGroups
	 * 		the accommodation order entry groups
	 */
	protected void populateAccommodationOfferingRao(final CartRAO target,
			final AccommodationOfferingModel accommodationOfferingModel,
			final List<AccommodationOrderEntryGroupModel> accommodationOrderEntryGroups)
	{
		if (Objects.isNull(accommodationOfferingModel))
		{
			return;
		}

		final AccommodationOfferingRAO accommodationOfferingRAO = new AccommodationOfferingRAO();

		accommodationOfferingRAO.setCode(accommodationOfferingModel.getCode());

		final Set<String> accommodationDestinationLocations = new HashSet<>();
		populateLocationsFromLocation(accommodationOfferingModel.getLocation(), accommodationDestinationLocations);
		accommodationOfferingRAO.setLocations(accommodationDestinationLocations);

		final AccommodationProviderModel provider = accommodationOfferingModel.getProvider();
		accommodationOfferingRAO.setProvider(populateProviderRao(provider));

		final Set<String> types = accommodationOfferingModel.getTypes().stream().map(AccommodationOfferingType::getCode)
				.collect(Collectors.toSet());
		accommodationOfferingRAO.setAccommodationOfferingTypes(types);


		for (final AccommodationOrderEntryGroupModel accommodationOrderEntryGroup : accommodationOrderEntryGroups)
		{
			populateAccommodationCategories(accommodationOrderEntryGroup, accommodationOfferingRAO);
		}

		target.setAccommodationOffering(accommodationOfferingRAO);
	}

	/**
	 * Populate accommodation categories.
	 *
	 * @param accommodationOrderEntryGroupModel
	 * 		the accommodation OrderEntry Group model
	 * @param accommodationOfferingRAO
	 * 		the accommodation offering rao
	 */
	protected void populateAccommodationCategories(final AccommodationOrderEntryGroupModel accommodationOrderEntryGroupModel,
			final AccommodationOfferingRAO accommodationOfferingRAO)
	{
		final Set<AccommodationCategoryData> accommodationCategoryDataSet = new HashSet<>();
		accommodationOrderEntryGroupModel.getAccommodation().getSupercategories().stream()
				.filter(AccommodationCategoryModel.class::isInstance).forEach(
				superCategory -> populateCategoriesFromCategory(AccommodationCategoryModel.class.cast(superCategory),
						accommodationCategoryDataSet));

		if (CollectionUtils.isEmpty(accommodationCategoryDataSet))
		{
			return;
		}
		final Set<String> categoryCodeList = accommodationCategoryDataSet.stream()
				.map(accommodationCategoryData -> accommodationCategoryData.getCode()).collect(Collectors.toSet());

		if (Objects.isNull(accommodationOfferingRAO.getAccommodationCategories()))
		{
			accommodationOfferingRAO.setAccommodationCategories(categoryCodeList);
		}
		else
		{
			accommodationOfferingRAO.getAccommodationCategories().addAll(categoryCodeList);
		}

	}


	/**
	 * Populate categories from category.
	 *
	 * @param categoryModel
	 * 		the category model
	 * @param categoryDataSet
	 * 		the category data set
	 */
	protected void populateCategoriesFromCategory(final AccommodationCategoryModel categoryModel,
			final Set<AccommodationCategoryData> categoryDataSet)
	{
		if (Objects.isNull(categoryModel))
		{
			return;
		}
		categoryDataSet.add(getAccommodationCategoryConverter().convert(categoryModel));
		categoryModel.getSupercategories().stream().filter(AccommodationCategoryModel.class::isInstance).forEach(
				superCategory -> populateCategoriesFromCategory(AccommodationCategoryModel.class.cast(superCategory),
						categoryDataSet));
	}

	/**
	 * Populate provider rao accommodation provider rao.
	 *
	 * @param provider
	 * 		the provider
	 * @return the accommodation provider rao
	 */
	protected AccommodationProviderRAO populateProviderRao(final AccommodationProviderModel provider)
	{
		if (Objects.isNull(provider))
		{
			return null;
		}

		final AccommodationProviderRAO accommodationProviderRAO = new AccommodationProviderRAO();
		accommodationProviderRAO.setCode(provider.getCode());

		return accommodationProviderRAO;
	}

	/**
	 * Populate locations from location.
	 *
	 * @param location
	 * 		the location
	 * @param locationSet
	 * 		the location set
	 */
	protected void populateLocationsFromLocation(final LocationModel location, final Set<String> locationSet)
	{
		if (Objects.isNull(location))
		{
			return;
		}
		locationSet.add(location.getCode());
		location.getSuperlocations().forEach(superLocation -> populateLocationsFromLocation(superLocation, locationSet));
	}

	/**
	 * Gets accommodation category converter.
	 *
	 * @return the accommodation category converter
	 */
	protected Converter<AccommodationCategoryModel, AccommodationCategoryData> getAccommodationCategoryConverter()
	{
		return accommodationCategoryConverter;
	}

	/**
	 * Sets accommodation category converter.
	 *
	 * @param accommodationCategoryConverter
	 * 		the accommodation category converter
	 */
	@Required
	public void setAccommodationCategoryConverter(
			final Converter<AccommodationCategoryModel, AccommodationCategoryData> accommodationCategoryConverter)
	{
		this.accommodationCategoryConverter = accommodationCategoryConverter;
	}
}
