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

package de.hybris.platform.travelstore.setup;

import de.hybris.platform.commerceservices.dataimport.impl.CoreDataImportService;
import de.hybris.platform.commerceservices.dataimport.impl.SampleDataImportService;
import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.commerceservices.setup.data.ImportData;
import de.hybris.platform.commerceservices.setup.events.CoreDataImportedEvent;
import de.hybris.platform.commerceservices.setup.events.SampleDataImportedEvent;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.core.initialization.SystemSetupParameterMethod;
import de.hybris.platform.cronjob.model.CompositeCronJobModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.travelstore.constants.TravelstoreConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * The type Travel store system setup.
 */
@SystemSetup(extension = TravelstoreConstants.EXTENSIONNAME)
public class TravelStoreSystemSetup extends AbstractSystemSetup
{
	private static final String IMPORT_CORE_DATA = "importCoreData";
	private static final String IMPORT_SAMPLE_DATA = "importSampleData";
	private static final String ACTIVATE_SOLR_CRON_JOBS = "activateSolrCronJobs";
	private static final String STORE_SUFFIX = "store";

	private static final String TRAVEL = "travel";
	private static final String AIRLINE = "airline";
	private static final String HOTELS = "hotels";
	private static final String NATUREBAYHOTELS = "naturebayhotels";
	private static final String ALL = "all";
	private static final String NONE = "none";
	private static final String COMMON = "common";

	private static final String SITE_SELECTION_PARAM = "siteSelectionParam";

	private static final String AIRLINE_SAMPLE_DATA = "airlineSampleData";
	private static final String COMPLETE_AIRLINE_DATA = "completeAirlineData";
	private static final String MINIMAL_AIRLINE_DATA = "minimalAirlineData";
	private static final String HOTELS_SAMPLE_DATA = "hotelsSampleData";
	private static final String NATUREBAYHOTELS_SAMPLE_DATA = "naturebayhotelsSampleData";
	private static final String COMPLETE_HOTELS_DATA = "completeHotelsData";
	private static final String MINIMAL_HOTELS_DATA = "minimalHotelsData";
	private static final String NATUREBAYHOTELS_DATA = "naturebayhotelsData";

	private static final String COMPLETE_SET_DIRECTORY = "completeset";
	private static final String MINIMAL_SET_DIRECTORY = "minimalset";

	private CoreDataImportService coreDataImportService;
	private SampleDataImportService sampleDataImportService;
	private ConfigurationService configurationService;

	@Resource(name = "defaultCronJobService")
	private CronJobService cronjobService;

	@SystemSetupParameterMethod
	@Override
	public List<SystemSetupParameter> getInitializationOptions()
	{
		final List<SystemSetupParameter> params = new ArrayList<>();

		params.add(createBooleanSystemSetupParameter(IMPORT_CORE_DATA, "Import Core Data", true));
		params.add(createBooleanSystemSetupParameter(IMPORT_SAMPLE_DATA, "Import Sample Data", true));
		params.add(createBooleanSystemSetupParameter(ACTIVATE_SOLR_CRON_JOBS, "Activate Solr Cron Jobs", true));

		// setup Site selection
		params.add(createSiteSelectionSystemSetupParameter());
		// setup Airline Data
		params.add(createAirlineDataSystemSetupParameter());
		//setup Hotels Data
		params.add(createHotelsDataSystemSetupParameter());
		//setup Nature Bay Hotels Data
		params.add(createNatureBayHotelsDataSystemSetupParameter());

		return params;
	}

