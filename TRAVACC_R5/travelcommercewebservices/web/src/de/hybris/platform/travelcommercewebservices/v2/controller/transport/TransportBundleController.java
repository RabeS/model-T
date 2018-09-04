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

package de.hybris.platform.travelcommercewebservices.v2.controller.transport;

import de.hybris.platform.commercefacades.travel.TravelBundleTemplateData;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.travelcommercewebservices.v2.controller.travel.BaseController;
import de.hybris.platform.travelfacades.facades.TravelBundleTemplateFacade;
import de.hybris.travelcommercewebservicescommons.dto.TravelBundleTemplateWsDTO;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


/**
 * Bundles Controller
 */
@Controller
@RequestMapping(value = "/{baseSiteId}")
@Api(tags = "Bundles")
public class TransportBundleController extends BaseController
{
	private static final Logger LOG = Logger.getLogger(TransportBundleController.class);

	@Resource(name = "travelBundleTemplateFacade")
	private TravelBundleTemplateFacade travelBundleTemplateFacade;

	/**
	 * This method gets the Bundle for given Bundle Id
	 * 
	 * @param bundleId
	 * @param fields
	 * @return
	 */
	@RequestMapping(value = "/catalogs/{catalogId}/bundles/{bundleId}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	@ApiOperation(value = "Returns the Bundle template for the given bundle Id", produces = "application/json")
	@ApiResponses(
	{ @ApiResponse(code = 200, message = "Success", response = TravelBundleTemplateWsDTO.class),
			@ApiResponse(code = 204, message = "No Content", response = ResponseEntity.class),
			@ApiResponse(code = 422, message = "Unprocessable Entity", response = ResponseEntity.class) })
	@ApiImplicitParams(
	{ @ApiImplicitParam(name = "fields", value = DEFAULT_FIELD_SET, required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "baseSiteId", required = true, dataType = "String", paramType = "path"),
			@ApiImplicitParam(name = "catalogId", required = true, dataType = "String", paramType = "path"),
			@ApiImplicitParam(name = "bundleId", required = true, dataType = "String", paramType = "path") })
	public ResponseEntity<TravelBundleTemplateWsDTO> getBundleByCode(
			@ApiParam(value = "Bundle identifier", required = true) @PathVariable final String bundleId,
			@ApiParam(value = "Catalog Id", required = true) @PathVariable final String catalogId,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("getBundleByCode: code=" + sanitize(bundleId) + " and catalog= " + sanitize(catalogId));
		}

		final TravelBundleTemplateData bundleTemplateData;
		try
		{
			bundleTemplateData = travelBundleTemplateFacade.getBundleTemplateByCodeAndCatalogVersion(bundleId, catalogId);
		}
		catch (final ModelNotFoundException e)
		{
			LOG.warn("No bundle is found for given id: " + bundleId);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		catch (final Exception e)
		{
			LOG.debug("Exception occurred while getting bundle by code", e);
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
		return new ResponseEntity<>(getDataMapper().map(bundleTemplateData, TravelBundleTemplateWsDTO.class), HttpStatus.OK);
	}

}
