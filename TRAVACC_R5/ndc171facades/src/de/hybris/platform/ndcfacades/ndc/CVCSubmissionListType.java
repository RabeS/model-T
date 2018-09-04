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
 * <p>Java class for CVC_SubmissionListType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CVC_SubmissionListType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Illegible"/>
 *     &lt;enumeration value="Included"/>
 *     &lt;enumeration value="NotPresent"/>
 *     &lt;enumeration value="NotSubmitted"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "CVC_SubmissionListType")
@XmlEnum
public enum CVCSubmissionListType {


    /**
     * Card holder has stated CVC is illegible.
     * 
     */
    @XmlEnumValue("Illegible")
    ILLEGIBLE("Illegible"),

    /**
     * Airline has included CVC in the Auth / Purchase.
     * 
     */
    @XmlEnumValue("Included")
    INCLUDED("Included"),

    /**
     * Card holder has stated CVC is not present.
     * 
     */
    @XmlEnumValue("NotPresent")
    NOT_PRESENT("NotPresent"),

    /**
     * Airline has not submitted CVC.
     * 
     */
    @XmlEnumValue("NotSubmitted")
    NOT_SUBMITTED("NotSubmitted");
    private final String value;

    CVCSubmissionListType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CVCSubmissionListType fromValue(String v) {
        for (CVCSubmissionListType c: CVCSubmissionListType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}