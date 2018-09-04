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

package de.hybris.platform.travelcommercewebservices.mapping.converters;

import de.hybris.platform.commercefacades.travel.PassengerInformationData;
import de.hybris.platform.commercefacades.travel.PassengerTypeData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.commercefacades.user.data.TitleData;
import de.hybris.platform.travelservices.enums.DocumentType;
import de.hybris.platform.webservicescommons.mapping.WsDTOMapping;
import de.hybris.travelcommercewebservicescommons.dto.PassengerInformationWsDTO;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;


/**
 * Converter for {@link TravellerInfoWsConverter}
 */
@WsDTOMapping
public class TravellerInfoWsConverter extends CustomConverter<PassengerInformationWsDTO , PassengerInformationData>
{

	@Override
	public PassengerInformationData convert(final PassengerInformationWsDTO passengerInformationWsDTO,
			final Type<? extends PassengerInformationData> type, final MappingContext mappingContext)
	{
		final PassengerInformationData passengerInformationData = new PassengerInformationData();

		final TitleData title = new TitleData();
		title.setCode(passengerInformationWsDTO.getTitle().getCode());
		passengerInformationData.setTitle(title);
		passengerInformationData.setFirstName(passengerInformationWsDTO.getFirstName());
		passengerInformationData.setSurname(passengerInformationWsDTO.getSurname());
		passengerInformationData.setGender(passengerInformationWsDTO.getGender());
		passengerInformationData.setReasonForTravel(passengerInformationWsDTO.getReasonForTravel());
		passengerInformationData.setSaveDetails(passengerInformationWsDTO.isSaveDetails());
		passengerInformationData.setEmail(passengerInformationWsDTO.getEmail());
		passengerInformationData.setDateOfBirth(passengerInformationWsDTO.getDateOfBirth());
		passengerInformationData.setDocumentNumber(passengerInformationWsDTO.getDocumentNumber());
		passengerInformationData.setAPIType(passengerInformationWsDTO.getAPIType());
		final CountryData countryOfIssue =  new CountryData();
		countryOfIssue.setIsocode(passengerInformationWsDTO.getCountryOfIssue().getIsocode());
		countryOfIssue.setName(passengerInformationWsDTO.getCountryOfIssue().getName());
		passengerInformationData.setCountryOfIssue(countryOfIssue);
		passengerInformationData.setDocumentExpiryDate(passengerInformationWsDTO.getDocumentExpiryDate());
		passengerInformationData.setDocumentType((passengerInformationWsDTO.getDocumentType() == null) ? null : DocumentType.valueOf(passengerInformationWsDTO.getDocumentType()));
		passengerInformationData.setMembershipNumber(passengerInformationWsDTO.getMembershipNumber());
		final CountryData nationality =  new CountryData();
		nationality.setName(passengerInformationWsDTO.getNationality().getName());
		nationality.setIsocode(passengerInformationWsDTO.getNationality().getIsocode());
		passengerInformationData.setNationality(nationality);

		if (StringUtils.isNotBlank(passengerInformationWsDTO.getSavedTravellerUId()))
		{
			passengerInformationData.setSavedTravellerUId(passengerInformationWsDTO.getSavedTravellerUId());
		}

		final PassengerTypeData passengerType = new PassengerTypeData();
		passengerType.setCode(passengerInformationWsDTO.getPassengerType().getCode());
		passengerInformationData.setPassengerType(passengerType);

		return passengerInformationData;
	}
}
