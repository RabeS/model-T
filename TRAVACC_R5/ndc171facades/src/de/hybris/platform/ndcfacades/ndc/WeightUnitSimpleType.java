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
 * <p>Java class for WeightUnitSimpleType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="WeightUnitSimpleType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Kilogram"/>
 *     &lt;enumeration value="Pound"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "WeightUnitSimpleType")
@XmlEnum
public enum WeightUnitSimpleType {

    @XmlEnumValue("Kilogram")
    KILOGRAM("Kilogram"),
    @XmlEnumValue("Pound")
    POUND("Pound");
    private final String value;

    WeightUnitSimpleType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static WeightUnitSimpleType fromValue(String v) {
        for (WeightUnitSimpleType c: WeightUnitSimpleType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}