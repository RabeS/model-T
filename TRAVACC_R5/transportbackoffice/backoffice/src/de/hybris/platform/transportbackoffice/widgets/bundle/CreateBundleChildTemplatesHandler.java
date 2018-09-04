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

package de.hybris.platform.transportbackoffice.widgets.bundle;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.configurablebundleservices.model.AutoPickBundleSelectionCriteriaModel;
import de.hybris.platform.configurablebundleservices.model.BundleSelectionCriteriaModel;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateStatusModel;
import de.hybris.platform.configurablebundleservices.model.PickExactlyNBundleSelectionCriteriaModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.travelservices.bundle.TravelBundleTemplateService;
import de.hybris.platform.travelservices.model.travel.BundleTemplateTransportOfferingMappingModel;
import de.hybris.platform.travelservices.services.TravelBundleTemplateStatusService;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Messagebox;

import com.hybris.cockpitng.config.jaxb.wizard.CustomType;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandler;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandlerAdapter;


/**
 * Custom Handler for the custom navigation action defined in the Bundle Wizard. This handler sets the catalog version of the
 * Parent Bundle in the child bundles
 */
public class CreateBundleChildTemplatesHandler implements FlowActionHandler
{
	private static final Logger LOG = Logger.getLogger(CreateBundleChildTemplatesHandler.class);

	protected static final String CREATE_BUNDLE_WIZARD_ERROR_TITLE = "create.bundle.wizard.second.step.error.title";
	protected static final String CREATE_BUNDLE_WIZARD_MANDATORY_FIELD_ERROR_MESSAGE = "create.bundle.wizard.mandatory.field"
			+ ".error.message";
	protected static final String CREATE_BUNDLE_WIZARD_DUPLICATE_ID_ERROR_MESSAGE = "create.bundle.wizard.duplicate.id.error"
			+ ".message";

	protected static final String CREATE_BUNDLE_TEMPLATE_ITEM_ID = "newBundleTemplate";
	protected static final String CREATE_FARE_BUNDLE_TEMPLATE_ITEM_ID = "fareBundleTemplate";
	protected static final String CREATE_ANCILLARY_BUNDLE_TEMPLATE_ITEM_ID = "ancillaryBundleTemplate";
	protected static final String BUNDLE_TEMPLATE_TRANSPORT_OFFERING_MAPPING_ID = "mapping";

	protected static final String FARE_PRODUCT_BUNDLE_ID = "FareProduct";
	protected static final String ANCILLARY_PRODUCT_BUNDLE_ID = "AncillaryProduct";
	protected static final String BUNDLE_MAPPING_ID = "BundleMapping";
	protected static final String DEFAULT_STATUS = "Approved_Status";
	protected static final String PICK_EXACTLY_ONE_ID = "_PickExactlyOne";
	protected static final String AUTO_PICK_BUNDLE_SELECTION_ID = "_AutoPickBundleSelection";
	protected static final String BUNDLE_TYPE_NAME_ID = "bundleTypeName";
	protected static final int DEFAULT_N_VALUE = 1;

	private TravelBundleTemplateService travelBundleTemplateService;
	private TravelBundleTemplateStatusService travelBundleTemplateStatusService;
	private ModelService modelService;
	private EnumerationService enumerationService;

