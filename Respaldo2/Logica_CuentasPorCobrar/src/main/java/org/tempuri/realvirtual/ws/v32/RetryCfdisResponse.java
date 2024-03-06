
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
 *         &lt;element name="RetryCfdisResult" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "retryCfdisResult"
})
@XmlRootElement(name = "RetryCfdisResponse")
public class RetryCfdisResponse {

    @XmlElement(name = "RetryCfdisResult")
    protected int retryCfdisResult;

    /**
     * Gets the value of the retryCfdisResult property.
     * 
     */
    public int getRetryCfdisResult() {
        return retryCfdisResult;
    }

    /**
     * Sets the value of the retryCfdisResult property.
     * 
     */
    public void setRetryCfdisResult(int value) {
        this.retryCfdisResult = value;
    }

}
