
package org.tempuri.realvirtual.ws.v32;

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
 *         &lt;element name="GetTicketSimpleResult" type="{http://tempuri.org/}StructCfd"/>
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
    "getTicketSimpleResult"
})
@XmlRootElement(name = "GetTicketSimpleResponse")
public class GetTicketSimpleResponse {

    @XmlElement(name = "GetTicketSimpleResult", required = true)
    protected StructCfd getTicketSimpleResult;

    /**
     * Gets the value of the getTicketSimpleResult property.
     * 
     * @return
     *     possible object is
     *     {@link StructCfd }
     *     
     */
    public StructCfd getGetTicketSimpleResult() {
        return getTicketSimpleResult;
    }

    /**
     * Sets the value of the getTicketSimpleResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link StructCfd }
     *     
     */
    public void setGetTicketSimpleResult(StructCfd value) {
        this.getTicketSimpleResult = value;
    }

}
