//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.08.04 at 02:01:34 PM IST 
//


package de.hybris.platform.ndcfacades.ndc;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CustomerAuthStatusListType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CustomerAuthStatusListType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="A"/>
 *     &lt;enumeration value="N"/>
 *     &lt;enumeration value="U"/>
 *     &lt;enumeration value="Y"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "CustomerAuthStatusListType")
@XmlEnum
public enum CustomerAuthStatusListType {


    /**
     * 
     *             A- An authentication attempt occurred but could not be completed
     *           
     * 
     */
    A,

    /**
     * N- Customer not authenticated
     * 
     */
    N,

    /**
     * U- Unable to perform authentication
     * 
     */
    U,

    /**
     * Y- Customer authenticated
     * 
     */
    Y;

    public String value() {
        return name();
    }

    public static CustomerAuthStatusListType fromValue(String v) {
        return valueOf(v);
    }

}
