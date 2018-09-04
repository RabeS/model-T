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
package de.hybris.platform.travelfulfilmentprocess.actions.order;

import de.hybris.platform.basecommerce.enums.FraudStatus;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.fraud.FraudService;
import de.hybris.platform.fraud.impl.FraudServiceResponse;
import de.hybris.platform.fraud.model.FraudReportModel;
import de.hybris.platform.orderhistory.model.OrderHistoryEntryModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.util.Config;
import de.hybris.platform.travelfulfilmentprocess.constants.TravelfulfilmentprocessConstants;

import org.springframework.beans.factory.annotation.Required;


/**
 * The type Fraud check order internal action.
 */
public class FraudCheckOrderInternalAction extends AbstractFraudCheckAction<OrderProcessModel>
{
	private FraudService fraudService;
	private String providerName;

	/**
	 * Gets fraud service.
	 *
	 * @return the fraud service
	 */
	protected FraudService getFraudService()
	{
		return fraudService;
	}

	/**
	 * Sets fraud service.
	 *
	 * @param fraudService
	 * 		the fraud service
	 */
	@Required
	public void setFraudService(final FraudService fraudService)
	{
		this.fraudService = fraudService;
	}

	/**
	 * Gets provider name.
	 *
	 * @return the provider name
	 */
	protected String getProviderName()
	{
		return providerName;
	}

	/**
	 * Sets provider name.
	 *
	 * @param providerName
	 * 		the provider name
	 */
	public void setProviderName(final String providerName)
	{
		this.providerName = providerName;
	}

	@Override
	public Transition executeAction(final OrderProcessModel process)//NOPMD
	{
		ServicesUtil.validateParameterNotNull(process, "Process can not be null");
		ServicesUtil.validateParameterNotNull(process.getOrder(), "Order can not be null");

		//get the fraud-detection configuration
		final double scoreLimit = Double.parseDouble(Config.getParameter(TravelfulfilmentprocessConstants.EXTENSIONNAME + ".fraud.scoreLimit"));
		final double scoreTolerance = Double.parseDouble(Config.getParameter(TravelfulfilmentprocessConstants.EXTENSIONNAME + ".fraud.scoreTolerance"));

		final OrderModel order = process.getOrder();
		final FraudServiceResponse response = getFraudService().recognizeOrderSymptoms(getProviderName(), order);

		final double score = response.getScore();
		if (score < scoreLimit)
		{
			final FraudReportModel fraudReport = createFraudReport(providerName, response, order, FraudStatus.OK);
			final OrderHistoryEntryModel historyEntry = createHistoryLog(providerName, order, FraudStatus.OK, null);
			order.setFraudulent(Boolean.FALSE);
			order.setPotentiallyFraudulent(Boolean.FALSE);
			order.setStatus(OrderStatus.FRAUD_CHECKED);
			modelService.save(fraudReport);
			modelService.save(historyEntry);
			modelService.save(order);
			return Transition.OK;
		}
		else if (score < scoreLimit + scoreTolerance)
		{
			final FraudReportModel fraudReport = createFraudReport(providerName, response, order, FraudStatus.CHECK);
			final OrderHistoryEntryModel historyEntry = createHistoryLog(providerName, order, FraudStatus.CHECK,
					fraudReport.getCode());
			order.setFraudulent(Boolean.FALSE);
			order.setPotentiallyFraudulent(Boolean.TRUE);
			order.setStatus(OrderStatus.FRAUD_CHECKED);
			modelService.save(fraudReport);
			modelService.save(historyEntry);
			modelService.save(order);
			return Transition.POTENTIAL;
		}
		else
		{
			final FraudReportModel fraudReport = createFraudReport(providerName, response, order, FraudStatus.FRAUD);
			final OrderHistoryEntryModel historyEntry = createHistoryLog(providerName, order, FraudStatus.FRAUD,
					fraudReport.getCode());
			order.setFraudulent(Boolean.TRUE);
			order.setPotentiallyFraudulent(Boolean.FALSE);
			order.setStatus(OrderStatus.FRAUD_CHECKED);
			modelService.save(fraudReport);
			modelService.save(historyEntry);
			modelService.save(order);
			return Transition.FRAUD;
		}
	}
}