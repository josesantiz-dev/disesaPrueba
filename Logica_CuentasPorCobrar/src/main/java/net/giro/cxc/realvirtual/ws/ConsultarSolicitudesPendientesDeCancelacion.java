package net.giro.cxc.realvirtual.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Clase Java para anonymous complex type.
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RfcReceptor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="produccion" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "rfcReceptor",
    "produccion"
})
@XmlRootElement(name = "ConsultarSolicitudesPendientesDeCancelacion")
public class ConsultarSolicitudesPendientesDeCancelacion {
    @XmlElement(name = "RfcReceptor")
    protected String rfcReceptor;
    protected boolean produccion;

    /**
     * Obtiene el valor de la propiedad rfcReceptor.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRfcReceptor() {
        return rfcReceptor;
    }

    /**
     * Define el valor de la propiedad rfcReceptor.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRfcReceptor(String value) {
        this.rfcReceptor = value;
    }

    /**
     * Obtiene el valor de la propiedad produccion.
     * 
     */
    public boolean isProduccion() {
        return produccion;
    }

    /**
     * Define el valor de la propiedad produccion.
     * 
     */
    public void setProduccion(boolean value) {
        this.produccion = value;
    }
}
