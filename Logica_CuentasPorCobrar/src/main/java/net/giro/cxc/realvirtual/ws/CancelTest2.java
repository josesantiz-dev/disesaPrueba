
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
 *         &lt;element name="CFDIB64" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CerEmisorB64" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LlaveEmisorB64" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EmailReceptor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "cfdib64",
    "cerEmisorB64",
    "llaveEmisorB64",
    "emailReceptor"
})
@XmlRootElement(name = "CancelTest2")
public class CancelTest2 {

    @XmlElement(name = "CFDIB64")
    protected String cfdib64;
    @XmlElement(name = "CerEmisorB64")
    protected String cerEmisorB64;
    @XmlElement(name = "LlaveEmisorB64")
    protected String llaveEmisorB64;
    @XmlElement(name = "EmailReceptor")
    protected String emailReceptor;

    /**
     * Obtiene el valor de la propiedad cfdib64.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCFDIB64() {
        return cfdib64;
    }

    /**
     * Define el valor de la propiedad cfdib64.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCFDIB64(String value) {
        this.cfdib64 = value;
    }

    /**
     * Obtiene el valor de la propiedad cerEmisorB64.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCerEmisorB64() {
        return cerEmisorB64;
    }

    /**
     * Define el valor de la propiedad cerEmisorB64.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCerEmisorB64(String value) {
        this.cerEmisorB64 = value;
    }

    /**
     * Obtiene el valor de la propiedad llaveEmisorB64.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLlaveEmisorB64() {
        return llaveEmisorB64;
    }

    /**
     * Define el valor de la propiedad llaveEmisorB64.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLlaveEmisorB64(String value) {
        this.llaveEmisorB64 = value;
    }

    /**
     * Obtiene el valor de la propiedad emailReceptor.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmailReceptor() {
        return emailReceptor;
    }

    /**
     * Define el valor de la propiedad emailReceptor.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmailReceptor(String value) {
        this.emailReceptor = value;
    }

}
