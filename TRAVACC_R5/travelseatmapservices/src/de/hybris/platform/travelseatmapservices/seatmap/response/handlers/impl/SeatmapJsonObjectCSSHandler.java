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
package de.hybris.platform.travelseatmapservices.seatmap.response.handlers.impl;

import de.hybris.platform.travelseatmapservices.seatmap.response.SeatMapJSONObject;
import de.hybris.platform.travelseatmapservices.seatmap.response.handlers.SeatmapJsonObjectHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.StringJoiner;

import org.apache.log4j.Logger;
import org.apache.logging.log4j.util.Strings;


/**
 * This populates seatMapJsonObject with the CSS.
 */
public class SeatmapJsonObjectCSSHandler implements SeatmapJsonObjectHandler
{
	private static final Logger LOG = Logger.getLogger(SeatmapJsonObjectCSSHandler.class);
	protected static final String CSS_FOLDER_NAME = "css";

	@Override
	public void handle(final String seatMapPath, final SeatMapJSONObject seatMapJsonObject)
	{
		seatMapJsonObject.setCss(getCssFileAsString(seatMapPath));
	}

	protected String getCssFileAsString(final String path)
	{
		try
		{
			final StringJoiner cssFile = new StringJoiner(Strings.LINE_SEPARATOR);

			final Path fileInputStream = Paths.get(path, CSS_FOLDER_NAME);
			final Optional<Path> cssFileFound = Files.list(fileInputStream).filter(file -> !Files.isDirectory(file)).findFirst();
			if (cssFileFound.isPresent())
			{
				Files.lines(cssFileFound.get()).forEach(line -> cssFile.add(line));
			}

			return cssFile.toString();
		}
		catch (final IOException ex)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.error(ex);
			}
		}
		return Strings.EMPTY;
	}
}
