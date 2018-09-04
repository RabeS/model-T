package de.hybris.platform.transportbackoffice.widgets.bundle;

import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.travelbackoffice.constants.TravelbackofficeConstants;
import de.hybris.platform.travelservices.model.travel.BundleTemplateTransportOfferingMappingModel;

import java.util.Map;

import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Messagebox;

import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEventTypes;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationUtils;
import com.hybris.cockpitng.config.jaxb.wizard.CustomType;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.util.MessageboxUtils;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandler;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandlerAdapter;


public class CreateBundlePreviewPopupHandler implements FlowActionHandler {


    protected static final String CREATE_BUNDLE_TEMPLATE_ITEM_ID = "newBundleTemplate";
    protected static final String CREATE_FARE_BUNDLE_TEMPLATE_ITEM_ID = "fareBundleTemplate";
    protected static final String CREATE_ANCILLARY_BUNDLE_TEMPLATE_ITEM_ID = "ancillaryBundleTemplate";
    protected static final String BUNDLE_TEMPLATE_TRANSPORT_OFFERING_MAPPING_ID = "mapping";


    private ModelService modelService;


    @Override
    public void perform(final CustomType customType, final FlowActionHandlerAdapter flowActionHandlerAdapter, final Map<String, String> parameters) {

        Messagebox.show(Labels.getLabel(TravelbackofficeConstants.CREATE_BUNDLE_TEMPLATE_CONFIRMATION_POPUP),
                Labels.getLabel(TravelbackofficeConstants.CREATE_BUNDLE_PREVIEW_CONFIRMATION_POPUP_TITLE),
                MessageboxUtils.NO_YES_OPTION, null, clickEvent -> {
                    if (Messagebox.Button.YES.equals(clickEvent.getButton())) {
                        final WidgetModel widgetModel = flowActionHandlerAdapter.getWidgetInstanceManager().getModel();

                        final BundleTemplateModel parentBundleTemplate = widgetModel
                                .getValue(CREATE_BUNDLE_TEMPLATE_ITEM_ID, BundleTemplateModel.class);

                        final BundleTemplateModel fareBundleTemplate = widgetModel
                                .getValue(CREATE_FARE_BUNDLE_TEMPLATE_ITEM_ID, BundleTemplateModel.class);

                        final BundleTemplateModel ancillaryBundleTemplate = widgetModel
                                .getValue(CREATE_ANCILLARY_BUNDLE_TEMPLATE_ITEM_ID, BundleTemplateModel.class);

                        final BundleTemplateTransportOfferingMappingModel mapping = widgetModel
                                .getValue(BUNDLE_TEMPLATE_TRANSPORT_OFFERING_MAPPING_ID, BundleTemplateTransportOfferingMappingModel.class);

                        try {

                            getModelService().saveAll(parentBundleTemplate);
                            getModelService().save(fareBundleTemplate);
                            getModelService().save(ancillaryBundleTemplate);
                            getModelService().save(mapping);

                            NotificationUtils.notifyUser(
                                    NotificationUtils.getWidgetNotificationSource(flowActionHandlerAdapter.getWidgetInstanceManager()),
                                    TravelbackofficeConstants.TRAVEL_GLOBAL_NOTIFICATION_EVENT_TYPE, NotificationEvent.Level.SUCCESS,
                                    Labels.getLabel(TravelbackofficeConstants.CREATE_BUNDLE_TEMPLATE_CONFIRMATION_MESSAGE));

                            flowActionHandlerAdapter.done();

                        } catch (final ModelSavingException mse) {
                            NotificationUtils.notifyUser(
                                    NotificationUtils.getWidgetNotificationSource(flowActionHandlerAdapter.getWidgetInstanceManager()),
                                    NotificationEventTypes.EVENT_TYPE_OBJECT_CREATION, NotificationEvent.Level.FAILURE, mse);
                        }

                    }

                });

    }

    /**
     * @return the modelService
     */
    protected ModelService getModelService() {
        return modelService;
    }

    /**
     * @param modelService
     *         the modelService to set
     */
    @Required
    public void setModelService(final ModelService modelService) {
        this.modelService = modelService;
    }
}
