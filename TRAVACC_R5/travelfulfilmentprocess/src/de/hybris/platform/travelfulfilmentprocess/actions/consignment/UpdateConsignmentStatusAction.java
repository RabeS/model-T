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

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderhistory.OrderHistoryService;
import de.hybris.platform.orderhistory.model.OrderHistoryEntryModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.travelservices.constants.TravelservicesConstants;
import de.hybris.platform.travelservices.model.order.TravelOrderEntryInfoModel;
import de.hybris.platform.travelservices.model.user.TravellerModel;
import de.hybris.platform.travelservices.utils.TravelDateUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;


/**
 * The type Update consignment status action.
 */
public class UpdateConsignmentStatusAction extends AbstractProceduralAction<OrderProcessModel>
{

	private static final Logger LOG = Logger.getLogger(UpdateConsignmentStatusAction.class);

	private CustomerAccountService customerAccountService;
	private OrderHistoryService orderHistoryService;
	private TimeService timeService;

	@Override
	public void executeAction(final OrderProcessModel orderProcess) throws RetryLaterException, Exception
	{
		final OrderModel order = orderProcess.getOrder();

		if (order.getOriginalOrder() != null)
		{

			if (LOG.isDebugEnabled())
			{
				LOG.debug("Existing original order with code: " + order.getOriginalOrder().getCode() + ", then proceed "
						+ "further to change consignment status");
			}

			updateConsignmentStatus(orderProcess);
		}
	}

	private void updateConsignmentStatus(final OrderProcessModel process)
	{
		final OrderModel order = process.getOrder();
		final OrderModel originalOrder = order.getOriginalOrder();

		final OrderHistoryEntryModel entry = createNewOrderHistoryEntry(originalOrder, order);

		if (!order.getUser().equals(originalOrder.getUser()))
		{
			order.setUser(originalOrder.getUser());
		}

		updateConsignmentsStatus(order, originalOrder);
		order.setCode(originalOrder.getCode());
		removeTravellersID(order);
		// original order has now been persisted as a snapshot so it's not needed anymore
		getModelService().remove(originalOrder);
		getModelService().saveAll(order, entry);
	}

	/**
	 * Creates a snapshot of the original order and attaches it to a newly created order history entry for new order
	 *
	 * @param originalOrder
	 * 		the original order
	 * @param newOrder
	 * 		the new order
	 *
	 * @return history entry which contains snapshot of the original order
	 */
	protected OrderHistoryEntryModel createNewOrderHistoryEntry(final OrderModel originalOrder, final OrderModel newOrder)
	{
		final OrderModel snapshot = getOrderHistoryService().createHistorySnapshot(originalOrder);

		snapshot.setOriginalVersion(newOrder);
		snapshot.setStatus(OrderStatus.AMENDED);
		if (CollectionUtils.isNotEmpty(originalOrder.getHistoryEntries()))
		{
			final List<OrderHistoryEntryModel> originalOrderHistoryEntries = originalOrder.getHistoryEntries();
			final List<OrderHistoryEntryModel> snapShotHistoryEntries = new ArrayList<OrderHistoryEntryModel>(
					originalOrderHistoryEntries.size());
			originalOrderHistoryEntries.forEach(orderHistoryEntryModel ->
				snapShotHistoryEntries.add(getModelService().clone(orderHistoryEntryModel)));

			snapshot.setHistoryEntries(snapShotHistoryEntries);
		}

		updateTravellersID(snapshot);
		cloneOrderConsignments(originalOrder, snapshot);

		final OrderHistoryEntryModel entry = getModelService().create(OrderHistoryEntryModel.class);
		entry.setTimestamp(getTimeService().getCurrentTime());
		entry.setOrder(newOrder);
		entry.setDescription(
				"Amended order on " + TravelDateUtils.convertDateToStringDate(entry.getTimestamp(),
						TravelservicesConstants.DATE_TIME_PATTERN));
		entry.setPreviousOrderVersion(snapshot);
		getModelService().save(snapshot);

		return entry;
	}

	/**
	 * Updates the {@link TravellerModel} inside the {@link TravelOrderEntryInfoModel} to set a version id consistent with the
	 * provided {@link OrderModel}
	 *
	 * @param orderModel
	 * 		the order model
	 */
	protected void updateTravellersID(final OrderModel orderModel)
	{
		final Set<TravellerModel> travellers = orderModel.getEntries().stream()
				.filter(entry -> Objects.nonNull(entry.getTravelOrderEntryInfo())
						&& Objects.nonNull(entry.getTravelOrderEntryInfo().getTravellers()))
				.flatMap(entry -> entry.getTravelOrderEntryInfo().getTravellers().stream()).collect(Collectors.toSet());

		travellers.forEach(traveller ->
		{
			traveller.setVersionID(orderModel.getVersionID());
			getModelService().save(traveller);
		});
	}

	/**
	 * Removes the {@link TravellerModel} inside the {@link TravelOrderEntryInfoModel} in the provided {@link OrderModel}
	 *
	 * @param orderModel
	 */
	private void removeTravellersID(final OrderModel orderModel)
	{
		final Set<TravellerModel> travellers = orderModel.getEntries().stream()
				.filter(entry -> Objects.nonNull(entry.getTravelOrderEntryInfo())
						&& Objects.nonNull(entry.getTravelOrderEntryInfo().getTravellers()))
				.flatMap(entry -> entry.getTravelOrderEntryInfo().getTravellers().stream()).collect(Collectors.toSet());

		travellers.forEach(traveller ->
		{
			traveller.setVersionID(null);
			getModelService().save(traveller);
		});
	}

