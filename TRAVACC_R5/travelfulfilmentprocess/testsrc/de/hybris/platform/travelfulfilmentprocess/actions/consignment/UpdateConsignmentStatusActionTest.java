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

package de.hybris.platform.travelfulfilmentprocess.actions.consignment;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.orderhistory.OrderHistoryService;
import de.hybris.platform.orderhistory.model.OrderHistoryEntryModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.travelservices.model.order.TravelOrderEntryInfoModel;
import de.hybris.platform.travelservices.model.user.TravellerModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


/**
 * The type Update consignment status action test.
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class UpdateConsignmentStatusActionTest
{
	@InjectMocks
	UpdateConsignmentStatusAction updateConsignmentStatusAction;

	@Mock
	CustomerAccountService customerAccountService;

	@Mock
	OrderHistoryService orderHistoryService;

	@Mock
	TimeService timeService;

	@Mock
	ModelService modelService;

	@Test
	public void testExecuteAction() throws RetryLaterException, Exception
	{
		final OrderProcessModel process = new OrderProcessModel();
		final OrderModel order = new OrderModel();
		final OrderModel originalOrder = new OrderModel();
		final UserModel user = new UserModel();
		final UserModel userOriginal = new UserModel();
		order.setUser(user);
		originalOrder.setUser(userOriginal);

		final AbstractOrderEntryModel entry1 = new AbstractOrderEntryModel();
		final AbstractOrderEntryModel entry2 = new AbstractOrderEntryModel();
		final TravelOrderEntryInfoModel travelOrderEntryInfoModel = new TravelOrderEntryInfoModel();
		final Collection<TravellerModel> travellers = Collections.singletonList(new TravellerModel());
		travelOrderEntryInfoModel.setTravellers(travellers);
		entry1.setTravelOrderEntryInfo(travelOrderEntryInfoModel);
		entry1.setEntryNumber(new Integer(1));
		entry1.setActive(true);
		entry2.setTravelOrderEntryInfo(travelOrderEntryInfoModel);
		entry2.setEntryNumber(new Integer(1));
		entry2.setActive(false);
		final List<AbstractOrderEntryModel> entries = new ArrayList<>();
		entries.add(entry1);
		entries.add(entry2);
		originalOrder.setEntries(entries);
		order.setEntries(entries);
		order.setOriginalOrder(originalOrder);
		process.setOrder(order);

		Mockito.when(orderHistoryService.createHistorySnapshot(originalOrder)).thenReturn(originalOrder);
		final OrderHistoryEntryModel historyEntry = new OrderHistoryEntryModel();
		originalOrder.setHistoryEntries(Collections.singletonList(historyEntry));
		Mockito.when(timeService.getCurrentTime()).thenReturn(new Date());
		Mockito.when(modelService.create(OrderHistoryEntryModel.class)).thenReturn(historyEntry);

		final ConsignmentModel consignmentModel = new ConsignmentModel();
		consignmentModel.setCode("consignmentModelCode");
		consignmentModel.setStatus(ConsignmentStatus.CHECKED_IN);
		final ConsignmentModel consignmentModel1 = new ConsignmentModel();
		consignmentModel1.setCode("consignmentModel1Code");
		consignmentModel1.setStatus(ConsignmentStatus.READY);
		final Set<ConsignmentModel> consignments = new HashSet<ConsignmentModel>();
		consignments.add(consignmentModel);
		consignments.add(consignmentModel1);

		final Set<ConsignmentEntryModel> consignmentEntries = new HashSet<>();
		final ConsignmentEntryModel consignmentEntryModel1 = new ConsignmentEntryModel();
		final ConsignmentEntryModel consignmentEntryModel2 = new ConsignmentEntryModel();
		consignmentEntryModel1.setOrderEntry(entry1);
		consignmentEntryModel2.setOrderEntry(entry2);
		consignmentEntries.add(consignmentEntryModel1);
		consignmentEntries.add(consignmentEntryModel2);

		consignmentModel.setConsignmentEntries(consignmentEntries);
		consignmentModel.setConsignmentProcesses(new ArrayList<ConsignmentProcessModel>());
		consignmentModel1.setConsignmentEntries(consignmentEntries);
		consignmentModel1.setConsignmentProcesses(new ArrayList<ConsignmentProcessModel>());

		order.setConsignments(consignments);

		originalOrder.setConsignments(consignments);

		Mockito.when(modelService.clone(consignmentModel)).thenReturn(consignmentModel);
		Mockito.when(modelService.clone(consignmentModel1)).thenReturn(consignmentModel1);
		Mockito.when(modelService.clone(consignmentEntryModel1)).thenReturn(consignmentEntryModel1);
		Mockito.when(modelService.clone(consignmentEntryModel2)).thenReturn(consignmentEntryModel2);

		updateConsignmentStatusAction.executeAction(process);
		Mockito.verify(modelService).saveAll(Matchers.any(OrderModel.class), Matchers.any(OrderHistoryEntryModel.class));
	}
}
