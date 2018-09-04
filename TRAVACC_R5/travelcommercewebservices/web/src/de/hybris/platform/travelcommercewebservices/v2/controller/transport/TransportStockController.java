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

import de.hybris.platform.commercefacades.product.data.StockData;
import de.hybris.platform.travelcommercewebservices.stock.TravelStockWsFacade;
import de.hybris.platform.travelcommercewebservices.v2.controller.travel.BaseController;
import de.hybris.travelcommercewebservicescommons.dto.StockLevelWsDTO;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * The Travel stock controller.
 */
@Controller
@RequestMapping(value = "/{baseSiteId}/stock/transportOfferings")
@Api(tags = "Stocks")
public class TransportStockController extends BaseController
{
	@Resource(name = "travelStockWsFacade")
	private TravelStockWsFacade travelStockWsFacade;

	/**
	 * Return a list of stock levels of the products in a transport offering
	 *
	 * @param transportOfferingCode
	 * 		the transport offering code
	 * @param fields
	 * 		the fields
	 * @return the transport offering stock
	 */
	@RequestMapping(value = "/{transportOfferingCode}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	@ApiOperation(value = "Returns the Transport Offering stock levels for the given code", produces = "application/json")
	@ApiResponses(
			{ @ApiResponse(code = 200, message = "Success", response = StockLevelWsDTO.class),
					@ApiResponse(code = 204, message = "No Content", response = String.class),
					@ApiResponse(code = 400, message = "Bad Request") })
	@ApiImplicitParams(
			{ @ApiImplicitParam(name = "fields", value = DEFAULT_FIELD_SET, required = false, dataType = "String", paramType = "query"),
					@ApiImplicitParam(name = "transportOfferingCode", required = true, dataType = "String", paramType = "path"),
					@ApiImplicitParam(name = "baseSiteId", required = true, dataType = "String", paramType = "path") })
	public ResponseEntity<List<StockLevelWsDTO>> getTransportOfferingStock(
			@ApiParam(value = "Transport Offering code", required = true) @PathVariable final String transportOfferingCode,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{

		final List<StockData> stocks = travelStockWsFacade
				.getStockLevelsForWarehouse(Collections.singletonList(transportOfferingCode));

		if (stocks == null)
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		if (CollectionUtils.isEmpty(stocks))
		{
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(getDataMapper().mapAsList(stocks, StockLevelWsDTO.class, null), HttpStatus.OK);
	}

	/**
	 * Return a list of stock levels for a given product in a transport offering
	 *
	 * @param transportOfferingCode
	 * 		the transport offering code
	 * @param productCode
	 * 		the product code
	 * @param fields
	 * 		the fields
	 * @return the product stock for transport offering
	 */
	@RequestMapping(value = "/{transportOfferingCode}/{productCode}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	@ApiOperation(value = "Return the stock levels of a product in a transport offering", produces = "application/json")
	@ApiResponses(
			{ @ApiResponse(code = 200, message = "Success", response = StockLevelWsDTO.class),
					@ApiResponse(code = 204, message = "No Content", response = String.class),
					@ApiResponse(code = 400, message = "Bad Request") })
	@ApiImplicitParams(
			{ @ApiImplicitParam(name = "fields", value = DEFAULT_FIELD_SET, required = false, dataType = "String", paramType = "query"),
					@ApiImplicitParam(name = "transportOfferingCode", required = true, dataType = "String", paramType = "path"),
					@ApiImplicitParam(name = "productCode", required = true, dataType = "String", paramType = "path"),
					@ApiImplicitParam(name = "baseSiteId", required = true, dataType = "String", paramType = "path") })
	public ResponseEntity<List<StockLevelWsDTO>> getProductStockForTransportOffering(
			@ApiParam(value = "Transport Offering code", required = true) @PathVariable final String transportOfferingCode,
			@ApiParam(value = "Product code", required = true) @PathVariable final String productCode,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{

		final List<StockData> stocks = travelStockWsFacade
				.getProductStockLevelsForWarehouses(productCode, Collections.singletonList(transportOfferingCode));

		if (stocks == null)
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		if (CollectionUtils.isEmpty(stocks))
		{
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(getDataMapper().mapAsList(stocks, StockLevelWsDTO.class, null), HttpStatus.OK);
	}

}
