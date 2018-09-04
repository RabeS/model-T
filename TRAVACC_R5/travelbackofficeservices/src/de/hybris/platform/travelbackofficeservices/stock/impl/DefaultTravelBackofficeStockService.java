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

package de.hybris.platform.travelbackofficeservices.stock.impl;

import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.travelbackofficeservices.model.ManageStockLevelInfoModel;
import de.hybris.platform.travelbackofficeservices.stock.TravelBackofficeStockService;
import de.hybris.platform.travelbackofficeservices.stocklevel.ManageStockLevelInfo;
import de.hybris.platform.travelservices.model.travel.ScheduleConfigurationModel;
import de.hybris.platform.travelservices.model.travel.TravelSectorModel;
import de.hybris.platform.travelservices.stock.TravelStockService;
import de.hybris.platform.travelservices.stock.impl.DefaultTravelStockService;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;


/**
 * Default implementation of {@link TravelStockService}
 */
public class DefaultTravelBackofficeStockService extends DefaultTravelStockService implements TravelBackofficeStockService
{
	/**
	 * @deprecated Deprecated since version 5.0.
	 */
	@Deprecated
	@Override
	public void createStockLevels(final ManageStockLevelInfo manageStockLevel,
			final Collection<? extends WarehouseModel> warehouses)
	{
		if (CollectionUtils.isEmpty(warehouses) || Objects.isNull(manageStockLevel)
				|| CollectionUtils.isEmpty(manageStockLevel.getStockLevelAttributes()))
		{
			return;
		}
		warehouses.forEach(warehouse -> manageStockLevel.getStockLevelAttributes()
				.forEach(stockLevelAttribute -> createStockLevel(warehouse, stockLevelAttribute)));
	}

	/**
	 * @deprecated Deprecated since version 5.0.
	 */
	@Deprecated
	@Override
	public void createStockLevels(final ManageStockLevelInfo manageStockLevel, final TravelSectorModel sector)
	{
		if (Objects.isNull(sector))
		{
			return;
		}
		createStockLevels(manageStockLevel, sector.getTransportOffering());
	}

	/**
	 * @deprecated Deprecated since version 5.0.
	 */
	@Deprecated
	@Override
	public void createStockLevels(final ManageStockLevelInfo manageStockLevel, final ScheduleConfigurationModel schedule)
	{
		if (Objects.isNull(schedule))
		{
			return;
		}
		createStockLevels(manageStockLevel, schedule.getTransportOfferings());
	}

	/**
	 * @deprecated Deprecated since version 5.0.
	 */
	@Deprecated
	@Override
	public void updateStockLevels(final ManageStockLevelInfo manageStockLevel,
			final Collection<? extends WarehouseModel> warehouses)
	{
		if (CollectionUtils.isEmpty(warehouses) || Objects.isNull(manageStockLevel)
				|| CollectionUtils.isEmpty(manageStockLevel.getStockLevelAttributes()))
		{
			return;
		}
		warehouses.forEach(warehouse -> manageStockLevel.getStockLevelAttributes()
				.forEach(stockLevelAttribute -> updateStockLevel(warehouse, stockLevelAttribute)));
	}

	/**
	 * @deprecated Deprecated since version 5.0.
	 */
	@Deprecated
	@Override
	public void updateStockLevels(final ManageStockLevelInfo manageStockLevel, final ScheduleConfigurationModel schedule)
	{
		if (Objects.isNull(schedule))
		{
			return;
		}
		updateStockLevels(manageStockLevel, schedule.getTransportOfferings());
	}

	/**
	 * @deprecated Deprecated since version 5.0.
	 */
	@Deprecated
	@Override
	public void updateStockLevels(final ManageStockLevelInfo manageStockLevel, final TravelSectorModel sector)
	{
		if (Objects.isNull(sector))
		{
			return;
		}
		updateStockLevels(manageStockLevel, sector.getTransportOffering());
	}

	@Override
	public void createStockLevels(final ManageStockLevelInfoModel manageStockLevel, final List<WarehouseModel> warehouses)
	{
		if (CollectionUtils.isEmpty(warehouses) || Objects.isNull(manageStockLevel)
				|| CollectionUtils.isEmpty(manageStockLevel.getStockLevelAttributes()))
		{
			return;
		}
		warehouses.forEach(warehouse -> manageStockLevel.getStockLevelAttributes()
				.forEach(stockLevelAttribute -> createStockLevel(warehouse, stockLevelAttribute)));
	}
}
