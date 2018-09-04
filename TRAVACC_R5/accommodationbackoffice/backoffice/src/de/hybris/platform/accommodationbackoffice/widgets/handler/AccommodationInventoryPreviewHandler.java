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

package de.hybris.platform.accommodationbackoffice.widgets.handler;

import de.hybris.platform.accommodationbackoffice.constants.AccommodationbackofficeConstants;
import de.hybris.platform.accommodationbackoffice.model.AccommodationInventoryCronJobModel;
import de.hybris.platform.accommodationbackoffice.model.ManageAccommodationStockLevelInfoModel;
import de.hybris.platform.accommodationbackofficeservices.stocklevel.ManageAccommodationStockLevelInfo;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.travelbackoffice.constants.TravelbackofficeConstants;
import de.hybris.platform.travelbackofficeservices.model.StockLevelAttributesModel;
import de.hybris.platform.travelservices.stocklevel.StockLevelAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.CollectionUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.Button;

import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationUtils;
import com.hybris.cockpitng.config.jaxb.wizard.CustomType;
import com.hybris.cockpitng.util.MessageboxUtils;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandler;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandlerAdapter;


/**
 * This is the handler for the validations for the Step #1 of the Add - manage inventory
 */
public class AccommodationInventoryPreviewHandler implements FlowActionHandler
{

	private CronJobService cronjobService;
	private ModelService modelService;

	@Override
	public void perform(final CustomType customType, final FlowActionHandlerAdapter adapter, final Map<String, String> parameters)
	{

		final ManageAccommodationStockLevelInfo manageStockLevel = adapter.getWidgetInstanceManager().getModel().getValue(
				TravelbackofficeConstants.CREATE_INVENTORY_BOOKING_CLASS_MANAGE_STOCK_LEVEL_ITEM_ID,
				ManageAccommodationStockLevelInfo.class);

		Messagebox.show(Labels.getLabel(AccommodationbackofficeConstants.POPUP_CONFIRMATION_MESSAGE),
				Labels.getLabel(AccommodationbackofficeConstants.POPUP_TITLE_MESSAGE), MessageboxUtils.NO_YES_OPTION,
				"z-messagebox-icon z-messagebox-information", clickEvent -> {
					if (Button.YES.equals(clickEvent.getButton()))
					{
						final List<StockLevelModel> stockLevelModels = new ArrayList<>();
						final AccommodationInventoryCronJobModel accommodationInventoryJob = (AccommodationInventoryCronJobModel) getCronjobService()
								.getCronJob("accommodationInventoryCronJob");

						accommodationInventoryJob.setStockLevels(stockLevelModels);
						accommodationInventoryJob
								.setManageStockLevel(populateManageStockLevelModel(manageStockLevel, stockLevelModels));
						getModelService().save(accommodationInventoryJob);
						getCronjobService().performCronJob(accommodationInventoryJob, true);

						NotificationUtils.notifyUser(NotificationUtils.getWidgetNotificationSource(adapter.getWidgetInstanceManager()),
								TravelbackofficeConstants.TRAVEL_GLOBAL_NOTIFICATION_EVENT_TYPE, NotificationEvent.Level.SUCCESS,
								Labels.getLabel(AccommodationbackofficeConstants.SUCCESS_CONFIRMATION));
						adapter.done();
					}
				});
	}

	/**
	 * @param manageStockLevel
	 * @param stockLevelModels
	 * @return
	 */
	protected ManageAccommodationStockLevelInfoModel populateManageStockLevelModel(
			final ManageAccommodationStockLevelInfo manageStockLevel, final List<StockLevelModel> stockLevelModels)
	{
		final ManageAccommodationStockLevelInfoModel manageStockLevelInfoModel = getModelService()
				.create(ManageAccommodationStockLevelInfoModel.class);
		List<StockLevelAttributesModel> stockLevelAttributes = populateStockLevelAttributes(manageStockLevel.getStockLevelAttributes());
		manageStockLevelInfoModel.setStockLevelAttributes(stockLevelAttributes);
		manageStockLevelInfoModel.setAccommodationOffering(manageStockLevel.getAccommodationOffering());
		manageStockLevelInfoModel.setModified(manageStockLevel.getModified());
		manageStockLevelInfoModel.setStartDate(manageStockLevel.getStartDate());
		manageStockLevelInfoModel.setEndDate(manageStockLevel.getEndDate());
		getModelService().saveAll(stockLevelAttributes);
		getModelService().save(manageStockLevelInfoModel);
		return manageStockLevelInfoModel;
	}

	/**
	 * @param stockLevelAttributes
	 * @param list
	 * @return
	 */
	protected List<StockLevelAttributesModel> populateStockLevelAttributes(final List<StockLevelAttributes> stockLevelAttributes)
	{
		final List<StockLevelAttributesModel> stockLevelAttributesList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(stockLevelAttributes))
		{
			stockLevelAttributes.forEach(stockLevelAttribute -> {
				final StockLevelAttributesModel stockLevelAttributesModel = getModelService().create(StockLevelAttributesModel.class);
				stockLevelAttributesModel.setCode(stockLevelAttribute.getCode());
				stockLevelAttributesModel.setOversellingQuantity(stockLevelAttribute.getOversellingQuantity());
				stockLevelAttributesModel.setAvailableQuantity(stockLevelAttribute.getAvailableQuantity());
				stockLevelAttributesModel.setAncillaryProduct(stockLevelAttribute.getAncillaryProduct());
				stockLevelAttributesModel.setInStockStatus(stockLevelAttribute.getInStockStatus());
				stockLevelAttributesList.add(stockLevelAttributesModel);
			});
		}
		return stockLevelAttributesList;
	}

	/**
	 * @return
	 */
	protected CronJobService getCronjobService()
	{
		return cronjobService;
	}

	/**
	 * @param cronjobService
	 */
	@Required
	public void setCronjobService(final CronJobService cronjobService)
	{
		this.cronjobService = cronjobService;
	}

	/**
	 * @return
	 */
	protected ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 */
	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

}
