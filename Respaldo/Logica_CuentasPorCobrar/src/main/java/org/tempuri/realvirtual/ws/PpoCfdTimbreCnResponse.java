
package org.tempuri.realvirtual.ws;

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
 *         &lt;element name="PpoCfdTimbre_CnResult" type="{http://tempuri.org/}ClienteUsuarioTimbre"/>
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
    "ppoCfdTimbreCnResult"
})
@XmlRootElement(name = "PpoCfdTimbre_CnResponse")
public class PpoCfdTimbreCnResponse {

    @XmlElement(name = "PpoCfdTimbre_CnResult", required = true)
    protected ClienteUsuarioTimbre ppoCfdTimbreCnResult;

    /**
     * Gets the value of the ppoCfdTimbreCnResult property.
     * 
     * @return
     *     possible object is
     *     {@link ClienteUsuarioTimbre }
     *     
     */
    public ClienteUsuarioTimbre getPpoCfdTimbreCnResult() {
        return ppoCfdTimbreCnResult;
    }

    /**
     * Sets the value of the ppoCfdTimbreCnResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ClienteUsuarioTimbre }
     *     
     */
    public void setPpoCfdTimbreCnResult(ClienteUsuarioTimbre value) {
        this.ppoCfdTimbreCnResult = value;
    }

}
