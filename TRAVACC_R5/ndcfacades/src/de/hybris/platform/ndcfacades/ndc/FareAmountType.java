//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.02.07 at 04:46:04 PM GMT 
//


package de.hybris.platform.ndcfacades.ndc;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FareAmountType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="FareAmountType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ADC"/>
 *     &lt;enumeration value="Bulk"/>
 *     &lt;enumeration value="IT"/>
 *     &lt;enumeration value="NOADC"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "FareAmountType")
@XmlEnum
public enum FareAmountType {


    /**
     * Additional collection
     * 
     */
    ADC("ADC"),
    @XmlEnumValue("Bulk")
    BULK("Bulk"),
    IT("IT"),
    NOADC("NOADC");
    private final String value;

    FareAmountType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static FareAmountType fromValue(String v) {
        for (FareAmountType c: FareAmountType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
