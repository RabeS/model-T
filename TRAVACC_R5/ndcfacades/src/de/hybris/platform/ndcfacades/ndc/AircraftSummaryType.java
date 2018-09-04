//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.02.07 at 04:46:04 PM GMT 
//


package de.hybris.platform.ndcfacades.ndc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * AIRCRAFT SUMMARY representation.
 * 
 * <p>Java class for AircraftSummaryType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AircraftSummaryType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.iata.org/IATA/EDIST}AircraftCoreType">
 *       &lt;sequence>
 *         &lt;element name="Name" type="{http://www.iata.org/IATA/EDIST}ProperNameSimpleType" minOccurs="0"/>
 *         &lt;element name="AirlineEquipCode" type="{http://www.iata.org/IATA/EDIST}ContextSimpleType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AircraftSummaryType", propOrder = {
    "name",
    "airlineEquipCode"
})
public class AircraftSummaryType
    extends AircraftCoreType
{

    @XmlElement(name = "Name")
    protected String name;
    @XmlElement(name = "AirlineEquipCode")
    protected String airlineEquipCode;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the airlineEquipCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAirlineEquipCode() {
        return airlineEquipCode;
    }

    /**
     * Sets the value of the airlineEquipCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAirlineEquipCode(String value) {
        this.airlineEquipCode = value;
    }

}