	/**
	 * Creates deep copy of consignment and consignment entries from original order to snapshot
	 * 
	 * @param originalOrder
	 *           the original order
	 * @param snapshot
	 *           the new order
	 */
	protected void cloneOrderConsignments(final OrderModel originalOrder, final OrderModel snapshot)
	{
		final Set<ConsignmentModel> originalOrderConsignments = originalOrder.getConsignments();

		if (CollectionUtils.isEmpty(originalOrderConsignments))
		{
			return;
		}
		final Set<ConsignmentModel> snapshotOrderConsignments = new HashSet<ConsignmentModel>();
		for (final ConsignmentModel originalOrderConsignment : originalOrderConsignments)
		{
			final ConsignmentModel snapshotConsignment = getModelService().clone(originalOrderConsignment);
			if (snapshotConsignment.getCode().equals(originalOrderConsignment.getCode()))
			{
				snapshotConsignment.setShippingAddress(getModelService().clone(originalOrderConsignment.getShippingAddress()));
				// copy consignment process
				final Collection<ConsignmentProcessModel> snapshotConsignmentProcesses = new ArrayList<ConsignmentProcessModel>();
				for (final ConsignmentProcessModel consProcessModel : originalOrderConsignment.getConsignmentProcesses())
				{
					snapshotConsignmentProcesses.add(consProcessModel);
				}
				snapshotConsignment.setConsignmentProcesses(snapshotConsignmentProcesses);
			}

			// Copy ConsignmentEntries
			final Set<ConsignmentEntryModel> snapshotConsignmentEntries = new HashSet<ConsignmentEntryModel>();
			for (final ConsignmentEntryModel originalOrderConsignmentEntry : originalOrderConsignment.getConsignmentEntries())
			{
				final ConsignmentEntryModel snapshotConsignmentEntry = getModelService().clone(originalOrderConsignmentEntry);
				final Optional<AbstractOrderEntryModel> optOrderEntry = snapshot.getEntries().stream()
						.filter(
								entry -> entry.getEntryNumber().equals(originalOrderConsignmentEntry.getOrderEntry().getEntryNumber()))
						.findFirst();

				if (!optOrderEntry.isPresent())
				{
					throw new RetryLaterException("Not able to find order entry");
				}
				snapshotConsignmentEntry.setOrderEntry(optOrderEntry.get());
				snapshotConsignmentEntries.add(snapshotConsignmentEntry);
			}
			snapshotConsignment.setConsignmentEntries(snapshotConsignmentEntries);
			snapshotOrderConsignments.add(snapshotConsignment);
		}
		snapshot.setConsignments(snapshotOrderConsignments);

	}

	/**
	 * Update consignments status.
	 *
	 * @param newOrder
	 * 		the new order
	 * @param originalOrder
	 * 		the original order
	 */
	protected void updateConsignmentsStatus(final OrderModel newOrder, final OrderModel originalOrder)
	{
		final Set<ConsignmentModel> newOrderConsignments = newOrder.getConsignments();
		// For all checked-in consignments from original order, copy the same status to new order
		for (final ConsignmentModel consignment : newOrderConsignments)
		{
			final String consignmentCode = consignment.getCode();
			for (final ConsignmentModel origConsignment : originalOrder.getConsignments())
			{
				if (origConsignment.getCode().equals(consignmentCode) && origConsignment.getStatus() == ConsignmentStatus.CHECKED_IN)
				{
					consignment.setStatus(origConsignment.getStatus());
					getModelService().save(consignment);
				}
			}
		}

		// For non checked-in consignments, update the consignment status
		for (final ConsignmentModel consignment : newOrderConsignments)
		{
			if (consignment.getStatus() == ConsignmentStatus.CHECKED_IN)
			{
				continue;
			}
			boolean allEntriesInActive = true;
			for (final ConsignmentEntryModel consignmentEntry : consignment.getConsignmentEntries())
			{
				if (consignmentEntry.getOrderEntry().getActive())
				{
					allEntriesInActive = false;
					break;
				}
			}
			if (allEntriesInActive)
			{
				consignment.setStatus(ConsignmentStatus.CANCELLED);
				getModelService().save(consignment);
			}
		}
	}

	/**
	 * Gets customer account service.
	 *
	 * @return the customerAccountService
	 */
	protected CustomerAccountService getCustomerAccountService()
	{
		return customerAccountService;
	}

	/**
	 * Sets customer account service.
	 *
	 * @param customerAccountService
	 * 		the customerAccountService to set
	 */
	public void setCustomerAccountService(final CustomerAccountService customerAccountService)
	{
		this.customerAccountService = customerAccountService;
	}

	/**
	 * Gets order history service.
	 *
	 * @return the orderHistoryService
	 */
	protected OrderHistoryService getOrderHistoryService()
	{
		return orderHistoryService;
	}

	/**
	 * Sets order history service.
	 *
	 * @param orderHistoryService
	 * 		the orderHistoryService to set
	 */
	public void setOrderHistoryService(final OrderHistoryService orderHistoryService)
	{
		this.orderHistoryService = orderHistoryService;
	}

	/**
	 * Gets time service.
	 *
	 * @return timeService time service
	 */
	protected TimeService getTimeService()
	{
		return timeService;
	}

	/**
	 * Sets time service.
	 *
	 * @param timeService
	 * 		the timeService to set
	 */
	public void setTimeService(final TimeService timeService)
	{
		this.timeService = timeService;
	}

}