	private SystemSetupParameter createSiteSelectionSystemSetupParameter()
	{
		// Determining which site to initialize based on configuration in local.properties
		String configuredSite = getConfigurationService().getConfiguration().getString("travelacc.sites");
		if (StringUtils.isEmpty(configuredSite) || !Arrays.asList(ALL, TRAVEL, AIRLINE, HOTELS, NATUREBAYHOTELS)
				.contains(configuredSite))
		{
			//fallback to default configuration if travelacc.sites is empty or incorrect
			configuredSite = ALL;
		}

		final SystemSetupParameter param = new SystemSetupParameter(SITE_SELECTION_PARAM);
		param.setLabel("Select sites:");
		param.addValue(ALL, StringUtils.equalsIgnoreCase(ALL, configuredSite));
		param.addValue(TRAVEL, StringUtils.equalsIgnoreCase(TRAVEL, configuredSite));
		param.addValue(AIRLINE, StringUtils.equalsIgnoreCase(AIRLINE, configuredSite));
		param.addValue(HOTELS, StringUtils.equalsIgnoreCase(HOTELS, configuredSite));
		param.addValue(NATUREBAYHOTELS, StringUtils.equalsIgnoreCase(NATUREBAYHOTELS, configuredSite));
		return param;
	}

	private SystemSetupParameter createAirlineDataSystemSetupParameter()
	{
		final SystemSetupParameter param = new SystemSetupParameter(AIRLINE_SAMPLE_DATA);
		param.setLabel("Import Airline Data");
		param.addValue(NONE);
		param.addValue(MINIMAL_AIRLINE_DATA, true);
		param.addValue(COMPLETE_AIRLINE_DATA);
		return param;
	}

	private SystemSetupParameter createHotelsDataSystemSetupParameter()
	{
		final SystemSetupParameter param = new SystemSetupParameter(HOTELS_SAMPLE_DATA);
		param.setLabel("Import Hotels Data");
		param.addValue(NONE);
		param.addValue(MINIMAL_HOTELS_DATA, true);
		param.addValue(COMPLETE_HOTELS_DATA);
		return param;
	}

	private SystemSetupParameter createNatureBayHotelsDataSystemSetupParameter()
	{
		final SystemSetupParameter param = new SystemSetupParameter(NATUREBAYHOTELS_SAMPLE_DATA);
		param.setLabel("Import Nature Bay Hotels Data");
		param.addValue(NONE);
		param.addValue(NATUREBAYHOTELS_DATA, true);
		return param;
	}

