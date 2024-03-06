
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
 *         &lt;element name="GetStatusSatResult" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "getStatusSatResult"
})
@XmlRootElement(name = "GetStatusSatResponse")
public class GetStatusSatResponse {

    @XmlElement(name = "GetStatusSatResult")
    protected int getStatusSatResult;

    /**
     * Gets the value of the getStatusSatResult property.
     * 
     */
    public int getGetStatusSatResult() {
        return getStatusSatResult;
    }

    /**
     * Sets the value of the getStatusSatResult property.
     * 
     */
    public void setGetStatusSatResult(int value) {
        this.getStatusSatResult = value;
    }

}
