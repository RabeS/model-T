//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.02.07 at 04:46:04 PM GMT 
//


package de.hybris.platform.ndcfacades.ndc;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Traveler" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://www.iata.org/IATA/EDIST}AnonymousTraveler" maxOccurs="unbounded" minOccurs="0"/>
 *                   &lt;element ref="{http://www.iata.org/IATA/EDIST}RecognizedTraveler" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "traveler"
})
@XmlRootElement(name = "Travelers")
public class Travelers {

    @XmlElement(name = "Traveler", required = true)
    protected List<Travelers.Traveler> traveler;

    /**
     * Gets the value of the traveler property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the traveler property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTraveler().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Travelers.Traveler }
     * 
     * 
     */
    public List<Travelers.Traveler> getTraveler() {
        if (traveler == null) {
            traveler = new ArrayList<Travelers.Traveler>();
        }
        return this.traveler;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element ref="{http://www.iata.org/IATA/EDIST}AnonymousTraveler" maxOccurs="unbounded" minOccurs="0"/>
     *         &lt;element ref="{http://www.iata.org/IATA/EDIST}RecognizedTraveler" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "anonymousTraveler",
        "recognizedTraveler"
    })
    public static class Traveler {

        @XmlElement(name = "AnonymousTraveler")
        protected List<AnonymousTravelerType> anonymousTraveler;
        @XmlElement(name = "RecognizedTraveler")
        protected List<TravelerDetailType> recognizedTraveler;

        /**
         * Gets the value of the anonymousTraveler property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the anonymousTraveler property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getAnonymousTraveler().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link AnonymousTravelerType }
         * 
         * 
         */
        public List<AnonymousTravelerType> getAnonymousTraveler() {
            if (anonymousTraveler == null) {
                anonymousTraveler = new ArrayList<AnonymousTravelerType>();
            }
            return this.anonymousTraveler;
        }

        /**
         * Gets the value of the recognizedTraveler property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the recognizedTraveler property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getRecognizedTraveler().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link TravelerDetailType }
         * 
         * 
         */
        public List<TravelerDetailType> getRecognizedTraveler() {
            if (recognizedTraveler == null) {
                recognizedTraveler = new ArrayList<TravelerDetailType>();
            }
            return this.recognizedTraveler;
        }

    }

}
