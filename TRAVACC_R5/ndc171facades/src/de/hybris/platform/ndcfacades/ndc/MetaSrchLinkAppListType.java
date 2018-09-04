//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.08.04 at 02:01:34 PM IST 
//


package de.hybris.platform.ndcfacades.ndc;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MetaSrchLinkAppListType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="MetaSrchLinkAppListType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="AirlineSite"/>
 *     &lt;enumeration value="IntraService"/>
 *     &lt;enumeration value="MetaSearchSite"/>
 *     &lt;enumeration value="SellerAgencySite"/>
 *     &lt;enumeration value="Other"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "MetaSrchLinkAppListType")
@XmlEnum
public enum MetaSrchLinkAppListType {

    @XmlEnumValue("AirlineSite")
    AIRLINE_SITE("AirlineSite"),
    @XmlEnumValue("IntraService")
    INTRA_SERVICE("IntraService"),
    @XmlEnumValue("MetaSearchSite")
    META_SEARCH_SITE("MetaSearchSite"),
    @XmlEnumValue("SellerAgencySite")
    SELLER_AGENCY_SITE("SellerAgencySite"),
    @XmlEnumValue("Other")
    OTHER("Other");
    private final String value;

    MetaSrchLinkAppListType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static MetaSrchLinkAppListType fromValue(String v) {
        for (MetaSrchLinkAppListType c: MetaSrchLinkAppListType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
