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

package de.hybris.platform.travelservices.retention.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.persistence.audit.gateway.WriteAuditGateway;
import de.hybris.platform.retention.hook.ItemCleanupHook;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.travelservices.model.AbstractOrderEntryGroupModel;
import de.hybris.platform.travelservices.model.order.AccommodationOrderEntryGroupModel;
import de.hybris.platform.travelservices.model.order.AccommodationOrderEntryInfoModel;
import de.hybris.platform.travelservices.model.order.TravelOrderEntryInfoModel;
import de.hybris.platform.travelservices.model.travel.RemarkModel;
import de.hybris.platform.travelservices.model.user.SpecialRequestDetailModel;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * This Hook removes customer travel specific objects such as customer traveller instance, saved travellers and saved
 * searchs.
 *
 */
public class TravelCommerceServicesOrderCleanupHook implements ItemCleanupHook<OrderModel>
{
	private static final Logger LOG = LoggerFactory.getLogger(TravelCommerceServicesOrderCleanupHook.class);
	private ModelService modelService;
	private WriteAuditGateway writeAuditGateway;

	@Override
	public void cleanupRelatedObjects(final OrderModel orderModel)
	{
		validateParameterNotNullStandardMessage("orderModel", orderModel);
		
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Cleaning up order related objects for: {}", orderModel);
		}
		
		if(Objects.nonNull(orderModel.getEntries()))
		{
			orderModel.getEntries().forEach(entry -> {
				
				final TravelOrderEntryInfoModel travelOrderEntryInfo = entry.getTravelOrderEntryInfo();
				
				if(Objects.nonNull(travelOrderEntryInfo))
				{
					final SpecialRequestDetailModel specialRequestDetail = travelOrderEntryInfo.getSpecialRequestDetail();
					if(Objects.nonNull(specialRequestDetail) && Objects.nonNull(specialRequestDetail.getRemarks()))
					{
						specialRequestDetail.getRemarks().forEach(remarks -> {
   						getModelService().remove(remarks);
   						getWriteAuditGateway().removeAuditRecordsForType(RemarkModel._TYPECODE, remarks.getPk());
   					});
					}
					
					getModelService().remove(travelOrderEntryInfo);
					getWriteAuditGateway().removeAuditRecordsForType(TravelOrderEntryInfoModel._TYPECODE, travelOrderEntryInfo.getPk());
				}
				
				if(Objects.nonNull(entry.getAccommodationOrderEntryInfo()))
				{
					getModelService().remove(entry.getAccommodationOrderEntryInfo());
					getWriteAuditGateway().removeAuditRecordsForType(AccommodationOrderEntryInfoModel._TYPECODE,
							entry.getAccommodationOrderEntryInfo().getPk());
				}
				final AbstractOrderEntryGroupModel orderEntryGroup = entry.getEntryGroup();
				if(Objects.nonNull(orderEntryGroup) && orderEntryGroup instanceof AccommodationOrderEntryGroupModel)
				{
					final AccommodationOrderEntryGroupModel accommodationOrderEntryGroupModel = (AccommodationOrderEntryGroupModel)orderEntryGroup;
					if(Objects.nonNull(accommodationOrderEntryGroupModel.getSpecialRequestDetail()) 
							&& Objects.nonNull(accommodationOrderEntryGroupModel.getSpecialRequestDetail().getRemarks()))
					{
      				accommodationOrderEntryGroupModel.getSpecialRequestDetail().getRemarks().forEach(remark -> {
      					getModelService().remove(remark);
      					getWriteAuditGateway().removeAuditRecordsForType(RemarkModel._TYPECODE, remark.getPk());
      				});
					}
					getModelService().remove(accommodationOrderEntryGroupModel);
					getWriteAuditGateway().removeAuditRecordsForType(AccommodationOrderEntryGroupModel._TYPECODE, accommodationOrderEntryGroupModel.getPk());
				}
				
				
				getModelService().remove(entry);
				getWriteAuditGateway().removeAuditRecordsForType(OrderEntryModel._TYPECODE, entry.getPk());
				});
		}
		
	}
	
	/**
	 * @return the modelService
	 */
	protected ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * @return the writeAuditGateway
	 */
	protected WriteAuditGateway getWriteAuditGateway()
	{
		return writeAuditGateway;
	}

	/**
	 * @param writeAuditGateway
	 *           the writeAuditGateway to set
	 */
	@Required
	public void setWriteAuditGateway(final WriteAuditGateway writeAuditGateway)
	{
		this.writeAuditGateway = writeAuditGateway;
	}
}
