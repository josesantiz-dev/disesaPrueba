
package org.tempuri.realvirtual.ws.v33;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element name="evcTipoComprobante" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="evcRfcEmisor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="evcRfcReceptor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="evcFechaExpedicion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="evcTotal" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "evcTipoComprobante",
    "evcRfcEmisor",
    "evcRfcReceptor",
    "evcFechaExpedicion",
    "evcTotal"
})
@XmlRootElement(name = "PpoCfdTimbre_Cn")
public class PpoCfdTimbreCn {

    protected String evcTipoComprobante;
    protected String evcRfcEmisor;
    protected String evcRfcReceptor;
    protected String evcFechaExpedicion;
    protected String evcTotal;

    /**
     * Gets the value of the evcTipoComprobante property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEvcTipoComprobante() {
        return evcTipoComprobante;
    }

    /**
     * Sets the value of the evcTipoComprobante property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEvcTipoComprobante(String value) {
        this.evcTipoComprobante = value;
    }

    /**
     * Gets the value of the evcRfcEmisor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEvcRfcEmisor() {
        return evcRfcEmisor;
    }

    /**
     * Sets the value of the evcRfcEmisor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEvcRfcEmisor(String value) {
        this.evcRfcEmisor = value;
    }

    /**
     * Gets the value of the evcRfcReceptor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEvcRfcReceptor() {
        return evcRfcReceptor;
    }

    /**
     * Sets the value of the evcRfcReceptor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEvcRfcReceptor(String value) {
        this.evcRfcReceptor = value;
    }

    /**
     * Gets the value of the evcFechaExpedicion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEvcFechaExpedicion() {
        return evcFechaExpedicion;
    }

    /**
     * Sets the value of the evcFechaExpedicion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEvcFechaExpedicion(String value) {
        this.evcFechaExpedicion = value;
    }

    /**
     * Gets the value of the evcTotal property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEvcTotal() {
        return evcTotal;
    }

    /**
     * Sets the value of the evcTotal property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEvcTotal(String value) {
        this.evcTotal = value;
    }

}
