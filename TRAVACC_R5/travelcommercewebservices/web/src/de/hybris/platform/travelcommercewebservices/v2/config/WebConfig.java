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

package de.hybris.platform.travelcommercewebservices.v2.config;


import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.travelcommercewebservices.constants.TravelcommercewebservicesConstants;
import de.hybris.platform.travelcommercewebservices.request.mapping.handler.CommerceHandlerMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import net.sourceforge.pmd.util.StringUtil;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.ClientCredentialsGrant;
import springfox.documentation.service.OAuth;
import springfox.documentation.service.ResourceOwnerPasswordCredentialsGrant;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;



/**
 * Spring configuration which replace <mvc:annotation-driven> tag. It allows override default
 * RequestMappingHandlerMapping with our own mapping handler
 *
 */
@Configuration
@EnableSwagger2
@ImportResource(
		{ "WEB-INF/config/v2/springmvc-v2-servlet.xml" })
public class WebConfig extends WebMvcConfigurationSupport
{
	@Resource
	private List<HttpMessageConverter<?>> messageConvertersV2;

	@Resource
	private List<HandlerExceptionResolver> exceptionResolversV2;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	private ApplicationContext applicationContext;

	@Override
	@Bean
	public RequestMappingHandlerMapping requestMappingHandlerMapping()
	{
		final CommerceHandlerMapping handlerMapping = new CommerceHandlerMapping("v2");
		handlerMapping.setOrder(0);
		handlerMapping.setDetectHandlerMethodsInAncestorContexts(true);
		handlerMapping.setInterceptors(getInterceptors());
		handlerMapping.setContentNegotiationManager(mvcContentNegotiationManager());
		return handlerMapping;
	}

	@Override
	protected void configureMessageConverters(final List<HttpMessageConverter<?>> converters)
	{
		converters.addAll(messageConvertersV2);
	}

	@Override
	protected void configureHandlerExceptionResolvers(final List<HandlerExceptionResolver> exceptionResolvers)
	{
		final ExceptionHandlerExceptionResolver exceptionHandlerExceptionResolver = new ExceptionHandlerExceptionResolver();
		exceptionHandlerExceptionResolver.setApplicationContext(applicationContext);
		exceptionHandlerExceptionResolver.setContentNegotiationManager(mvcContentNegotiationManager());
		exceptionHandlerExceptionResolver.setMessageConverters(getMessageConverters());
		exceptionHandlerExceptionResolver.afterPropertiesSet();

		exceptionResolvers.add(exceptionHandlerExceptionResolver);
		exceptionResolvers.addAll(exceptionResolversV2);
		exceptionResolvers.add(new ResponseStatusExceptionResolver());
		exceptionResolvers.add(new DefaultHandlerExceptionResolver());
	}

	@Override
	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException //NOSONAR
	{
		super.setApplicationContext(applicationContext);
		this.applicationContext = applicationContext;
	}

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer)
	{
		configurer.favorPathExtension(false).favorParameter(true);
	}

	@Bean
	public Docket apiDocumentation()
	{
		return new Docket(DocumentationType.SWAGGER_2)//
				.apiInfo(apiInfo())//
				.protocols(Sets.newHashSet(TravelcommercewebservicesConstants.HTTPS_PROTOCOL))
				.select()//
				.paths(PathSelectors.any())//
				.build()//
				.securitySchemes(securitySchemes())//
				.securityContexts(securityContexts());
	}

	@Bean
	public Docket searchApiDocumentation()
	{
		return new Docket(DocumentationType.SWAGGER_2)//
				.apiInfo(searchApiInfo())//
				.groupName("search")
				.protocols(Sets.newHashSet(TravelcommercewebservicesConstants.HTTPS_PROTOCOL))
				.select()//
				.paths(PathSelectors.regex(".*\\/search.*"))//
				.build()//
				.securitySchemes(securitySchemes())//
				.securityContexts(securityContexts());
	}

	protected ApiInfo apiInfo()
	{
		return new ApiInfoBuilder()//
				.title(getPropertyValue(TravelcommercewebservicesConstants.DOCUMENTATION_TITLE_PROPERTY))//
				.description(getPropertyValue(TravelcommercewebservicesConstants.DOCUMENTATION_DESC_PROPERTY))//
				.version(getPropertyValue(TravelcommercewebservicesConstants.API_VERSION))//
				.build();
	}

	protected ApiInfo searchApiInfo()
	{
		return new ApiInfoBuilder()//
				.title(getPropertyValue(TravelcommercewebservicesConstants.SEARCH_DOCUMENTATION_TITLE_PROPERTY))//
				.description(getPropertyValue(TravelcommercewebservicesConstants.SEARCH_DOCUMENTATION_DESC_PROPERTY))//
				.version(getPropertyValue(TravelcommercewebservicesConstants.API_VERSION))//
				.build();
	}


	protected List<SecurityContext> securityContexts()
	{
		return Arrays
				.asList(SecurityContext.builder().forPaths(PathSelectors.any()).securityReferences(securityReferences()).build());
	}

	protected List<SecurityScheme> securitySchemes()
	{
		return Arrays.asList(
				new ApiKey(getPropertyValue(TravelcommercewebservicesConstants.APIKEY_NAME_PROPERTY), getPropertyValue(TravelcommercewebservicesConstants.APIKEY_KEYNAME_PROPERTY),
						getPropertyValue(TravelcommercewebservicesConstants.APIKEY_PASSAS_PROPERTY)));
	}

	protected List<SecurityReference> securityReferences()
	{
		return Arrays.asList(
				SecurityReference.builder().reference(getPropertyValue(TravelcommercewebservicesConstants.APIKEY_NAME_PROPERTY)).scopes(new AuthorizationScope[0])
						.build());
	}

	protected String getPropertyValue(final String propertyName)
	{
		return configurationService.getConfiguration().getString(propertyName);
	}
}
