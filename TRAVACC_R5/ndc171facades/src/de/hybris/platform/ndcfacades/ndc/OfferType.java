//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.08.04 at 02:01:34 PM IST 
//


package de.hybris.platform.ndcfacades.ndc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * Offer definition.
 * 
 * <p>Java class for OfferType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OfferType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.iata.org/IATA/EDIST/2017.1}OfferItemDetailType">
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OfferType")
@XmlSeeAlso({
    de.hybris.platform.ndcfacades.ndc.ShopProductRS.Response.Offers.Offer.class,
    de.hybris.platform.ndcfacades.ndc.ItinReshopRS.Response.ReShopOffers.ReShopOffer.class,
    de.hybris.platform.ndcfacades.ndc.InvDiscrepencyAlertType.AlternateOffers.AlternateOffer.class,
    de.hybris.platform.ndcfacades.ndc.FlightPriceRS.AirlineOffers.AirlineOffer.class,
    de.hybris.platform.ndcfacades.ndc.ItinReshopAlertsType.OrderItemExceptions.Exception.ExpiredTimeLimits.ExpiredTimeLimit.AlternateOffers.AlternateOffer.class,
    de.hybris.platform.ndcfacades.ndc.ItinReshopAlertsType.OrderItemExceptions.Exception.InvDiscrepency.AlternateOffers.AlternateOffer.class,
    de.hybris.platform.ndcfacades.ndc.ItinReshopAlertsType.OrderItemExceptions.Exception.Eligibility.AlternateOffers.AlternateOffer.class
})
public class OfferType
    extends OfferItemDetailType
{


}