	/**
	 * This method will be called during the system initialization.
	 *
	 * @param context
	 * 		the context provides the selected parameters and values
	 */
	@SystemSetup(type = SystemSetup.Type.PROJECT, process = SystemSetup.Process.INIT)
	public void createProjectData(final SystemSetupContext context)
	{
		final List<ImportData> importData = new ArrayList<>();

		final List<String> sites = determineImportSites(context);

		if (CollectionUtils.isNotEmpty(CollectionUtils.intersection(sites, Arrays.asList(AIRLINE, HOTELS, TRAVEL, ALL))))
		{
			final List<String> travelSites = sites.stream().filter(siteName -> !siteName.equals(NATUREBAYHOTELS))
					.collect(Collectors.toList());
			importData.add(createImportData(TRAVEL, travelSites));
		}

		if (sites.containsAll(Collections.singletonList(NATUREBAYHOTELS)))
		{
			importData.add(createImportData(NATUREBAYHOTELS, Collections.singletonList(NATUREBAYHOTELS)));
		}

		getCoreDataImportService().execute(this, context, importData);
		getEventService().publishEvent(new CoreDataImportedEvent(context, importData));

		if (CollectionUtils.isNotEmpty(CollectionUtils.intersection(sites, Arrays.asList(AIRLINE, HOTELS, TRAVEL, ALL))))
		{
			importStoreData(context);
			importCommonDataset(context, TRAVEL + STORE_SUFFIX, COMMON, MINIMAL_SET_DIRECTORY);
		}

		// Import Airline store minimal/complete data
		if (sites.contains(AIRLINE) || sites.contains(TRAVEL))
		{
			importAirlineStoreData(context);
		}
		// Import Hotels store minimal/complete data
		if (sites.contains(HOTELS) || sites.contains(TRAVEL))
		{
			importHotelsStoreData(context);
		}
		// Import All store minimal/complete data
		if (sites.contains(TRAVEL))
		{
			importTravelStoreData(context);
		}
		// Import Nature Bay Hotels Chain store minimal/complete data
		if (sites.contains(NATUREBAYHOTELS))
		{
			importImpexFile(context,
					"/travelstore/import/sampledata/productCatalogs/naturebayhotelsProductCatalog/catalog-sync.impex", false);
			importNatureBayHotelsStoreData(context);
		}

		getSampleDataImportService().execute(this, context, importData);

		if (sites.contains(HOTELS) || sites.contains(TRAVEL))
		{
			runCronJobs(Arrays.asList("updateStockLevelsToAccommodationOfferingCronJob", "marketingRatePlanInfoCronjob"));
		}
		if (sites.contains(NATUREBAYHOTELS))
		{
			runCronJobs(Arrays.asList("updateStockLevelsToAccommodationOfferingNBCronJob", "marketingRatePlanInfoNBCronjob"));
		}

		if (sites.contains(AIRLINE) || sites.contains(TRAVEL))
		{
			final CompositeCronJobModel transportOfferingsCompositeJob = (CompositeCronJobModel) cronjobService
					.getCronJob("transportOfferingsCompositeJob");
			cronjobService.performCronJob(transportOfferingsCompositeJob, true);

			final CronJobModel travelGeocodeAddressesCronJob = cronjobService.getCronJob("travelGeocodeAddressesCronJob");
			// Preform cronjob, set latitude/longitude of all POS so that "duration(dynamic attribute of transport offering)"
			// value is

			// set during indexing.
			cronjobService.performCronJob(travelGeocodeAddressesCronJob, true);
		}

		getEventService().publishEvent(new SampleDataImportedEvent(context, importData));
		if (this.getBooleanSystemSetupParameter(context, ACTIVATE_SOLR_CRON_JOBS))
		{
			sites.forEach(site -> {
				this.logInfo(context, String.format("Activating solr index for [%s]", site));
				getSampleDataImportService().runSolrIndex(context.getExtensionName(), site);
			});
		}
	}

	protected ImportData createImportData(final String productCatalogName, final List<String> sites)
	{
		final ImportData importData = new ImportData();
		importData.setProductCatalogName(productCatalogName);
		importData.setContentCatalogNames(sites);
		importData.setStoreNames(sites);
		return importData;
	}

	protected void runCronJobs(final List<String> cronjobs)
	{
		for (final String cronjob : cronjobs)
		{
			final CronJobModel cronJobModel = cronjobService.getCronJob(cronjob);
			cronjobService.performCronJob(cronJobModel, true);
		}
	}

	private void importCommonDataset(final SystemSetupContext context, final String baseImportDirectory, final String storeName,
			final String dataSetDirectory)
	{
		final String importRoot = "/" + baseImportDirectory + "/import/coredata/datasets/";
		importImpexFile(context, importRoot + storeName + "/" + dataSetDirectory + "/identifiers.impex", false);
		importImpexFile(context, importRoot + storeName + "/" + dataSetDirectory + "/locations.impex", false);
		importImpexFile(context, importRoot + storeName + "/" + dataSetDirectory + "/locations-media.impex", false);
		importImpexFile(context, importRoot + storeName + "/" + dataSetDirectory + "/categories.impex", false);
		importImpexFile(context, importRoot + storeName + "/" + dataSetDirectory + "/taxdata.impex", false);
		importImpexFile(context, importRoot + storeName + "/" + dataSetDirectory + "/travelrestrictions.impex", false);
		importImpexFile(context, importRoot + storeName + "/" + dataSetDirectory + "/ancillaryproducts.impex", false);
	}

