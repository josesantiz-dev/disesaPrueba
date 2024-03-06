
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
 *         &lt;element name="totalfactura" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="rfcreceptor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "totalfactura",
    "rfcreceptor"
})
@XmlRootElement(name = "CancelTicketExt")
public class CancelTicketExt {

    protected String base64Cfd;
    protected String totalfactura;
    protected String rfcreceptor;

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
     * Obtiene el valor de la propiedad totalfactura.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotalfactura() {
        return totalfactura;
    }

    /**
     * Define el valor de la propiedad totalfactura.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotalfactura(String value) {
        this.totalfactura = value;
    }

    /**
     * Obtiene el valor de la propiedad rfcreceptor.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRfcreceptor() {
        return rfcreceptor;
    }

    /**
     * Define el valor de la propiedad rfcreceptor.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRfcreceptor(String value) {
        this.rfcreceptor = value;
    }

}
