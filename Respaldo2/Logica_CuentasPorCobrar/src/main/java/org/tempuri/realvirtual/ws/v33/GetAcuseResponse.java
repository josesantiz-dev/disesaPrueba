
package org.tempuri.realvirtual.ws.v33;

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
 *         &lt;element name="GetAcuseResult" type="{http://tempuri.org/}StructAcuse" minOccurs="0"/>
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
    "getAcuseResult"
})
@XmlRootElement(name = "GetAcuseResponse")
public class GetAcuseResponse {

    @XmlElement(name = "GetAcuseResult")
    protected StructAcuse getAcuseResult;

    /**
     * Gets the value of the getAcuseResult property.
     * 
     * @return
     *     possible object is
     *     {@link StructAcuse }
     *     
     */
    public StructAcuse getGetAcuseResult() {
        return getAcuseResult;
    }

    /**
     * Sets the value of the getAcuseResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link StructAcuse }
     *     
     */
    public void setGetAcuseResult(StructAcuse value) {
        this.getAcuseResult = value;
    }

}
