
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
 *         &lt;element name="CancelTicketExtendedResult" type="{http://tempuri.org/}StructCancelExtended"/>
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
    "cancelTicketExtendedResult"
})
@XmlRootElement(name = "CancelTicketExtendedResponse")
public class CancelTicketExtendedResponse {

    @XmlElement(name = "CancelTicketExtendedResult", required = true)
    protected StructCancelExtended cancelTicketExtendedResult;

    /**
     * Gets the value of the cancelTicketExtendedResult property.
     * 
     * @return
     *     possible object is
     *     {@link StructCancelExtended }
     *     
     */
    public StructCancelExtended getCancelTicketExtendedResult() {
        return cancelTicketExtendedResult;
    }

    /**
     * Sets the value of the cancelTicketExtendedResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link StructCancelExtended }
     *     
     */
    public void setCancelTicketExtendedResult(StructCancelExtended value) {
        this.cancelTicketExtendedResult = value;
    }

}
