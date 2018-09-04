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

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.travel.ancillary.data.OfferGroupData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.travelservices.model.travel.OfferGroupModel;


/**
 * Populates ignore rules property for rules evaluation
 */
public class IgnoreRulesPopulator implements Populator<CategoryModel, OfferGroupData>
{

	@Override
	public void populate(final CategoryModel source, final OfferGroupData target) throws ConversionException
	{
		target.setIgnoreRules(
				OfferGroupModel.class.isInstance(source) ? OfferGroupModel.class.cast(source).getIgnoreRules() : true);
	}

}