	@Override
	public void perform(final CustomType customType, final FlowActionHandlerAdapter flowActionHandlerAdapter,
			final Map<String, String> map)
	{
		final BundleTemplateModel parentBundleTemplate = flowActionHandlerAdapter.getWidgetInstanceManager().getModel()
				.getValue(CREATE_BUNDLE_TEMPLATE_ITEM_ID, BundleTemplateModel.class);
		final BundleTemplateModel fareBundleTemplate = flowActionHandlerAdapter.getWidgetInstanceManager().getModel()
				.getValue(CREATE_FARE_BUNDLE_TEMPLATE_ITEM_ID, BundleTemplateModel.class);
		final BundleTemplateModel ancillaryBundleTemplate = flowActionHandlerAdapter.getWidgetInstanceManager().getModel()
				.getValue(CREATE_ANCILLARY_BUNDLE_TEMPLATE_ITEM_ID, BundleTemplateModel.class);

		final BundleTemplateTransportOfferingMappingModel mapping = flowActionHandlerAdapter.getWidgetInstanceManager().getModel()
				.getValue(BUNDLE_TEMPLATE_TRANSPORT_OFFERING_MAPPING_ID, BundleTemplateTransportOfferingMappingModel.class);

		if (Objects.isNull(parentBundleTemplate) || Objects.isNull(fareBundleTemplate) || Objects.isNull(ancillaryBundleTemplate))
		{
			Messagebox.show(Labels.getLabel(CREATE_BUNDLE_WIZARD_MANDATORY_FIELD_ERROR_MESSAGE),
					Labels.getLabel(CREATE_BUNDLE_WIZARD_ERROR_TITLE), Messagebox.OK, Messagebox.EXCLAMATION);
			return;
		}

		if (!validateUniqueBundleId(parentBundleTemplate.getId(), parentBundleTemplate.getVersion()))
		{
			Messagebox.show(Labels.getLabel(CREATE_BUNDLE_WIZARD_DUPLICATE_ID_ERROR_MESSAGE),
					Labels.getLabel(CREATE_BUNDLE_WIZARD_ERROR_TITLE), Messagebox.OK, Messagebox.EXCLAMATION);
			return;
		}

		final CatalogVersionModel catalogVersion = parentBundleTemplate.getCatalogVersion();
		final String bundleTemplateId = parentBundleTemplate.getId();
		final String bundleTemplateVersion = parentBundleTemplate.getVersion();
		final BundleTemplateStatusModel status = getBundleTemplateStatus();

		fareBundleTemplate.setCatalogVersion(catalogVersion);
		fareBundleTemplate.setId(getFareBundleTemplateId(bundleTemplateId));
		fareBundleTemplate.setVersion(bundleTemplateVersion);
		fareBundleTemplate.setStatus(status);
		fareBundleTemplate.setBundleSelectionCriteria(getFareProductBundleSelectionCriteria(catalogVersion, bundleTemplateId));

		ancillaryBundleTemplate.setCatalogVersion(catalogVersion);
		ancillaryBundleTemplate.setId(getAncillaryBundleTemplateId(bundleTemplateId));
		ancillaryBundleTemplate.setVersion(bundleTemplateVersion);
		ancillaryBundleTemplate.setStatus(status);
		ancillaryBundleTemplate.setBundleSelectionCriteria(
				getAncillaryProductBundleSelectionCriteria(catalogVersion, bundleTemplateId));

		parentBundleTemplate.setChildTemplates(Stream.of(fareBundleTemplate, ancillaryBundleTemplate).collect(Collectors.toList()));
		parentBundleTemplate.setStatus(status);

		mapping.setCatalogVersion(catalogVersion);
		mapping.setCode(getBundleTemplateMappingCode(bundleTemplateId));
		mapping.setBundleTemplate(parentBundleTemplate);
		flowActionHandlerAdapter.getWidgetInstanceManager().getModel()
				.put(BUNDLE_TYPE_NAME_ID, getEnumerationService().getEnumerationName(parentBundleTemplate.getType()));
		flowActionHandlerAdapter.next();
	}

	/**
	 * Validate the bundleTemplateId that should be unique for the given catalog.
	 *
	 * @param bundleTemplateId
	 * 		as the bundle template id
	 * @param version
	 * 		as the version
	 *
	 * @return true if the id is valid, false otherwise
	 */
	protected boolean validateUniqueBundleId(final String bundleTemplateId, final String version)
	{
		Boolean result = Boolean.FALSE;
		try
		{
			getTravelBundleTemplateService().getBundleTemplateForCode(bundleTemplateId, version);
		}
		catch (final ModelNotFoundException ex)
		{
			LOG.debug(ex);
			result = Boolean.TRUE;
		}
		catch (final AmbiguousIdentifierException ex)
		{
			LOG.debug(ex);
		}

		return result;
	}

	/**
	 * Returns the bundle template status model
	 *
	 * @return bundle template status model
	 */
	protected BundleTemplateStatusModel getBundleTemplateStatus()
	{
		return getTravelBundleTemplateStatusService().getBundleTemplateStatusForId(DEFAULT_STATUS);
	}

	/**
	 * Returns the id of the Fare Product Bundle Template. The id is build as a concat of the {#FARE_PRODUCT_BUNDLE_ID} constant
	 * and the id of the parentBundleTemplate id.
	 *
	 * @param bundleTemplateId
	 * 		the bundle template id
	 *
	 * @return a string representing the id of the Fare Product Bundle Template
	 */
	protected String getFareBundleTemplateId(final String bundleTemplateId)
	{
		return FARE_PRODUCT_BUNDLE_ID + bundleTemplateId;
	}

	/**
	 * Returns the id of the Ancillary Product Bundle Template. The id is build as a concat of the {#ANCILLARY_PRODUCT_BUNDLE_ID}
	 * constant and the id of the parentBundleTemplate id.
	 *
	 * @param bundleTemplateId
	 * 		the bundle template id
	 *
	 * @return a string representing the id of the Ancillary Product Bundle Template
	 */
	protected String getAncillaryBundleTemplateId(final String bundleTemplateId)
	{
		return ANCILLARY_PRODUCT_BUNDLE_ID + bundleTemplateId;
	}

