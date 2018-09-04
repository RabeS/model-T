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

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.persistence.audit.gateway.WriteAuditGateway;
import de.hybris.platform.retention.hook.ItemCleanupHook;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.travelservices.model.travel.RemarkModel;
import de.hybris.platform.travelservices.model.user.SavedSearchModel;
import de.hybris.platform.travelservices.model.user.SpecialRequestDetailModel;
import de.hybris.platform.travelservices.model.user.TravellerInfoModel;
import de.hybris.platform.travelservices.model.user.TravellerModel;
import de.hybris.platform.travelservices.model.user.TravellerPreferenceModel;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


/**
 * This Hook removes customer travel specific objects such as customer traveller instance, saved travellers and saved
 * searchs.
 *
 */
public class TravelCommerceServicesCustomerCleanupHook implements ItemCleanupHook<CustomerModel>
{
	private static final Logger LOG = LoggerFactory.getLogger(TravelCommerceServicesCustomerCleanupHook.class);
	private ModelService modelService;
	private WriteAuditGateway writeAuditGateway;

	@Override
	public void cleanupRelatedObjects(final CustomerModel customerModel)
	{
		validateParameterNotNullStandardMessage("customerModel", customerModel);
		
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Cleaning up customer related objects for: {}", customerModel);
		}

		cleanupTravellerRelatedObjects(customerModel);

		for (final SavedSearchModel savedSearch : customerModel.getSavedSearch())
		{
			getModelService().remove(savedSearch);
			getWriteAuditGateway().removeAuditRecordsForType(SavedSearchModel._TYPECODE, savedSearch.getPk());
		}
		
	}
	
	protected void cleanupTravellerRelatedObjects(final CustomerModel customerModel){
		
		final TravellerModel customerTravellerInstance = customerModel.getCustomerTravellerInstance();
		
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Cleaning up traveller related objects for: {}", customerTravellerInstance);
		}
		
		if (Objects.nonNull(customerTravellerInstance))
		{
			final SpecialRequestDetailModel specialRequestDetail = customerTravellerInstance.getSpecialRequestDetail();
			if (Objects.nonNull(specialRequestDetail))
			{
				specialRequestDetail.getRemarks().forEach(remark -> {
					getModelService().remove(remark);
					getWriteAuditGateway().removeAuditRecordsForType(RemarkModel._TYPECODE, remark.getPk());
				});

				getModelService().remove(specialRequestDetail);
				getWriteAuditGateway().removeAuditRecordsForType(SpecialRequestDetailModel._TYPECODE, specialRequestDetail.getPk());
			}

			if (Objects.nonNull(customerTravellerInstance.getTravellerPreference()))
			{
				customerTravellerInstance.getTravellerPreference().forEach(travellerPreferenceModel -> {
					getModelService().remove(travellerPreferenceModel);
					getWriteAuditGateway().removeAuditRecordsForType(TravellerPreferenceModel._TYPECODE,
							travellerPreferenceModel.getPk());
				});
			}

			final TravellerInfoModel travellerInfo = customerTravellerInstance.getInfo();
			if (Objects.nonNull(travellerInfo))
			{
				getModelService().remove(travellerInfo);
				getWriteAuditGateway().removeAuditRecordsForType(TravellerInfoModel._TYPECODE, travellerInfo.getPk());
			}
			getModelService().remove(customerTravellerInstance);
			getWriteAuditGateway().removeAuditRecordsForType(TravellerModel._TYPECODE, customerTravellerInstance.getPk());
		}
		if(Objects.nonNull(customerModel.getSavedTraveller()))
		{
			customerModel.getSavedTraveller().forEach(savedTraveller -> {
				getModelService().remove(savedTraveller);
				getWriteAuditGateway().removeAuditRecordsForType(TravellerModel._TYPECODE, savedTraveller.getPk());
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