	private void importAirlineStoreData(final SystemSetupContext context)
	{
		final String parameter = getImportProductDataSystemSetupParameter(context, AIRLINE_SAMPLE_DATA);
		if (StringUtils.equalsIgnoreCase(MINIMAL_AIRLINE_DATA, parameter))
		{
			importImpexData(context, TRAVEL + STORE_SUFFIX, AIRLINE, MINIMAL_SET_DIRECTORY, getAirlineStoreFileNames(Boolean.FALSE));
		}
		else if (StringUtils.equalsIgnoreCase(COMPLETE_AIRLINE_DATA, parameter))
		{
			importImpexData(context, TRAVEL + STORE_SUFFIX, AIRLINE, COMPLETE_SET_DIRECTORY, getAirlineStoreFileNames(Boolean.TRUE));
		}
	}

	private void importHotelsStoreData(final SystemSetupContext context)
	{
		if (StringUtils.equalsIgnoreCase(MINIMAL_HOTELS_DATA,
				getImportProductDataSystemSetupParameter(context, HOTELS_SAMPLE_DATA)))
		{
			importImpexData(context, TRAVEL + STORE_SUFFIX, HOTELS, MINIMAL_SET_DIRECTORY, getHotelsStoreFileNames(Boolean.FALSE));
		}
		else if (StringUtils.equalsIgnoreCase(COMPLETE_HOTELS_DATA,
				getImportProductDataSystemSetupParameter(context, HOTELS_SAMPLE_DATA)))
		{
			importImpexData(context, TRAVEL + STORE_SUFFIX, HOTELS, COMPLETE_SET_DIRECTORY, getHotelsStoreFileNames(Boolean.TRUE));
		}
	}

	private void importNatureBayHotelsStoreData(final SystemSetupContext context)
	{
		importImpexData(context, TRAVEL + STORE_SUFFIX, NATUREBAYHOTELS, null, getNatureBayStoreFileNames());
	}

	private void importTravelStoreData(final SystemSetupContext context)
	{
		importImpexData(context, TRAVEL + STORE_SUFFIX, TRAVEL, null, getTravelStoreFileNames());
	}

	private List<String> determineImportSites(final SystemSetupContext context)
	{
		final String parameterValue = context.getParameter(context.getExtensionName() + "_" + SITE_SELECTION_PARAM);
		if (StringUtils.isNotBlank(parameterValue))
		{
			if (StringUtils.equalsIgnoreCase(parameterValue, ALL))
			{
				return Arrays.asList(AIRLINE, HOTELS, NATUREBAYHOTELS, TRAVEL);
			}
			else
			{
				return Collections.singletonList(parameterValue);
			}
		}
		else
		{
			final String configuredSite = getConfigurationService().getConfiguration().getString("travelacc.sites");
			if (StringUtils.isEmpty(configuredSite) || !Arrays.asList(ALL, TRAVEL, AIRLINE, HOTELS, NATUREBAYHOTELS)
					.contains(configuredSite))
			{
				//fallback to default configuration if travelacc.sites is empty or incorrect
				logInfo(context,
						"Missing setup parameter for key [" + SITE_SELECTION_PARAM + "]. Using default value: [" + ALL + "]");
				return Arrays.asList(AIRLINE, HOTELS, NATUREBAYHOTELS, TRAVEL);
			}

			if (StringUtils.equalsIgnoreCase(configuredSite, ALL))
			{
				return Arrays.asList(AIRLINE, HOTELS, NATUREBAYHOTELS, TRAVEL);
			}
			else
			{
				return Collections.singletonList(configuredSite);
			}
		}
	}

	/**
	 * This method will be called during the system initialization. Imports travel data common to both minimal and complete set.
	 *
	 * @param context
	 * 		the context provides the selected parameters and values
	 */
	private void importStoreData(final SystemSetupContext context)
	{
		importImpexFile(context, "/travelstore/import/sampledata/productCatalogs/travelProductCatalog/catalog-sync.impex", false);
		importImpexFile(context, "/travelstore/import/coredata/common/searchrestriction.impex", false);
	}

