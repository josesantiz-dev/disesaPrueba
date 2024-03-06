
package net.giro.cxc.realvirtual.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para anonymous complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
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
     * Obtiene el valor de la propiedad base64Cfd.
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
     * Define el valor de la propiedad base64Cfd.
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
     * Obtiene el valor de la propiedad rfcAdministrador.
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
     * Define el valor de la propiedad rfcAdministrador.
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
     * Obtiene el valor de la propiedad cuentaAdministrador.
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
     * Define el valor de la propiedad cuentaAdministrador.
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
     * Obtiene el valor de la propiedad claveAdministrador.
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
     * Define el valor de la propiedad claveAdministrador.
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
