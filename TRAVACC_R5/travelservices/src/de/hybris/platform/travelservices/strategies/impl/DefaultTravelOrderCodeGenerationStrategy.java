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

package de.hybris.platform.travelservices.strategies.impl;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.travelservices.strategies.TravelOrderCodeGenerationStrategy;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * The type Default travel order code generation strategy.
 */
public class DefaultTravelOrderCodeGenerationStrategy implements TravelOrderCodeGenerationStrategy
{
	private static final Logger LOG = Logger.getLogger(DefaultTravelOrderCodeGenerationStrategy.class);

	private ModelService modelService;


	@Override
	public void generateTravelOrderCode(final AbstractOrderModel orderModel)
	{
		final String originalOrderCode = Integer.toString(Integer.parseInt(orderModel.getCode()), 36).toUpperCase();
		boolean success;
		do
		{
			success = updateOrderCode(getCodeFromOrderNumber(originalOrderCode), orderModel);
		}
		while (!success);
	}


	protected boolean updateOrderCode(final String orderCode, final AbstractOrderModel orderModel)
	{
		try
		{
			orderModel.setCode(orderCode);
			getModelService().save(orderModel);
			return true;
		}
		catch (final ModelSavingException mEx)
		{
			LOG.info("Clashing order code found. Trying again...");
			LOG.debug(mEx);
			return false;
		}

	}

	/**
	 * Algorithm to calculate the order code starting from the one generated by the persistent key generator. Steps: -
	 * Code is initially converted into a base36 number (6 alphanumeric chars) - From the string built in the previous
	 * step, a SHA512 digest (128 chars) is calculated - The digest is shuffled - 8 alphanumeric chars are extracted from
	 * the digest and shuffled - The 8 chars (HEX) are converted into an integer - The integer coming from the previous
	 * step, is converted into a base36 number and it will always be a 6 alphanumeric chars string
	 *
	 * @param convertedOrderCode
	 * @return
	 */
	protected String getCodeFromOrderNumber(final String convertedOrderCode)
	{
		final String shaCode = DigestUtils.sha512Hex(convertedOrderCode);
		final Character[] charObjectArray = ArrayUtils.toObject(shaCode.toCharArray());
		final List<Character> shaCharList = Arrays.asList(charObjectArray);
		Collections.shuffle(shaCharList);
		final List<Character> orderCodeCharList = shaCharList.stream().limit(8)
				.collect(Collectors.collectingAndThen(Collectors.toList(), collected -> {
					Collections.shuffle(collected);
					return collected;
				}));

		final String shaHexString = new String(
				ArrayUtils.toPrimitive(orderCodeCharList.toArray(new Character[orderCodeCharList.size()])));
		return normalizeCode(Integer.toString(new BigInteger(shaHexString, 16).abs().intValue(), 36).toUpperCase());
	}

	/**
	 * Normalize the code, removing sign if present and padding with a zero if the leading zero had been truncated
	 * because not significant.
	 * 
	 * @param codeToNormalize
	 * @return
	 */
	protected String normalizeCode(final String codeToNormalize)
	{
		return StringUtils.leftPad(codeToNormalize.replace("-", StringUtils.EMPTY), 6, "0");
	}


	/**
	 * 
	 * @return modelService
	 */
	protected ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * 
	 * @param modelService
	 *           the modelService to set
	 */
	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}
}
