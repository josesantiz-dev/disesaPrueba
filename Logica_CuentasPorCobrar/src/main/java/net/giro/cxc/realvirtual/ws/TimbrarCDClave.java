
package net.giro.cxc.realvirtual.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
     * Obtiene el valor de la propiedad rfcEmisor.
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
     * Define el valor de la propiedad rfcEmisor.
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
     * Obtiene el valor de la propiedad usuarioWs.
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
     * Define el valor de la propiedad usuarioWs.
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
     * Obtiene el valor de la propiedad claveWs.
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
     * Define el valor de la propiedad claveWs.
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
     * Obtiene el valor de la propiedad cadenaXml.
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
     * Define el valor de la propiedad cadenaXml.
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
     * Obtiene el valor de la propiedad claveServicio.
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
     * Define el valor de la propiedad claveServicio.
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
