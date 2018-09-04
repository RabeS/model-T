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

package de.hybris.platform.accommodationbackoffice.cronjob;

import de.hybris.platform.accommodationbackoffice.model.AccommodationInventoryCronJobModel;
import de.hybris.platform.accommodationbackoffice.model.ManageAccommodationStockLevelInfoModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.travelbackofficeservices.stock.TravelBackofficeStockService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * The type Accommodation inventory job.
 */
public class AccommodationInventoryJob extends AbstractJobPerformable<AccommodationInventoryCronJobModel>
{

	private ModelService modelService;
	private static final Logger LOG = Logger.getLogger(AccommodationInventoryJob.class);
	private TravelBackofficeStockService travelbackofficeStockService;

	@Override
	public PerformResult perform(final AccommodationInventoryCronJobModel manageInventoryModel)
	{

		LOG.info("Start performing AccommodationInventoryJob...");

		if (Objects.nonNull(manageInventoryModel.getManageStockLevel())
				&& Objects.nonNull(manageInventoryModel.getManageStockLevel().getAccommodationOffering()))
		{
			final WarehouseModel warehouse = manageInventoryModel.getManageStockLevel().getAccommodationOffering();
			createStockLevels(manageInventoryModel.getManageStockLevel(), Collections.singletonList(warehouse));
		}
		manageInventoryModel.setStockLevels(new ArrayList<>());
		manageInventoryModel.setManageStockLevel(null);
		getModelService().save(manageInventoryModel);
		LOG.info("AccommodationInventoryJob completed.");
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	/**
	 * Create stock levels.
	 *
	 * @param manageStockLevel
	 * 		the manage stock level
	 * @param warehouses
	 * 		the warehouses
	 */
	protected void createStockLevels(final ManageAccommodationStockLevelInfoModel manageStockLevel,
			final List<WarehouseModel> warehouses)
	{
		if (CollectionUtils.isEmpty(warehouses) || Objects.isNull(manageStockLevel)
				|| CollectionUtils.isEmpty(manageStockLevel.getStockLevelAttributes()))
		{
			return;
		}
		final Date startDate = manageStockLevel.getStartDate();
		final Date endDate = manageStockLevel.getEndDate();
		warehouses.forEach(warehouse -> manageStockLevel.getStockLevelAttributes().forEach(stockLevelAttribute -> {
			Date date = startDate;
			while (date.before(endDate))
			{
				getTravelbackofficeStockService().createStockLevel(warehouse, stockLevelAttribute, date);
				date = DateUtils.addDays(date, 1);
			}
		}));
	}

	/**
	 * Gets travelbackoffice stock service.
	 *
	 * @return travelbackofficeStockService travelbackoffice stock service
	 */
	protected TravelBackofficeStockService getTravelbackofficeStockService()
	{
		return travelbackofficeStockService;
	}

	/**
	 * Sets travelbackoffice stock service.
	 *
	 * @param travelbackofficeStockService
	 * 		the travelbackofficeStockService to set
	 */
	@Required
	public void setTravelbackofficeStockService(final TravelBackofficeStockService travelbackofficeStockService)
	{
		this.travelbackofficeStockService = travelbackofficeStockService;
	}

	/**
	 * Gets model service.
	 *
	 * @return model service
	 */
	protected ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * Sets model service.
	 *
	 *@param modelService
	 *      the modelService to set
	 */
	@Override
	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}
}
