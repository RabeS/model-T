package de.hybris.platform.travelcommercewebservices.conv;

import de.hybris.platform.travelcommercewebservices.constants.TravelcommercewebservicesConstants;

import com.thoughtworks.xstream.converters.SingleValueConverter;

import java.util.Optional;

public class ImageUrlConverter implements SingleValueConverter
{
    @Override
    public String toString(Object o)
    {
        return Optional.ofNullable(o)
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .map(this::addRootContext)
                .orElseGet(() -> null);
    }

    protected String addRootContext(final String imageUrl){
        return new StringBuilder(TravelcommercewebservicesConstants.V2_ROOT_CONTEXT)
                .append(imageUrl)
                .toString();
    }

    @Override
    public Object fromString(String s)
    {
        return null;
    }

    @Override
    public boolean canConvert(Class type)
    {
        return type == String.class;
    }
}
