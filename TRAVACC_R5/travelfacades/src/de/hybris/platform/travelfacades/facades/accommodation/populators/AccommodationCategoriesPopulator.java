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

package de.hybris.platform.travelfacades.facades.accommodation.populators;

import de.hybris.platform.commercefacades.accommodation.AccommodationCategoryData;
import de.hybris.platform.commercefacades.accommodation.RoomTypeData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.travelservices.model.product.AccommodationModel;
import de.hybris.platform.travelservices.model.travel.AccommodationCategoryModel;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * The type Accommodation categories populator.
 *
 * @param <SOURCE>
 * 		the type parameter
 * @param <TARGET>
 * 		the type parameter
 */
public class AccommodationCategoriesPopulator<SOURCE extends AccommodationModel, TARGET extends RoomTypeData>
		implements Populator<SOURCE, TARGET>
{

	private Converter<AccommodationCategoryModel, AccommodationCategoryData> accommodationCategoryConverter;

	@Override
	public void populate(final SOURCE source, final TARGET target) throws ConversionException
	{
		populateAccommodationCategories(source, target);

	}

	/**
	 * Populate accommodation categories.
	 *
	 * @param accommodationModel
	 * 		the accommodation model
	 * @param roomtype
	 * 		the room stay
	 */
	protected void populateAccommodationCategories(final AccommodationModel accommodationModel, final RoomTypeData roomtype)
	{
		final Set<AccommodationCategoryData> accommodationCategoryDataSet = new HashSet<>();
		accommodationModel.getSupercategories().stream().filter(AccommodationCategoryModel.class::isInstance).forEach(
				superCategory -> populateCategoriesFromCategory(AccommodationCategoryModel.class.cast(superCategory),
						accommodationCategoryDataSet));

		if (CollectionUtils.isEmpty(accommodationCategoryDataSet))
		{
			return;
		}
		roomtype.setAccommodationCategories(accommodationCategoryDataSet);
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