	private void importImpexData(final SystemSetupContext context, final String baseImportDirectory, final String storeName,
			final String dataSetDirectory, final List<String> fileNames)
	{
		logInfo(context, "Begin importing dataset for store [" + storeName + "]");
		final String importRoot = "/" + baseImportDirectory + "/import/coredata/datasets/";

		fileNames.forEach(file -> importImpexFile(context,
				importRoot + storeName + "/" + (StringUtils.isNotBlank(dataSetDirectory) ? dataSetDirectory + "/" : "") + file,
				false));
	}

	private List<String> getAirlineStoreFileNames(final boolean isCompleteSet)
	{
		final List<String> fileNames = new ArrayList<>(30);
		fileNames.add("transportfacility.impex");
		fileNames.add("travelsector.impex");
		fileNames.add("travelroute.impex");
		fileNames.add("categories.impex");
		fileNames.add("fareproductsdata.impex");
		fileNames.add("seat_accommodationproduct.impex");
		fileNames.add("travelrestrictions.impex");
		fileNames.add("ancillaryproducts.impex");
		fileNames.add("feeproducts.impex");
		fileNames.add("bundlesdata.impex");
		fileNames.add("bundletemplates-pricerules.impex");
		fileNames.add("bundletemplatetransportofferingmapping.impex");
		fileNames.add("bundletemplates-selectioncriteria.impex");
		fileNames.add("pricesdata.impex");
		fileNames.add("b2bpricesdata.impex");
		fileNames.add("activities.impex");
		fileNames.add("aircraftinfo.impex");
		fileNames.add("transportvehicle.impex");
		fileNames.add("accommodationmap.impex");
		fileNames.add("transportvehicleconfigurationmapping.impex");
		fileNames.add("seatmap.impex");
		fileNames.add("specialservicerequest.impex");
		fileNames.add("stock.impex");
		fileNames.add("products-relations.impex");
		fileNames.add("terminal.impex");
		fileNames.add("transportfacilityterminalmapping.impex");

		if (isCompleteSet)
		{
			fileNames.add("scheduleconfiguration_complete_popular.impex");
			fileNames.add("scheduleconfiguration_complete_nonpopular.impex");
		}
		else
		{
			fileNames.add("scheduleconfiguration.impex");
		}
		return fileNames;
	}

	private List<String> getHotelsStoreFileNames(final boolean isCompleteSet)
	{
		final List<String> fileNames = new ArrayList<>(30);
		fileNames.add("propertyfacilitytypes.impex");
		fileNames.add("propertyfacilities.impex");
		fileNames.add("accommodationfacilitytypes.impex");
		fileNames.add("accommodationfacilities.impex");
		fileNames.add("accommodationproviders.impex");
		fileNames.add("locations.impex");
		fileNames.add("points-of-service.impex");
		fileNames.add("accommodationofferings.impex");
		fileNames.add("accommodationofferings-media.impex");
		fileNames.add("guestoccupancies.impex");
		fileNames.add("categories.impex");
		fileNames.add("accommodations.impex");
		fileNames.add("accommodations-media.impex");
		fileNames.add("cancelpenalties.impex");
		fileNames.add("guarantees.impex");
		fileNames.add("mealtypes.impex");
		fileNames.add("rateplans.impex");
		fileNames.add("rateplaninclusions.impex");
		fileNames.add("roomrateproduct.impex");
		fileNames.add("accommodationrestrictions.impex");
		fileNames.add("serviceproducts.impex");
		fileNames.add("serviceproducts-media.impex");
		fileNames.add("pricerows.impex");
		fileNames.add("rateplanaccommodationmapping.impex");
		fileNames.add("users.impex");
		fileNames.add("accommodationofferingcustomerreview.impex");
		fileNames.add("roompreference.impex");
		fileNames.add("taxgroups.impex");
		fileNames.add("productstaxes.impex");
		fileNames.add("optimizedaccommodationofferings.impex");

		if (isCompleteSet)
		{
			//Any File to be included during Complete Set should be added here.
		}

		return fileNames;
	}

