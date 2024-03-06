
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
 *         &lt;element name="TimbrarCDClaveResult" type="{http://tempuri.org/}StructCfd" minOccurs="0"/>
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
    "timbrarCDClaveResult"
})
@XmlRootElement(name = "TimbrarCDClaveResponse")
public class TimbrarCDClaveResponse {

    @XmlElement(name = "TimbrarCDClaveResult")
    protected StructCfd timbrarCDClaveResult;

    /**
     * Gets the value of the timbrarCDClaveResult property.
     * 
     * @return
     *     possible object is
     *     {@link StructCfd }
     *     
     */
    public StructCfd getTimbrarCDClaveResult() {
        return timbrarCDClaveResult;
    }

    /**
     * Sets the value of the timbrarCDClaveResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link StructCfd }
     *     
     */
    public void setTimbrarCDClaveResult(StructCfd value) {
        this.timbrarCDClaveResult = value;
    }

}
