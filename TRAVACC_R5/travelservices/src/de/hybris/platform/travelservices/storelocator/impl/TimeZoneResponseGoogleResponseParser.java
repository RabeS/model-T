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

package de.hybris.platform.travelservices.storelocator.impl;

import de.hybris.platform.commercefacades.storelocator.data.TimeZoneResponseData;
import de.hybris.platform.storelocator.exception.GeoServiceWrapperException;

import java.io.IOException;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseExtractor;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;


/**
 * The type Time zone response google response parser.
 */
public class TimeZoneResponseGoogleResponseParser implements ResponseExtractor<TimeZoneResponseData>
{

	@Override
	public TimeZoneResponseData extractData(final ClientHttpResponse response) throws IOException
	{
		final TimeZoneResponseData timeZoneResponseData = new TimeZoneResponseData();
		final XPath xpath = XPathFactory.newInstance().newXPath();
		final InputSource inputSource = new InputSource(response.getBody());
		try
		{
			final Node root = (Node) xpath.evaluate("/", inputSource, XPathConstants.NODE);
			final String status = xpath.evaluate("/TimeZoneResponse/status", root);
			if (!"OK".equalsIgnoreCase(status))
			{
				throw new GeoServiceWrapperException("Could not get timezone offset : ", status);
			}
			final String dstOffset = xpath.evaluate("/TimeZoneResponse/dst_offset", root);
			if (StringUtils.isNotBlank(dstOffset))
			{
				timeZoneResponseData.setDstOffset(Double.parseDouble(xpath.evaluate("/TimeZoneResponse/dst_offset", root)));
			}
			final String rawOffset = xpath.evaluate("/TimeZoneResponse/raw_offset", root);
			if (StringUtils.isNotBlank(rawOffset))
			{
				timeZoneResponseData.setRawOffset(Double.parseDouble(xpath.evaluate("/TimeZoneResponse/raw_offset", root)));
			}
			timeZoneResponseData.setStatus(xpath.evaluate("/TimeZoneResponse/status", root));
			timeZoneResponseData.setTimeZoneId(xpath.evaluate("/TimeZoneResponse/time_zone_id", root));
			timeZoneResponseData.setTimeZoneName(xpath.evaluate("/TimeZoneResponse/time_zone_name", root));

		}
		catch (final XPathExpressionException e)
		{
			throw new GeoServiceWrapperException("Cannot get Google response due to :", e);
		}
		return timeZoneResponseData;
	}
}
