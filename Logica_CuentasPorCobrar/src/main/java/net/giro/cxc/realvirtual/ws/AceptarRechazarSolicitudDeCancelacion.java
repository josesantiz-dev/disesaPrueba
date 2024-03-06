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
 *         &lt;element name="xml" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="produccion" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="aceptacion" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
    "xml",
    "produccion",
    "aceptacion"
})
@XmlRootElement(name = "AceptarRechazarSolicitudDeCancelacion")
public class AceptarRechazarSolicitudDeCancelacion {
    protected String xml;
    protected boolean produccion;
    protected boolean aceptacion;

    /**
     * Obtiene el valor de la propiedad xml.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXml() {
        return xml;
    }

    /**
     * Define el valor de la propiedad xml.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXml(String value) {
        this.xml = value;
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

    /**
     * Obtiene el valor de la propiedad aceptacion.
     * 
     */
    public boolean isAceptacion() {
        return aceptacion;
    }

    /**
     * Define el valor de la propiedad aceptacion.
     * 
     */
    public void setAceptacion(boolean value) {
        this.aceptacion = value;
    }
}
