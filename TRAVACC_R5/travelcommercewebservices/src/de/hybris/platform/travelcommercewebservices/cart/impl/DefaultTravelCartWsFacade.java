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
package de.hybris.platform.travelcommercewebservices.cart.impl;

import static de.hybris.platform.util.localization.Localization.getLocalizedString;

import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.travel.AddBundleToCartRequestData;
import de.hybris.platform.commercefacades.travel.AddToCartResponseData;
import de.hybris.platform.commercefacades.travel.TravellerData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.travelcommercewebservices.cart.TravelCartWsFacade;
import de.hybris.platform.travelcommercewebservices.cart.data.AccommodationToCartData;
import de.hybris.platform.travelcommercewebservices.cart.data.AccommodationToCartRequestData;
import de.hybris.platform.travelcommercewebservices.cart.data.AddToCartRequestData;
import de.hybris.platform.travelcommercewebservices.cart.data.AddToCartResultData;
import de.hybris.platform.travelcommercewebservices.constants.TravelcommercewebservicesConstants;
import de.hybris.platform.travelcommercewebservices.enums.CartResultType;
import de.hybris.platform.travelcommercewebservices.error.data.ErrorData;
import de.hybris.platform.travelfacades.facades.TravelRestrictionFacade;
import de.hybris.platform.travelfacades.order.AccommodationCartFacade;
import de.hybris.platform.travelfacades.order.impl.DefaultTravelCartFacade;
import de.hybris.platform.travelfacades.strategies.AddBundleToCartValidationStrategy;
import de.hybris.platform.travelfacades.strategies.AddToCartValidationStrategy;
import de.hybris.platform.travelservices.enums.AmendStatus;
import de.hybris.platform.travelservices.enums.BookingJourneyType;
import de.hybris.platform.travelservices.model.accommodation.ConfiguredAccommodationModel;
import de.hybris.platform.travelservices.price.data.PriceLevel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;



/**
 * Facade for operations on carts {@link TravelCartWsFacade}.
 */