	/**
	 * Returns the id of the Bundle Template Transport Offering Mapping. The id is build as a concat of the given id and the
	 * {#BUNDLE_MAPPING_ID} constant.
	 *
	 * @param bundleTemplateId
	 * 		the bundle template id
	 *
	 * @return a string representing the id of the Ancillary Product Bundle Template
	 */
	protected String getBundleTemplateMappingCode(final String bundleTemplateId)
	{
		return bundleTemplateId + BUNDLE_MAPPING_ID;
	}

	/**
	 * Returns the {@link BundleSelectionCriteriaModel} instance of {@link PickExactlyNBundleSelectionCriteriaModel} for the Fare
	 * product bundle template.
	 *
	 * @param catalogVersion
	 * 		as the catalog version
	 * @param bundleTemplateId
	 * 		as the bundle template id
	 *
	 * @return the bundle selection criteria model
	 */
	protected BundleSelectionCriteriaModel getFareProductBundleSelectionCriteria(final CatalogVersionModel catalogVersion,
			final String bundleTemplateId)
	{
		final PickExactlyNBundleSelectionCriteriaModel pickExactlyNBundleSelectionCriteria = getModelService()
				.create(PickExactlyNBundleSelectionCriteriaModel.class);
		pickExactlyNBundleSelectionCriteria.setId(FARE_PRODUCT_BUNDLE_ID + bundleTemplateId + PICK_EXACTLY_ONE_ID);
		pickExactlyNBundleSelectionCriteria.setCatalogVersion(catalogVersion);
		pickExactlyNBundleSelectionCriteria.setN(DEFAULT_N_VALUE);
		return pickExactlyNBundleSelectionCriteria;
	}

	/**
	 * Returns the {@link BundleSelectionCriteriaModel} instance of {@link AutoPickBundleSelectionCriteriaModel} for the Ancillary
	 * product bundle template.
	 *
	 * @param catalogVersion
	 * 		as the catalog version
	 * @param bundleTemplateId
	 * 		as the bundle template id
	 *
	 * @return the bundle selection criteria model
	 */
	protected BundleSelectionCriteriaModel getAncillaryProductBundleSelectionCriteria(final CatalogVersionModel catalogVersion,
			final String bundleTemplateId)
	{
		final AutoPickBundleSelectionCriteriaModel autoPickBundleSelectionCriteria = getModelService()
				.create(AutoPickBundleSelectionCriteriaModel.class);
		autoPickBundleSelectionCriteria.setId(ANCILLARY_PRODUCT_BUNDLE_ID + bundleTemplateId + AUTO_PICK_BUNDLE_SELECTION_ID);
		autoPickBundleSelectionCriteria.setCatalogVersion(catalogVersion);
		return autoPickBundleSelectionCriteria;
	}

	/**
	 * Gets travel bundle template service.
	 *
	 * @return the travel bundle template service
	 */
	protected TravelBundleTemplateService getTravelBundleTemplateService()
	{
		return travelBundleTemplateService;
	}

	/**
	 * Sets travel bundle template service.
	 *
	 * @param travelBundleTemplateService
	 * 		the travel bundle template service
	 */
	@Required
	public void setTravelBundleTemplateService(final TravelBundleTemplateService travelBundleTemplateService)
	{
		this.travelBundleTemplateService = travelBundleTemplateService;
	}

	/**
	 * Gets travel bundle template status service.
	 *
	 * @return the travel bundle template status service
	 */
	protected TravelBundleTemplateStatusService getTravelBundleTemplateStatusService()
	{
		return travelBundleTemplateStatusService;
	}

	/**
	 * Sets travel bundle template status service.
	 *
	 * @param travelBundleTemplateStatusService
	 * 		the travel bundle template status service
	 */
	@Required
	public void setTravelBundleTemplateStatusService(final TravelBundleTemplateStatusService travelBundleTemplateStatusService)
	{
		this.travelBundleTemplateStatusService = travelBundleTemplateStatusService;
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
	 * Sets model service.
	 *
	 * @param modelService
	 * 		the model service
	 */
	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * Gets enumeration service.
	 *
	 * @return the enumeration service
	 */
	protected EnumerationService getEnumerationService()
	{
		return enumerationService;
	}

	/**
	 * Sets enumeration service.
	 *
	 * @param enumerationService
	 * 		the enumeration service
	 */
	@Required
	public void setEnumerationService(final EnumerationService enumerationService)
	{
		this.enumerationService = enumerationService;
	}
}
