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

package de.hybris.platform.travelbackoffice.setup;

import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.core.initialization.SystemSetupParameterMethod;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.travelbackoffice.constants.TravelbackofficeConstants;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;


/**
 * Class handling initialization and update data setup for Travelbackoffice extension
 */
@SystemSetup(extension = TravelbackofficeConstants.EXTENSIONNAME)
public class TravelbackofficeSystemSetup extends AbstractSystemSetup
{
	private static final Logger LOG = Logger.getLogger(TravelbackofficeSystemSetup.class);
	private static final String POPULATE_TRANSPORT_OFFERING_LOCATIONS = "populateTransportOfferingLocations";
	private static final String CREATE_SCHEDULE_CONFIGURATIONS = "createScheduleConfigurations";

	@Resource(name = "defaultCronJobService")
	private CronJobService cronjobService;

	@SystemSetupParameterMethod
	@Override
	public List<SystemSetupParameter> getInitializationOptions()
	{
		final List<SystemSetupParameter> params = new ArrayList<SystemSetupParameter>();

		params.add(createBooleanSystemSetupParameter(POPULATE_TRANSPORT_OFFERING_LOCATIONS, "Populate Locations", true));
		params.add(createBooleanSystemSetupParameter(CREATE_SCHEDULE_CONFIGURATIONS, "Create Schedule Configurations", true));

		return params;
	}

	/**
	 * This method will be called during the system initialization.
	 *
	 * @param context
	 * 		the context provides the selected parameters and values
	 */
	@SystemSetup(type = SystemSetup.Type.PROJECT, process = SystemSetup.Process.INIT)
	public void createData(final SystemSetupContext context)
	{
		importBackofficeData(context);

		final boolean populateLocations = this.getBooleanSystemSetupParameter(context, POPULATE_TRANSPORT_OFFERING_LOCATIONS);
		if (populateLocations)
		{
			LOG.info("Updating Transport Offerings with origin and destination locations");
			final CronJobModel updateLocationsToTransportOfferingJob = cronjobService
					.getCronJob("updateLocationsToTransportOfferingJob");
			cronjobService.performCronJob(updateLocationsToTransportOfferingJob, true);
		}

		final boolean createConfigurations = this.getBooleanSystemSetupParameter(context, CREATE_SCHEDULE_CONFIGURATIONS);
		if (createConfigurations)
		{
			LOG.info("Creating schedule configurations from a transport offerings pool");
			cronjobService.performCronJob(cronjobService.getCronJob("manageScheduleConfigurationsJob"), true);
		}
	}

	protected void importBackofficeData(final SystemSetupContext context)
	{
		importImpexFile(context, "/impex/jobs.impex", false);
	}
}
