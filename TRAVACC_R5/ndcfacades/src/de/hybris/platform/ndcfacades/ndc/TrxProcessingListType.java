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
 * <p>Java class for TrxProcessingListType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="TrxProcessingListType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Complete"/>
 *     &lt;enumeration value="InComplete"/>
 *     &lt;enumeration value="Unknown"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TrxProcessingListType")
@XmlEnum
public enum TrxProcessingListType {

    @XmlEnumValue("Complete")
    COMPLETE("Complete"),
    @XmlEnumValue("InComplete")
    IN_COMPLETE("InComplete"),
    @XmlEnumValue("Unknown")
    UNKNOWN("Unknown");
    private final String value;

    TrxProcessingListType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TrxProcessingListType fromValue(String v) {
        for (TrxProcessingListType c: TrxProcessingListType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
