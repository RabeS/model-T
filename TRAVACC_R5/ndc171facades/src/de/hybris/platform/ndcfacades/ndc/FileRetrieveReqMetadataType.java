//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.08.04 at 02:01:34 PM IST 
//


package de.hybris.platform.ndcfacades.ndc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * File Retrieve Request Metadata definition.
 * 
 * <p>Java class for FileRetrieveReqMetadataType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FileRetrieveReqMetadataType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.iata.org/IATA/EDIST/2017.1}MediaMetadatas" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FileRetrieveReqMetadataType", propOrder = {
    "mediaMetadatas"
})
public class FileRetrieveReqMetadataType {

    @XmlElement(name = "MediaMetadatas")
    protected MediaMetadatas mediaMetadatas;

    /**
     * Gets the value of the mediaMetadatas property.
     * 
     * @return
     *     possible object is
     *     {@link MediaMetadatas }
     *     
     */
    public MediaMetadatas getMediaMetadatas() {
        return mediaMetadatas;
    }

    /**
     * Sets the value of the mediaMetadatas property.
     * 
     * @param value
     *     allowed object is
     *     {@link MediaMetadatas }
     *     
     */
    public void setMediaMetadatas(MediaMetadatas value) {
        this.mediaMetadatas = value;
    }

}