	private List<String> getNatureBayStoreFileNames()
	{
		final List<String> fileNames = new ArrayList<>(30);
		fileNames.add("locations.impex");
		fileNames.add("locations-media.impex");
		fileNames.add("accommodationchains.impex");
		fileNames.add("accommodationproviders.impex");
		fileNames.add("propertyfacilitytypes.impex");
		fileNames.add("propertyfacilities.impex");
		fileNames.add("accommodationfacilitytypes.impex");
		fileNames.add("accommodationfacilities.impex");
		fileNames.add("accommodationofferings-locations.impex");
		fileNames.add("points-of-service.impex");
		fileNames.add("accommodationofferings.impex");
		fileNames.add("accommodationofferings-media.impex");
		fileNames.add("guestoccupancies.impex");
		fileNames.add("categories.impex");
		fileNames.add("taxgroups.impex");
		fileNames.add("accommodations.impex");
		fileNames.add("accommodations-media.impex");
		fileNames.add("cancelpenalties.impex");
		fileNames.add("guarantees.impex");
		fileNames.add("mealtypes.impex");
		fileNames.add("rateplans.impex");
		fileNames.add("rateplaninclusions.impex");
		fileNames.add("roomrateproduct.impex");
		fileNames.add("accommodationrestrictions.impex");
		fileNames.add("serviceproducts.impex");
		fileNames.add("serviceproducts-media.impex");
		fileNames.add("pricerows.impex");
		fileNames.add("rateplanaccommodationmapping.impex");
		fileNames.add("users.impex");
		fileNames.add("accommodationofferingcustomerreview.impex");
		fileNames.add("roompreference.impex");
		fileNames.add("productstaxes.impex");
		fileNames.add("optimizedaccommodationofferings.impex");

		return fileNames;
	}

	private List<String> getTravelStoreFileNames()
	{
		final List<String> fileNames = new ArrayList<>(30);
		fileNames.add("standardproducts.impex");
		fileNames.add("pricerows.impex");
		fileNames.add("bundlesdata.impex");
		fileNames.add("bundletemplates-pricerules.impex");
		fileNames.add("bundletemplates-selectioncriteria.impex");

		return fileNames;
	}

	/**
	 * Retrieves import product data parameter from context
	 *
	 * @param context
	 * 		the context
	 * @return import product data parameter
	 */
	protected String getImportProductDataSystemSetupParameter(final SystemSetupContext context, final String setupParameter)
	{
		final String parameterValue = context.getParameter(context.getExtensionName() + "_" + setupParameter);
		if (StringUtils.isNotBlank(parameterValue))
		{
			return parameterValue;
		}
		else
		{
			final String defaultValue = StringUtils.equalsIgnoreCase(setupParameter, AIRLINE_SAMPLE_DATA) ? MINIMAL_AIRLINE_DATA
					: MINIMAL_HOTELS_DATA;
			logInfo(context,
					"Missing setup parameter for key [" + setupParameter + "]. Using default value: [" + defaultValue + "]");
			return defaultValue;
		}
	}

	/**
	 * @return the coreDataImportService
	 */
	public CoreDataImportService getCoreDataImportService()
	{
		return coreDataImportService;
	}

	/**
	 * @param coreDataImportService
	 * 		the coreDataImportService to set
	 */
	@Required
	public void setCoreDataImportService(final CoreDataImportService coreDataImportService)
	{
		this.coreDataImportService = coreDataImportService;
	}

	/**
	 * @return the sampleDataImportService
	 */
	protected SampleDataImportService getSampleDataImportService()
	{
		return sampleDataImportService;
	}

	/**
	 * @param sampleDataImportService
	 * 		the sampleDataImportService to set
	 */
	@Required
	public void setSampleDataImportService(final SampleDataImportService sampleDataImportService)
	{
		this.sampleDataImportService = sampleDataImportService;
	}

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
