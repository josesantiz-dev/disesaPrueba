
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
 *         &lt;element name="rfc_emisor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="usuario_ws" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="clave_ws" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="base64Cfd" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cadena_xml" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ClaveServicio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "rfcEmisor",
    "usuarioWs",
    "claveWs",
    "base64Cfd",
    "cadenaXml",
    "claveServicio"
})
@XmlRootElement(name = "TimbrarCDClave")
public class TimbrarCDClave {

    @XmlElement(name = "rfc_emisor")
    protected String rfcEmisor;
    @XmlElement(name = "usuario_ws")
    protected String usuarioWs;
    @XmlElement(name = "clave_ws")
    protected String claveWs;
    protected String base64Cfd;
    @XmlElement(name = "cadena_xml")
    protected String cadenaXml;
    @XmlElement(name = "ClaveServicio")
    protected String claveServicio;

    /**
     * Gets the value of the rfcEmisor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRfcEmisor() {
        return rfcEmisor;
    }

    /**
     * Sets the value of the rfcEmisor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRfcEmisor(String value) {
        this.rfcEmisor = value;
    }

    /**
     * Gets the value of the usuarioWs property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsuarioWs() {
        return usuarioWs;
    }

    /**
     * Sets the value of the usuarioWs property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsuarioWs(String value) {
        this.usuarioWs = value;
    }

    /**
     * Gets the value of the claveWs property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClaveWs() {
        return claveWs;
    }

    /**
     * Sets the value of the claveWs property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClaveWs(String value) {
        this.claveWs = value;
    }

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
     * Gets the value of the cadenaXml property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCadenaXml() {
        return cadenaXml;
    }

    /**
     * Sets the value of the cadenaXml property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCadenaXml(String value) {
        this.cadenaXml = value;
    }

    /**
     * Gets the value of the claveServicio property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClaveServicio() {
        return claveServicio;
    }

    /**
     * Sets the value of the claveServicio property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClaveServicio(String value) {
        this.claveServicio = value;
    }

}
