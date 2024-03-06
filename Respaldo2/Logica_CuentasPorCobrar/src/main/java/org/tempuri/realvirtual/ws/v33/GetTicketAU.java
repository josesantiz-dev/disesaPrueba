
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
 *         &lt;element name="base64Cfd" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="rfcAdministrador" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cuentaAdministrador" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="claveAdministrador" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "base64Cfd",
    "rfcAdministrador",
    "cuentaAdministrador",
    "claveAdministrador"
})
@XmlRootElement(name = "GetTicketAU")
public class GetTicketAU {

    protected String base64Cfd;
    protected String rfcAdministrador;
    protected String cuentaAdministrador;
    protected String claveAdministrador;

    /**
     * Gets the value of the base64Cfd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBase64Cfd() {
        return base64Cfd;
    }

    /**
     * Sets the value of the base64Cfd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBase64Cfd(String value) {
        this.base64Cfd = value;
    }

    /**
     * Gets the value of the rfcAdministrador property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRfcAdministrador() {
        return rfcAdministrador;
    }

    /**
     * Sets the value of the rfcAdministrador property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRfcAdministrador(String value) {
        this.rfcAdministrador = value;
    }

    /**
     * Gets the value of the cuentaAdministrador property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCuentaAdministrador() {
        return cuentaAdministrador;
    }

    /**
     * Sets the value of the cuentaAdministrador property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCuentaAdministrador(String value) {
        this.cuentaAdministrador = value;
    }

    /**
     * Gets the value of the claveAdministrador property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClaveAdministrador() {
        return claveAdministrador;
    }

    /**
     * Sets the value of the claveAdministrador property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClaveAdministrador(String value) {
        this.claveAdministrador = value;
    }

}
