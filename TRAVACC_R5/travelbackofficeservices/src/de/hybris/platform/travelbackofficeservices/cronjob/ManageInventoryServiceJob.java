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

package de.hybris.platform.travelbackofficeservices.cronjob;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.travelbackofficeservices.model.cronjob.ManageInventoryServiceCronJobModel;
import de.hybris.platform.travelbackofficeservices.stock.TravelBackofficeStockService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class ManageInventoryServiceJob extends AbstractJobPerformable<ManageInventoryServiceCronJobModel>
{
	private static final Logger LOG = Logger.getLogger(ManageInventoryServiceJob.class);
	private TravelBackofficeStockService travelbackofficeStockService;
	
	@Override
	public PerformResult perform(final ManageInventoryServiceCronJobModel manageInventoryModel)
	{
		LOG.info("Start performing ManageInventoryServiceJob...");
		
		if(Objects.nonNull(manageInventoryModel.getManageStockLevel()) && 
				CollectionUtils.isNotEmpty(manageInventoryModel.getManageStockLevel().getTransportOfferings())){
			if (BooleanUtils.isTrue(manageInventoryModel.getManageStockLevel().getModified())){
				final List<WarehouseModel> warehouses = new ArrayList<>(manageInventoryModel.getManageStockLevel().getTransportOfferings());
				if(CollectionUtils.isNotEmpty(manageInventoryModel.getStockLevels())){
   				final List<StockLevelModel> stockLevels = manageInventoryModel.getStockLevels();
   				getTravelbackofficeStockService().updateStockLevelsForTransportOffering(stockLevels, warehouses);
				}
			}
			else{
				final List<WarehouseModel> warehouses = new ArrayList<>(manageInventoryModel.getManageStockLevel().getTransportOfferings());
				getTravelbackofficeStockService().createStockLevels(manageInventoryModel.getManageStockLevel(), warehouses);
			}
		}
		manageInventoryModel.setStockLevels(new ArrayList<>());
		manageInventoryModel.setManageStockLevel(null);
		getModelService().save(manageInventoryModel);
		LOG.info("ManageInventoryServiceJob completed.");
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}
	
	/**
	 * @return travelbackofficeStockService
	 */
	protected TravelBackofficeStockService getTravelbackofficeStockService()
	{
		return travelbackofficeStockService;
	}

	/**
	 * @param travelbackofficeStockService
	 * 		the travelbackofficeStockService to set
	 */
	@Required
	public void setTravelbackofficeStockService(final TravelBackofficeStockService travelbackofficeStockService)
	{
		this.travelbackofficeStockService = travelbackofficeStockService;
	}

	/**
	 * @return
	 */
	protected ModelService getModelService()
	{
		return modelService;
	}

}
