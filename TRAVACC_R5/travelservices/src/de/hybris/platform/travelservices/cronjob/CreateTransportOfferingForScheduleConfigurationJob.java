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

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.travelservices.dao.ScheduleConfigurationDao;
import de.hybris.platform.travelservices.model.cronjob.CreateTransportOfferingForScheduleConfigurationJobModel;
import de.hybris.platform.travelservices.model.travel.ScheduleConfigurationModel;
import de.hybris.platform.travelservices.model.warehouse.TransportOfferingModel;
import de.hybris.platform.travelservices.services.TransportOfferingService;

import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * This class will get the schedule configurations and create the transport offerings for all schedule congifurations
 */
public class CreateTransportOfferingForScheduleConfigurationJob
		extends AbstractJobPerformable<CreateTransportOfferingForScheduleConfigurationJobModel>
{
	private static final Logger LOG = Logger.getLogger(CreateTransportOfferingForScheduleConfigurationJob.class);
	private ScheduleConfigurationDao scheduleConfigurationDao;
	private TimeService timeService;
	private TransportOfferingService transportOfferingService;

	@Override
	public PerformResult perform(final CreateTransportOfferingForScheduleConfigurationJobModel scheduleConfigurationJobModel)
	{
		LOG.info("Start performing CreateTransportOfferingForScheduleConfigurationJob...");

		final List<ScheduleConfigurationModel> scheduleConfigurationList = getScheduleConfigurationDao()
				.findAllScheduleConfigurations();

		scheduleConfigurationList.forEach(scheduleConfiguration -> {
			if (CollectionUtils.isNotEmpty(scheduleConfiguration.getTransportOfferings()))
			{
				getModelService().removeAll(scheduleConfiguration.getTransportOfferings());
			}

			final List<TransportOfferingModel> transportOfferingModels = getTransportOfferingService()
					.createTransportOfferingForScheduleConfiguration(scheduleConfiguration);

			getModelService().saveAll(transportOfferingModels);
		});

		LOG.info("CreateTransportOfferingForScheduleConfigurationJob completed.");
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	/**
	 * Gets model service.
	 *
	 * @return the model service
	 */
	protected ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * Gets schedule configuration dao.
	 *
	 * @return schedule configuration dao
	 */
	protected ScheduleConfigurationDao getScheduleConfigurationDao()
	{
		return scheduleConfigurationDao;
	}

	/**
	 * Sets schedule configuration dao.
	 *
	 * @param scheduleConfigurationDao
	 * 		the schedule configuration dao
	 */
	@Required
	public void setScheduleConfigurationDao(final ScheduleConfigurationDao scheduleConfigurationDao)
	{
		this.scheduleConfigurationDao = scheduleConfigurationDao;
	}

	/**
	 * Gets time service.
	 *
	 * @return time service
	 */
	protected TimeService getTimeService()
	{
		return timeService;
	}

	/**
	 * Sets time service.
	 *
	 * @param timeService
	 * 		the time service
	 */
	@Required
	public void setTimeService(final TimeService timeService)
	{
		this.timeService = timeService;
	}

	/**
	 * Gets transport offering service.
	 *
	 * @return transport offering service
	 */
	protected TransportOfferingService getTransportOfferingService()
	{
		return transportOfferingService;
	}

	/**
	 * Sets transport offering service.
	 *
	 * @param transportOfferingService
	 * 		the transport offering service
	 */
	@Required
	public void setTransportOfferingService(final TransportOfferingService transportOfferingService)
	{
		this.transportOfferingService = transportOfferingService;
	}
}
