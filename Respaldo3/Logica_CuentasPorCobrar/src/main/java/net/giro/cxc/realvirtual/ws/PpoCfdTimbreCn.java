
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
     * Obtiene el valor de la propiedad evcTipoComprobante.
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
     * Define el valor de la propiedad evcTipoComprobante.
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
     * Obtiene el valor de la propiedad evcRfcEmisor.
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
     * Define el valor de la propiedad evcRfcEmisor.
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
     * Obtiene el valor de la propiedad evcRfcReceptor.
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
     * Define el valor de la propiedad evcRfcReceptor.
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
     * Obtiene el valor de la propiedad evcFechaExpedicion.
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
     * Define el valor de la propiedad evcFechaExpedicion.
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
     * Obtiene el valor de la propiedad evcTotal.
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
     * Define el valor de la propiedad evcTotal.
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
