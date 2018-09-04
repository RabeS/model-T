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
import de.hybris.platform.accommodationbackofficeservices.stocklevel.ManageAccommodationStockLevelInfo;
import de.hybris.platform.travelbackoffice.constants.TravelbackofficeConstants;
import de.hybris.platform.travelservices.model.warehouse.AccommodationOfferingModel;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import org.springframework.util.StringUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.Button;

import com.hybris.cockpitng.config.jaxb.wizard.CustomType;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandler;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandlerAdapter;


/**
 * This is the handler for the validations for the Step #1 of the Add - manage inventory
 */
public class AccommodationInventoryHandler implements FlowActionHandler
{

	@Override
	public void perform(final CustomType customType, final FlowActionHandlerAdapter adapter, final Map<String, String> parameters)
	{
		final AccommodationOfferingModel accommodationOffering = adapter.getWidgetInstanceManager().getModel()
				.getValue(AccommodationbackofficeConstants.ACCOMMODATION_OFFERING, AccommodationOfferingModel.class);

		final String accommodationType = adapter.getWidgetInstanceManager().getModel()
				.getValue(AccommodationbackofficeConstants.SELECTED_ACCOMMODATION, String.class);

		final Date startDate = adapter.getWidgetInstanceManager().getModel().getValue(AccommodationbackofficeConstants.START_DATE,
				Date.class);

		final Date endDate = adapter.getWidgetInstanceManager().getModel().getValue(AccommodationbackofficeConstants.END_DATE,
				Date.class);

		if (Objects.isNull(accommodationOffering) || StringUtils.isEmpty(accommodationType) || Objects.isNull(startDate)
				|| Objects.isNull(endDate))
		{
			Messagebox.show(Labels.getLabel(AccommodationbackofficeConstants.MANAGE_INVENTORY_MANDATORY_FIELDS_ERROR),
					Labels.getLabel(TravelbackofficeConstants.MESSAGE_BOX_WARNING_TITLE), new Button[]
					{ Button.OK }, null, null);
			return;
		}

		if (startDate.after(endDate))
		{
			Messagebox.show(Labels.getLabel(AccommodationbackofficeConstants.MANAGE_INVENTORY_DATE_FIELDS_ERROR),
					Labels.getLabel(TravelbackofficeConstants.MESSAGE_BOX_WARNING_TITLE), new Button[]
					{ Button.OK }, null, null);
			return;
		}
		final ManageAccommodationStockLevelInfo manageStockLevel = adapter.getWidgetInstanceManager().getModel()
				.getValue("manageStockLevelInfo", ManageAccommodationStockLevelInfo.class);

		manageStockLevel.setAccommodationOffering(accommodationOffering);
		manageStockLevel.setProductType(accommodationType);
		manageStockLevel.setStartDate(startDate);
		manageStockLevel.setEndDate(endDate);

		adapter.getWidgetInstanceManager().getModel().put(TravelbackofficeConstants.FROM_PREVIOUS_STEP, Boolean.FALSE);

		adapter.next();
	}

}