public class DefaultTravelCartWsFacade extends DefaultTravelCartFacade implements TravelCartWsFacade
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultTravelCartWsFacade.class);

	protected static final String BASKET_ERROR_OCCURRED = "basket.error.occurred";
	protected static final String BASKET_QUANTITY_NO_ITEM_ADDED_ERROR_PREFIX_KEY = "basket.information.quantity.noItemsAdded";
	protected static final String BASKET_QUANTITY_NOT_ENOUGH_STOCK_PREFIX_KEY = "basket.page.message.update.reducedNumberOfItemsAdded.noStock";

	private List<AddBundleToCartValidationStrategy> addBundleToCartValidationStrategyList;
	private List<AddToCartValidationStrategy> addToCartValidationStrategyList;
	private AccommodationCartFacade accommodationCartFacade;
	private TravelRestrictionFacade travelRestrictionFacade;
	private ConfigurationService configurationService;
	private EnumerationService enumerationService;

	@Override
	public AddToCartResultData addBundle(final AddBundleToCartRequestData addBundleToCartRequestData)
	{
		Assert.notNull(addBundleToCartRequestData.getAddBundleToCartData());
		Assert.notNull(addBundleToCartRequestData.getPassengerTypes());
		List<CartModificationData> totalCartModifications = new ArrayList<>();

		final AddToCartResultData addToCartResultData = new AddToCartResultData();
		addToCartResultData.setCartId(getCurrentCartCode());
		addToCartResultData.setStatus(CartResultType.ERROR);

		if (validateBundle(addBundleToCartRequestData))
		{
			setErrorList(addToCartResultData, null, TravelcommercewebservicesConstants.NOT_VALIDATED);
			return addToCartResultData;
		}

		// Clear cart of entries with OD > current OD
		final Integer odRefNum = addBundleToCartRequestData.getAddBundleToCartData().get(0).getOriginDestinationRefNumber();
		cleanUpCartForMinOriginDestinationRefNumber(odRefNum);

		try
		{
			totalCartModifications = addBundleToCart(addBundleToCartRequestData);
		}
		catch (final CommerceCartModificationException ex)
		{
			LOG.error("Exception occurred while adding bundle to cart : ", ex);
			setErrorList(addToCartResultData, ex, getLocalizedString(BASKET_ERROR_OCCURRED));
			return addToCartResultData;
		}
		for (final CartModificationData cartModification : totalCartModifications)
		{
			setAddErrorMessage(addToCartResultData, cartModification, null);
		}
		setStatusResponse(addToCartResultData);
		//Do cart evaluation only if there are no errors
		if (CartResultType.COMPLETED.equals(addToCartResultData.getStatus()))
		{
			// Add fees and discounts
			evaluateCart();
		}
		return addToCartResultData;
	}


	@Override
	public AddToCartResultData addAncillary(final AddToCartRequestData addToCartRequestData)
	{
		final AddToCartResultData addToCartResultData = new AddToCartResultData();
		addToCartResultData.setCartId(getCurrentCartCode());
		addToCartResultData.setStatus(CartResultType.ERROR);

		if (validateAncillaryProducts(addToCartRequestData))
		{
			setErrorList(addToCartResultData, null, TravelcommercewebservicesConstants.NOT_VALIDATED);
			return addToCartResultData;
		}
		CartModificationData cartModification = null;
		try
		{
			cartModification = addAncillaryProducts(addToCartRequestData);
		}
		catch (final CommerceCartModificationException ex)
		{
			LOG.warn("Couldn't update product with Code: " + addToCartRequestData.getProductCode() + ", Quantity: "
					+ addToCartRequestData.getQuantity(), ex);
		}
		setAddErrorMessage(addToCartResultData, cartModification, addToCartRequestData.getQuantity());
		setStatusResponse(addToCartResultData);
		return addToCartResultData;
	}

	@Override
	public AddToCartResultData addSelectedAccommodationsToCart(
			final AccommodationToCartRequestData addAccommodationToCartRequestData)
	{
		Assert.notNull(addAccommodationToCartRequestData.getAccommodationToCart());
		final AddToCartResultData addToCartResultData = new AddToCartResultData();
		addToCartResultData.setCartId(getCurrentCartCode());
		addToCartResultData.setStatus(CartResultType.ERROR);

		final List<AccommodationToCartData> addRemoveAccommodations = addAccommodationToCartRequestData.getAccommodationToCart();
		final List<AccommodationToCartData> removeAccommodations = addRemoveAccommodations.stream().filter(
				addRemoveAccommodation -> StringUtils.isNotEmpty(addRemoveAccommodation.getPreviousSelectedAccommodationUid()))
				.collect(Collectors.toList());

		final List<AccommodationToCartData> addAccommodations = addRemoveAccommodations.stream()
				.filter(addRemoveAccommodation -> StringUtils.isNotEmpty(addRemoveAccommodation.getAccommodationUid()))
				.collect(Collectors.toList());

		if (!validateForDuplicateAccommodation(addAccommodations))
		{
			setErrorList(addToCartResultData, null, "Cannot book same accommodation for multiple passengers");
			return addToCartResultData;
		}

		for (final AccommodationToCartData removeAccommodation : removeAccommodations)
		{
			final boolean isRemoved = getAccommodationCartFacade()
					.removeSelectedAccommodationFromCart(removeAccommodation.getPreviousSelectedAccommodationUid(),
							removeAccommodation.getTransportOfferings(), removeAccommodation.getTravellerCode(),
							removeAccommodation.getTravelRouteCode());

			if (!isRemoved)
			{
				setErrorList(addToCartResultData, null, BASKET_ERROR_OCCURRED);
				return addToCartResultData;
			}
		}

		validateAccommodationToCartData(addAccommodations, addToCartResultData);
		if (CollectionUtils.isNotEmpty(addToCartResultData.getErrors()))
		{
			return addToCartResultData;
		}

		for (final AccommodationToCartData addAccommodation : addAccommodations)
		{
			final boolean isBooked = getAccommodationCartFacade()
					.addSelectedAccommodationToCart(addAccommodation.getAccommodationUid(), addAccommodation.getTransportOfferings(),
							addAccommodation.getTravellerCode(), addAccommodation.getOriginDestinationRefNumber(),
							addAccommodation.getTravelRouteCode());

			if (!isBooked)
			{
				addAccommodations.remove(addAccommodation);
				rollbackAccomodation(addAccommodations);
				setErrorList(addToCartResultData, null,
						"No accommodation added to the cart as the accommodation " + addAccommodation.getAccommodationUid()
								+ " is not available");
				return addToCartResultData;
			}
			addToCartResultData.setStatus(CartResultType.COMPLETED);
		}
		return addToCartResultData;
	}

	@Override
	public void setBookingJourneyTypeToCart(final String bookingJourney)
	{
		final BookingJourneyType bookingJourneyType = getEnumerationService().getEnumerationValue(BookingJourneyType.class,
				bookingJourney);
		final CartModel cart = getCartService().getSessionCart();
		cart.setBookingJourneyType(bookingJourneyType);
		getCartService().saveOrder(cart);
	}

	
	/**
	 * Validate accommodation to cart data.
	 *
	 * @param addAccommodations
	 * 		the add accommodations
	 * @param addToCartResultData
	 * 		the add to cart result data
	 */
	protected void validateAccommodationToCartData(final List<AccommodationToCartData> addAccommodations,
			final AddToCartResultData addToCartResultData)
	{

		for (final AccommodationToCartData addAccommodation : addAccommodations)
		{
			final ConfiguredAccommodationModel accommodation = getAccommodationMapService()
					.getAccommodation(addAccommodation.getAccommodationUid());
			final TravellerData traveller = getTravellerFacade().getTravellerFromCurrentCart(addAccommodation.getTravellerCode());
			if (Objects.isNull(traveller))
			{
				setErrorList(addToCartResultData, null, "No traveller found for id " + addAccommodation.getTravellerCode());
			}
			final Boolean isAccommodationAvailableForBooking = getAccommodationMapService()
					.isAccommodationAvailableForBooking(accommodation, addAccommodation.getTransportOfferings(), traveller);

			if (!isAccommodationAvailableForBooking)
			{
				setErrorList(addToCartResultData, null,
						"Accommodation " + addAccommodation.getAccommodationUid() + " is not available");
			}

			final boolean isSelectedAccommodation = getCartService().getSessionCart().getSelectedAccommodations().stream().anyMatch(
					selectedAccommodationModel -> selectedAccommodationModel.getConfiguredAccommodation().equals(accommodation));
			if (isSelectedAccommodation)
			{
				setErrorList(addToCartResultData, null,
						"accommodation " + addAccommodation.getAccommodationUid()
								+ " exists in the cart. Please add another accommodation");
			}

		}
	}

	protected void rollbackAccomodation(final List<AccommodationToCartData> accommodations)
	{
		for (final AccommodationToCartData removeAccommodation : accommodations)
		{
			getAccommodationCartFacade().removeSelectedAccommodationFromCart(removeAccommodation.getAccommodationUid(),
					removeAccommodation.getTransportOfferings(), removeAccommodation.getTravellerCode(),
					removeAccommodation.getTravelRouteCode());
		}
	}

	protected boolean validateForDuplicateAccommodation(final List<AccommodationToCartData> accommodations)
	{
		final List<String> accommodationIds = accommodations.stream().map(AccommodationToCartData::getAccommodationUid).collect(
				Collectors.toList());
		final Set<String> addAccommodationSet = new HashSet<>(accommodationIds);
		return accommodationIds.size() == addAccommodationSet.size();

	}

	protected boolean validateBundle(final AddBundleToCartRequestData addBundleToCartRequestData)
	{
		for (final AddBundleToCartValidationStrategy strategy : addBundleToCartValidationStrategyList)
		{
			final AddToCartResponseData response = strategy.validate(addBundleToCartRequestData);
			if (!response.isValid())
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Validate ancillary products boolean.
	 *
	 * @param addToCartRequestData
	 * 		the add to cart request data
	 * @return the boolean
	 */
	protected boolean validateAncillaryProducts(final AddToCartRequestData addToCartRequestData)
	{
		for (final AddToCartValidationStrategy strategy : addToCartValidationStrategyList)
		{
			final AddToCartResponseData addToCartResponseData = strategy
					.validateAddToCart(addToCartRequestData.getProductCode(), addToCartRequestData.getQuantity(),
							addToCartRequestData.getTravellerCode(), addToCartRequestData.getTransportOfferings(),
							addToCartRequestData.getTravelRouteCode());
			if (!addToCartResponseData.isValid())
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Add ancillary products cart modification data.
	 *
	 * @param addToCartRequestData
	 * 		the add to cart request data
	 * @return the cart modification data
	 * @throws CommerceCartModificationException
	 * 		the commerce cart modification exception
	 */
	protected CartModificationData addAncillaryProducts(final AddToCartRequestData addToCartRequestData)
			throws CommerceCartModificationException
	{
		final OrderEntryData existingOrderEntry = getOrderEntry(addToCartRequestData.getProductCode(),
				addToCartRequestData.getTravelRouteCode(), addToCartRequestData.getTransportOfferings(),
				addToCartRequestData.getTravellerCode(), true);
		CartModificationData cartModification = null;
		if (Objects.isNull(existingOrderEntry) && addToCartRequestData.getQuantity() > 0)
		{

			final PriceLevel priceLevel = getTravelCommercePriceFacade()
					.getPriceLevelInfo(addToCartRequestData.getProductCode(), addToCartRequestData.getTransportOfferings(),
							addToCartRequestData.getTravelRouteCode());
			if (Objects.isNull(priceLevel))
			{
				return cartModification;
			}
			if (Objects.nonNull(addToCartRequestData.getTravellerCode()))
			{
				final TravellerData traveller = getTravellerFacade()
						.getTravellerFromCurrentCart(addToCartRequestData.getTravellerCode());
				if (Objects.isNull(traveller))
				{
					return null;
				}
				getTravelCommercePriceFacade().setPriceAndTaxSearchCriteriaInContext(priceLevel,
						addToCartRequestData.getTransportOfferings(), traveller);
			}
			else
			{
				getTravelCommercePriceFacade()
						.setPriceAndTaxSearchCriteriaInContext(priceLevel, addToCartRequestData.getTransportOfferings());
			}

			cartModification = addToCart(addToCartRequestData.getProductCode(), addToCartRequestData.getQuantity());
			getTravelCommercePriceFacade()
					.addPropertyPriceLevelToCartEntry(priceLevel, cartModification.getEntry().getProduct().getCode(),
							cartModification.getEntry().getEntryNumber());
			if (cartModification.getQuantityAdded() >= MINIMUM_PRODUCT_QUANTITY)
			{
				addPropertiesToCartEntry(addToCartRequestData.getTravellerCode(),
						addToCartRequestData.getOriginDestinationRefNumber(), addToCartRequestData.getTravelRouteCode(),
						addToCartRequestData.getTransportOfferings(), Boolean.TRUE, AmendStatus.NEW, cartModification);
				recalculateCart();
			}
		}
		else if (Objects.nonNull(existingOrderEntry))
		{
			final long newQuantity = existingOrderEntry.getQuantity() + addToCartRequestData.getQuantity();
			cartModification = updateCartEntry(existingOrderEntry.getEntryNumber(), newQuantity);
		}
		return cartModification;
	}

	/**
	 * Add properties to cart entry.
	 *
	 * @param travellerCode
	 * 		the traveller code
	 * @param originDestinationRefNo
	 * 		the origin destination ref no
	 * @param travelRoute
	 * 		the travel route
	 * @param transportOfferingCodes
	 * 		the transport offering codes
	 * @param active
	 * 		the active
	 * @param amendStatus
	 * 		the amend status
	 * @param cartModification
	 * 		the cart modification
	 */
	protected void addPropertiesToCartEntry(final String travellerCode, final int originDestinationRefNo, final String
			travelRoute,
			final List<String> transportOfferingCodes, final Boolean active, final AmendStatus amendStatus,
			final CartModificationData cartModification)
	{
		final String productCode = cartModification.getEntry().getProduct().getCode();
		final String addToCartCriteria = travelRestrictionFacade.getAddToCartCriteria(productCode);

		addPropertiesToCartEntry(productCode, cartModification.getEntry().getEntryNumber(), transportOfferingCodes, travelRoute,
				originDestinationRefNo, travellerCode, active, amendStatus, addToCartCriteria);
	}

	/**
	 * Sets add error message.
	 *
	 * @param addToCartResultData
	 * 		the add to cart result data
	 * @param cartModification
	 * 		the cart modification
	 * @param requestedQuantity
	 * 		the requested quantity
	 */
	protected void setAddErrorMessage(final AddToCartResultData addToCartResultData, final CartModificationData cartModification,
			final Integer requestedQuantity)
	{
		String errorMessage = null;
		if (Objects.isNull(cartModification))
		{
			errorMessage = getLocalizedString(BASKET_ERROR_OCCURRED);
			setErrorList(addToCartResultData, null, errorMessage);
			return;
		}
		else if (cartModification.getQuantityAdded() == 0L)
		{
			errorMessage = getLocalizedString(BASKET_QUANTITY_NO_ITEM_ADDED_ERROR_PREFIX_KEY + cartModification.getStatusCode(),
					new Object[] { cartModification.getEntry().getProduct().getName() });
		}
		else if (Objects.nonNull(requestedQuantity) && cartModification.getQuantityAdded() != requestedQuantity.longValue())
		{
			addToCartResultData.setStatus(CartResultType.NOT_ENOUGH_STOCK);
			errorMessage = getLocalizedString(BASKET_QUANTITY_NOT_ENOUGH_STOCK_PREFIX_KEY + cartModification.getStatusCode(),
					new Object[] { cartModification.getEntry().getProduct().getName() });
		}
		setErrorList(addToCartResultData, null, errorMessage);
	}

	/**
	 * Sets error list.
	 *
	 * @param addToCartResultData
	 * 		the add to cart result data
	 * @param ex
	 * 		the ex
	 * @param exceptionKey
	 * 		the exception key
	 */
	protected void setErrorList(final AddToCartResultData addToCartResultData, final Exception ex, final String exceptionKey)
	{
		if (StringUtils.isNotEmpty(exceptionKey))
		{
			final List<ErrorData> errorDataList = new ArrayList<>();
			ErrorData errorData = null;
			if (Objects.nonNull(ex))
			{
				errorData = createErrorData(ex, exceptionKey);
			}
			else
			{
				errorData = createErrorData(null, exceptionKey);
			}
			errorData.setMessage(exceptionKey);
			errorDataList.add(errorData);
			addToCartResultData.setErrors(errorDataList);
		}
	}

	/**
	 * Create error data error data.
	 *
	 * @param exception
	 * 		the exception
	 * @param exceptionKey
	 * 		the exception key
	 * @return the error data
	 */
	protected ErrorData createErrorData(final Exception exception, final String exceptionKey)
	{
		final ErrorData error = new ErrorData();

		final String errorSubject = getConfigurationService().getConfiguration().getString(exceptionKey);
		if (Objects.nonNull(errorSubject))
		{
			error.setSubject(errorSubject);
		}
		else
		{
			error.setSubject(exceptionKey);
		}
		if (Objects.nonNull(exception))
		{
			error.setMessage(exception.getMessage());
			error.setType(exception.getClass().getSimpleName());
		}
		return error;
	}

	/**
	 * Sets status response.
	 *
	 * @param addToCartResultData
	 * 		the add to cart result data
	 */
	protected void setStatusResponse(final AddToCartResultData addToCartResultData)
	{
		if (CollectionUtils.isEmpty(addToCartResultData.getErrors()))
		{
			addToCartResultData.setStatus(CartResultType.COMPLETED);
		}
		else
		{
			addToCartResultData.setStatus(CartResultType.ERROR);
		}
	}

	/**
	 * Gets configuration service.
	 *
	 * @return the configurationService
	 */
	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	/**
	 * Sets configuration service.
	 *
	 * @param configurationService
	 * 		the configurationService to set
	 */
	@Required
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	/**
	 * Gets addToCart validation strategy list
	 *
	 * @return the addToCartValidationStrategyList
	 */
	protected List<AddToCartValidationStrategy> getAddToCartValidationStrategyList()
	{
		return addToCartValidationStrategyList;
	}

	/**
	 * Sets addToCart validation strategy list.
	 *
	 * @param addToCartValidationStrategyList
	 * 		the addToCartValidationStrategyList to set
	 */
	@Required
	public void setAddToCartValidationStrategyList(final List<AddToCartValidationStrategy> addToCartValidationStrategyList)
	{
		this.addToCartValidationStrategyList = addToCartValidationStrategyList;
	}

	/**
	 * Gets addBundleToCart validation strategy list
	 *
	 * @return the addBundleToCartValidationStrategyList
	 */
	protected List<AddBundleToCartValidationStrategy> getAddBundleToCartValidationStrategyList()
	{
		return addBundleToCartValidationStrategyList;
	}

	/**
	 * Sets addBundleToCart validation strategy list
	 *
	 * @param addBundleToCartValidationStrategyList
	 * 		the addBundleToCartValidationStrategyList to set
	 */
	@Required
	public void setAddBundleToCartValidationStrategyList(
			final List<AddBundleToCartValidationStrategy> addBundleToCartValidationStrategyList)
	{
		this.addBundleToCartValidationStrategyList = addBundleToCartValidationStrategyList;
	}

	/**
	 * Gets travel restriction facade
	 *
	 * @return the travelRestrictionFacade
	 */
	protected TravelRestrictionFacade getTravelRestrictionFacade()
	{
		return travelRestrictionFacade;
	}

	/**
	 * Sets travel restriction facade.
	 *
	 * @param travelRestrictionFacade
	 * 		the travelRestrictionFacade to set
	 */
	@Required
	public void setTravelRestrictionFacade(final TravelRestrictionFacade travelRestrictionFacade)
	{
		this.travelRestrictionFacade = travelRestrictionFacade;
	}

	/**
	 * Gets accommodation facade.
	 *
	 * @return the accommodationCartFacade
	 */
	protected AccommodationCartFacade getAccommodationCartFacade()
	{
		return accommodationCartFacade;
	}

	/**
	 * Sets accommodation facade.
	 *
	 * @param accommodationCartFacade
	 * 		the accommodationCartFacade to set
	 */
	@Required
	public void setAccommodationCartFacade(final AccommodationCartFacade accommodationCartFacade)
	{
		this.accommodationCartFacade = accommodationCartFacade;
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