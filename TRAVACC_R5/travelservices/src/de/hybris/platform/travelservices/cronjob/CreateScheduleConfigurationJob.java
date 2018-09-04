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
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.travelservices.dao.ScheduleConfigurationDao;
import de.hybris.platform.travelservices.model.cronjob.CreateScheduleConfigurationJobModel;
import de.hybris.platform.travelservices.model.travel.ScheduleConfigurationModel;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * This class get all the schedule configurations and modify each with the correct start and end dates
 */
public class CreateScheduleConfigurationJob extends AbstractJobPerformable<CreateScheduleConfigurationJobModel>
{
	private static final Logger LOG = Logger.getLogger(CreateScheduleConfigurationJob.class);
	protected static final String MAXIMUM_SCHEDULE_NUMBER_OF_DAYS = "maximum.schedule.number.of.days";
	protected static final int DEFAULT_MAX_SCHEDULE_DAYS = 30;

	private ScheduleConfigurationDao scheduleConfigurationDao;
	private TimeService timeService;
	private ConfigurationService configurationService;

	@Override
	public PerformResult perform(final CreateScheduleConfigurationJobModel scheduleConfigurationJobModel)
	{
		LOG.info("Start performing CreateScheduleConfigurationJob...");

		final List<ScheduleConfigurationModel> scheduleConfigurationList = getScheduleConfigurationDao()
				.findAllScheduleConfigurations();

		final int maxScheduleDays = getConfigurationService().getConfiguration()
				.getInt(MAXIMUM_SCHEDULE_NUMBER_OF_DAYS, DEFAULT_MAX_SCHEDULE_DAYS);

		final Date startDate = getTimeService().getCurrentTime();
		final Date endDate = DateUtils.addDays(startDate, maxScheduleDays);
		
		scheduleConfigurationList.forEach(scheduleConfiguration -> {
			scheduleConfiguration.setStartDate(startDate);
			scheduleConfiguration.setEndDate(endDate);
			getModelService().save(scheduleConfiguration);
		});

		LOG.info("CreateScheduleConfigurationJob completed.");
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
	 * Gets configuration service.
	 *
	 * @return configuration service
	 */
	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	@Required
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}
}
