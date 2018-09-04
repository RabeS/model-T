package de.hybris.platform.travelfacades.populators;

import de.hybris.platform.commercefacades.accommodation.AccommodationCategoryData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.travelservices.model.travel.AccommodationCategoryModel;

import org.apache.commons.lang.BooleanUtils;


/**
 * Populator to populate the basic information of an AccommodationCategoryData from the AccommodationCategoryModel
 */
public class AccommodationCategoryBasicPopulator implements Populator<AccommodationCategoryModel, AccommodationCategoryData>
{

	@Override
	public void populate(final AccommodationCategoryModel source, final AccommodationCategoryData target)
			throws ConversionException
	{
		target.setName(source.getName());
		target.setCode(source.getCode());
		target.setIgnoreRules(BooleanUtils.isTrue(source.getIgnoreRules()));
	}

}