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
package de.hybris.platform.travelservices.cronjob;

import de.hybris.platform.basecommerce.enums.InStockStatus;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.travelservices.model.cronjob.UpdateStockLevelsToTransportOfferingCronJobModel;
import de.hybris.platform.travelservices.model.travel.TransportFacilityModel;
import de.hybris.platform.travelservices.model.travel.TravelSectorModel;
import de.hybris.platform.travelservices.model.warehouse.TransportOfferingModel;
import de.hybris.platform.travelservices.services.TransportOfferingService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


/**
 * A Cron job to set-up StockLevels to TransportOfferings.
 */
public class UpdateStockLevelsToTransportOfferingJob
		extends AbstractJobPerformable<UpdateStockLevelsToTransportOfferingCronJobModel>
{
	private static final Logger LOG = LoggerFactory.getLogger(UpdateStockLevelsToTransportOfferingJob.class);

	private static final int FORCE_IN_STOCK_QUANTITY = -1;
	private static final int FORCE_OUT_OF_STOCK_QUANTITY = -2;

	private TransportOfferingService transportOfferingService;

	/**
	 * This method calls a DAO to get all configured TransportOfferings. For each TransportOffering check if the travelSector is
	 * domestic, if yes, set economy stockLevel, else(i.e international sector) set economy, economyplus & business stockLevels.
	 *
	 * @param updateStockLevelsToTransportOfferingCronJobModel
	 * 		CronJob model with stockLevel data.
	 */
	@Override
	public PerformResult perform(
			final UpdateStockLevelsToTransportOfferingCronJobModel updateStockLevelsToTransportOfferingCronJobModel)
	{
		LOG.info("Performing Cronjob -> UpdateStockLevelsToTransportOfferingCronJob....");

		final int batchSize = updateStockLevelsToTransportOfferingCronJobModel.getBatchSize();
		int offset = 0;
		SearchResult<TransportOfferingModel> searchResult;

		do
		{
			searchResult = getTransportOfferingService().getTransportOfferings(batchSize, offset);
			for (final TransportOfferingModel transportOffering : searchResult.getResult())
			{
				if (!transportOffering.getActive())
				{
					continue;
				}
				final List<String> transportFacilities = new ArrayList<>();
				final Optional<TravelSectorModel> travelSectorModel = Optional.ofNullable(transportOffering.getTravelSector());
				final Optional<String> origin = travelSectorModel.map(TravelSectorModel::getOrigin)
						.map(TransportFacilityModel::getCode);
				final Optional<String> destination = travelSectorModel.map(TravelSectorModel::getDestination)
						.map(TransportFacilityModel::getCode);

				origin.ifPresent(originCode -> destination.ifPresent(destinationCode -> {
					transportFacilities.add(originCode);
					transportFacilities.add(destinationCode);
					transportFacilities.add(origin.get());
					setupEconomyAndAncillaryStockLevels(updateStockLevelsToTransportOfferingCronJobModel, transportOffering);

					if (!updateStockLevelsToTransportOfferingCronJobModel.getDomesticAirports().containsAll(transportFacilities))
					{
						// It is an International sector, so set-up EconomyPlus and Business stocks as well.
						setupEcoPlusAndBusinessStockLevels(updateStockLevelsToTransportOfferingCronJobModel, transportOffering);
					}
				}));
			}
			LOG.info("Cronjob -> UpdateStockLevelsToTransportOfferingJob " + (batchSize + offset)
					+ " TransportOffering Items Processed");
			offset += batchSize;
		}
		while (offset <= searchResult.getTotalCount());

		LOG.info("Cronjob -> UpdateStockLevelsToTransportOfferingCronJob Completed");
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	protected void setupEcoPlusAndBusinessStockLevels(
			final UpdateStockLevelsToTransportOfferingCronJobModel updateStockLevelsToTransportOfferingCronJobModel,
			final TransportOfferingModel transportOffering)
	{
		setupStockLevel(transportOffering, updateStockLevelsToTransportOfferingCronJobModel.getEconomyPlusStockLevels());
		setupStockLevel(transportOffering, updateStockLevelsToTransportOfferingCronJobModel.getEconomyPlusAncillaryStockLevels());
		setupStockLevel(transportOffering, updateStockLevelsToTransportOfferingCronJobModel.getBusinessStockLevel());
		setupStockLevel(transportOffering, updateStockLevelsToTransportOfferingCronJobModel.getBusinessAncillaryStockLevel());
	}

	protected void setupEconomyAndAncillaryStockLevels(
			final UpdateStockLevelsToTransportOfferingCronJobModel updateStockLevelsToTransportOfferingCronJobModel,
			final TransportOfferingModel transportOffering)
	{
		// For all sectors, set-up economy stock
		setupStockLevel(transportOffering, updateStockLevelsToTransportOfferingCronJobModel.getEconomyStockLevels());
		setupStockLevel(transportOffering, updateStockLevelsToTransportOfferingCronJobModel.getEconomyAncillaryStockLevels());
		// setup ancillary stock
		setupStockLevel(transportOffering, updateStockLevelsToTransportOfferingCronJobModel.getAncillaryStockLevel());
	}

	/**
	 * For each Stock entry check if the product code exists, if yes, update(or reset) availability, else create a new stockLevel
	 * and assign availability
	 *
	 * @param transportOffering
	 * 		TransportOfferingModel to which the stockLevel has to be updated.
	 * @param stockMap
	 * 		Fare Booking Class / Product Code and stockLevel.
	 */
	protected void setupStockLevel(final TransportOfferingModel transportOffering, final Map<String, Integer> stockMap)
	{
		if(Objects.isNull(stockMap))
		{
			return;
		}
		stockMap.entrySet().forEach(entry -> updateTransportofferingWithStockLevel(transportOffering, entry));
	}

	protected void updateTransportofferingWithStockLevel(final TransportOfferingModel transportOffering,
			final Entry<String, Integer> entry)
	{
		final StockLevelModel stockLevelModel = CollectionUtils.isEmpty(transportOffering.getStockLevels()) ?
				createStockLevel(transportOffering, entry.getKey())
				: getStockLevelForProductCode(transportOffering, entry.getKey());

		if(FORCE_IN_STOCK_QUANTITY == entry.getValue())
		{
			stockLevelModel.setAvailable(0);
			stockLevelModel.setInStockStatus(InStockStatus.FORCEINSTOCK);
		}
		else if(FORCE_OUT_OF_STOCK_QUANTITY >= entry.getValue())
		{
			stockLevelModel.setAvailable(0);
			stockLevelModel.setInStockStatus(InStockStatus.FORCEOUTOFSTOCK);
		}
		else
		{
			stockLevelModel.setAvailable(entry.getValue());
		}

		stockLevelModel.setReserved(0);
		getModelService().save(stockLevelModel);
	}

	protected StockLevelModel getStockLevelForProductCode(final TransportOfferingModel transportOffering, final String productCode)
	{
		return transportOffering.getStockLevels()
				.stream().filter(stockLevel -> productCode.equals(stockLevel.getProductCode()))
				.findAny().orElse(createStockLevel(transportOffering, productCode));
	}

	protected StockLevelModel createStockLevel(final TransportOfferingModel transportOffering, final String productCode)
	{
		final StockLevelModel stockLevelModel = getModelService().create(StockLevelModel.class);
		stockLevelModel.setWarehouse(transportOffering);
		stockLevelModel.setProductCode(productCode);
		return stockLevelModel;
	}

	/**
	 * @return the modelService
	 */
	protected ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @return the transportOfferingService
	 */
	protected TransportOfferingService getTransportOfferingService()
	{
		return transportOfferingService;
	}

	/**
	 * @param transportOfferingService
	 * 		the transportOfferingService to set
	 */
	@Required
	public void setTransportOfferingService(final TransportOfferingService transportOfferingService)
	{
		this.transportOfferingService = transportOfferingService;
	}
}
