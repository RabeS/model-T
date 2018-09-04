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

package de.hybris.platform.accommodationbackoffice.controllers;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.zk.ui.event.Events;

import com.hybris.backoffice.navigation.TreeNodeSelector;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.widgets.configurableflow.ConfigurableFlowContextParameterNames;


/**
 *
 */
public class AccommodationMainPanelController extends DefaultWidgetController
{

	@ViewEvent(componentID = "createAccommodationProvider", eventName = Events.ON_CLICK)
	public void createAccommodationProvider() throws InterruptedException
	{
		final Map<String, Object> outputCtx = new HashMap<>();
		outputCtx.put(ConfigurableFlowContextParameterNames.TYPE_CODE.getName(), "AccommodationProvider");
		sendOutput("contextMap", outputCtx);
	}

	@ViewEvent(componentID = "createAccommodationOffering", eventName = Events.ON_CLICK)
	public void createAccommodationOffering() throws InterruptedException
	{
		final Map<String, Object> outputCtx = new HashMap<>();
		outputCtx.put(ConfigurableFlowContextParameterNames.TYPE_CODE.getName(), "AccommodationOffering");
		sendOutput("contextMap", outputCtx);
	}

	@ViewEvent(componentID = "createSleepingRoom", eventName = Events.ON_CLICK)
	public void createSleepingRoom() throws InterruptedException
	{
		final Map<String, Object> outputCtx = new HashMap<>();
		outputCtx.put(ConfigurableFlowContextParameterNames.TYPE_CODE.getName(), "SleepingRoom");
		sendOutput("contextMap", outputCtx);
	}

	@ViewEvent(componentID = "createNonSleepingRoom", eventName = Events.ON_CLICK)
	public void createNonSleepingRoom() throws InterruptedException
	{
		final Map<String, Object> outputCtx = new HashMap<>();
		outputCtx.put(ConfigurableFlowContextParameterNames.TYPE_CODE.getName(), "NonSleepingRoom");
		sendOutput("contextMap", outputCtx);
	}

	@ViewEvent(componentID = "createRatePlan", eventName = Events.ON_CLICK)
	public void createRatePlan() throws InterruptedException
	{
		final Map<String, Object> outputCtx = new HashMap<>();
		outputCtx.put(ConfigurableFlowContextParameterNames.TYPE_CODE.getName(), "RatePlan");
		sendOutput("contextMap", outputCtx);
	}

	@ViewEvent(componentID = "createNonRoomProduct", eventName = Events.ON_CLICK)
	public void createNonRoomProduct() throws InterruptedException
	{
		final Map<String, Object> outputCtx = new HashMap<>();
		outputCtx.put(ConfigurableFlowContextParameterNames.TYPE_CODE.getName(), "ExtraProduct");
		sendOutput("contextMap", outputCtx);
	}

	@ViewEvent(componentID = "merchandisingWorkspaceBtn", eventName = Events.ON_CLICK)
	public void openRulesExplorerTree() throws InterruptedException
	{
		sendOutput("treeNode", new TreeNodeSelector("hmc_typenode_accommodation_all_source_rule_templates", true));
	}

	@ViewEvent(componentID = "assignRoomAndFloor", eventName = Events.ON_CLICK)
	public void addRoomTypeConfiguration() throws InterruptedException
	{
		final Map<String, Object> outputCtx = new HashMap<>();
		outputCtx.put(ConfigurableFlowContextParameterNames.TYPE_CODE.getName(), "RoomTypeConfiguration");
		sendOutput("contextMap", outputCtx);
	}

	@ViewEvent(componentID = "addAccommodationInventory", eventName = Events.ON_CLICK)
	public void addAccommodationInventory() throws InterruptedException
	{
		final Map<String, Object> outputCtx = new HashMap<>();
		outputCtx.put(ConfigurableFlowContextParameterNames.TYPE_CODE.getName(), "AccommodationInventory");
		sendOutput("contextMap", outputCtx);
	}

	@ViewEvent(componentID = "modifyAccommodationInventory", eventName = Events.ON_CLICK)
	public void modifyInventory() throws InterruptedException
	{
		final Map<String, Object> outputCtx = new HashMap<>();
		outputCtx.put(ConfigurableFlowContextParameterNames.TYPE_CODE.getName(), "AccommodationInventory");
		sendOutput("contextMapForModifyWizard", outputCtx);
	}

}
