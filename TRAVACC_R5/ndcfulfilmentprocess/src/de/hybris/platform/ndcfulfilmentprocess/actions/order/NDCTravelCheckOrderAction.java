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

package de.hybris.platform.ndcfulfilmentprocess.actions.order;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.processengine.action.AbstractAction;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.travelfulfilmentprocess.TravelCheckOrderService;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Checks the validity of order and returns a decision based on whether it is amendment journey or not
 */
public class NDCTravelCheckOrderAction extends AbstractAction<OrderProcessModel>
{
	private static final Logger LOG = Logger.getLogger(NDCTravelCheckOrderAction.class);

	private TravelCheckOrderService travelCheckOrderService;

	@Override
	public String execute(final OrderProcessModel process) throws RetryLaterException, Exception
	{
		return executeAction(process).toString();
	}

	/**
	 * Execute action transition.
	 *
	 * @param process
	 * 		the process
	 *
	 * @return the transition
	 */
	protected Transition executeAction(final OrderProcessModel process)
	{
		final OrderModel order = process.getOrder();

		if (order == null)
		{
			LOG.error("Missing the order, exiting the process");
			return Transition.NOK;
		}

		if (getTravelCheckOrderService().check(order))
		{
			setOrderStatus(order, OrderStatus.CHECKED_VALID);
			return !isPayLater(order) && isPaymentRequired(order) ? Transition.OK : Transition.NO_PAYMENT;
		}
		else
		{
			setOrderStatus(order, OrderStatus.CHECKED_INVALID);
			return Transition.NOK;
		}
	}

	/**
	 * Is pay later boolean.
	 *
	 * @param order
	 * 		the order
	 *
	 * @return the boolean
	 */
	protected boolean isPayLater(final OrderModel order)
	{
		final List<PaymentTransactionModel> paymentTransactions = order.getPaymentTransactions();

		return paymentTransactions.stream()
				.flatMap(paymentTransactionModel -> paymentTransactionModel.getEntries().stream()).allMatch(
						paymentTransactionEntryModels -> PaymentTransactionType.PAY_LATER
								.equals(paymentTransactionEntryModels.getType()));
	}

	/**
	 * Is payment required boolean.
	 *
	 * @param order
	 * 		the order
	 *
	 * @return the boolean
	 */
	protected boolean isPaymentRequired(final OrderModel order)
	{
		final List<PaymentTransactionModel> paymentTransactions = order.getPaymentTransactions();
		final Map<PaymentTransactionType, List<PaymentTransactionEntryModel>> paymentTransactionEntries = paymentTransactions
				.stream().flatMap(transaction -> transaction.getEntries().stream())
				.collect(Collectors.groupingBy(PaymentTransactionEntryModel::getType));
		BigDecimal amountToPay = BigDecimal.ZERO;
		amountToPay = amountToPay.add(paymentTransactionEntries.get(PaymentTransactionType.AUTHORIZATION).stream()
				.map(PaymentTransactionEntryModel::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
		if (CollectionUtils.isNotEmpty(paymentTransactionEntries.get(PaymentTransactionType.CAPTURE)))
		{
			amountToPay = amountToPay.subtract(paymentTransactionEntries.get(PaymentTransactionType.CAPTURE).stream()
					.map(PaymentTransactionEntryModel::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
		}
		return amountToPay.doubleValue() > 0;
	}

	/**
	 * Sets travel check order service.
	 *
	 * @param travelCheckOrderService
	 * 		the travel check order service
	 */
	@Required
	public void setTravelCheckOrderService(final TravelCheckOrderService travelCheckOrderService)
	{
		this.travelCheckOrderService = travelCheckOrderService;
	}

	/**
	 * The enum Transition.
	 */
	public enum Transition
	{
		OK, NO_PAYMENT, NOK;

		/**
		 * Gets string values.
		 *
		 * @return the string values
		 */
		public static Set<String> getStringValues()
		{
			final Set<String> res = new HashSet<String>();

			for (final Transition t : Transition.values())
			{
				res.add(t.toString());
			}
			return res;
		}
	}

	@Override
	public Set<String> getTransitions()
	{
		return Transition.getStringValues();
	}

	/**
	 * Gets travel check order service.
	 *
	 * @return the travel check order service
	 */
	protected TravelCheckOrderService getTravelCheckOrderService()
	{
		return travelCheckOrderService;
	}

}
