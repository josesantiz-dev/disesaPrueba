
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
 *         &lt;element name="CancelTicketSCResult" type="{http://tempuri.org/}StructCancel" minOccurs="0"/>
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
    "cancelTicketSCResult"
})
@XmlRootElement(name = "CancelTicketSCResponse")
public class CancelTicketSCResponse {

    @XmlElement(name = "CancelTicketSCResult")
    protected StructCancel cancelTicketSCResult;

    /**
     * Gets the value of the cancelTicketSCResult property.
     * 
     * @return
     *     possible object is
     *     {@link StructCancel }
     *     
     */
    public StructCancel getCancelTicketSCResult() {
        return cancelTicketSCResult;
    }

    /**
     * Sets the value of the cancelTicketSCResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link StructCancel }
     *     
     */
    public void setCancelTicketSCResult(StructCancel value) {
        this.cancelTicketSCResult = value;
    }

}
