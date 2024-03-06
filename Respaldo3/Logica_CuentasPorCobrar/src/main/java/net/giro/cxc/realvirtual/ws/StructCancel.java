
package net.giro.cxc.realvirtual.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para StructCancel complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="StructCancel">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="EstatusCFDI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RfcSolicitante" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Fecha" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="UUIDs" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NoSerieFirmante" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Descripcion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="state" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Acuse" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RutaDescargaAcuse" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EsCancelable" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EstatusCancelacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Mensaje" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StructCancel", propOrder = {
    "estatusCFDI",
    "rfcSolicitante",
    "fecha",
    "uuiDs",
    "noSerieFirmante",
    "descripcion",
    "state",
    "acuse",
    "rutaDescargaAcuse",
    "esCancelable",
    "estatusCancelacion",
    "mensaje"
})
public class StructCancel {

    @XmlElement(name = "EstatusCFDI")
    protected String estatusCFDI;
    @XmlElement(name = "RfcSolicitante")
    protected String rfcSolicitante;
    @XmlElement(name = "Fecha")
    protected String fecha;
    @XmlElement(name = "UUIDs")
    protected String uuiDs;
    @XmlElement(name = "NoSerieFirmante")
    protected String noSerieFirmante;
    @XmlElement(name = "Descripcion")
    protected String descripcion;
    protected int state;
    @XmlElement(name = "Acuse")
    protected String acuse;
    @XmlElement(name = "RutaDescargaAcuse")
    protected String rutaDescargaAcuse;
    @XmlElement(name = "EsCancelable")
    protected String esCancelable;
    @XmlElement(name = "EstatusCancelacion")
    protected String estatusCancelacion;
    @XmlElement(name = "Mensaje")
    protected String mensaje;

    /**
     * Obtiene el valor de la propiedad estatusCFDI.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEstatusCFDI() {
        return estatusCFDI;
    }

    /**
     * Define el valor de la propiedad estatusCFDI.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEstatusCFDI(String value) {
        this.estatusCFDI = value;
    }

    /**
     * Obtiene el valor de la propiedad rfcSolicitante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRfcSolicitante() {
        return rfcSolicitante;
    }

    /**
     * Define el valor de la propiedad rfcSolicitante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRfcSolicitante(String value) {
        this.rfcSolicitante = value;
    }

    /**
     * Obtiene el valor de la propiedad fecha.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFecha() {
        return fecha;
    }

    /**
     * Define el valor de la propiedad fecha.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFecha(String value) {
        this.fecha = value;
    }

    /**
     * Obtiene el valor de la propiedad uuiDs.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUUIDs() {
        return uuiDs;
    }

    /**
     * Define el valor de la propiedad uuiDs.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUUIDs(String value) {
        this.uuiDs = value;
    }

    /**
     * Obtiene el valor de la propiedad noSerieFirmante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNoSerieFirmante() {
        return noSerieFirmante;
    }

    /**
     * Define el valor de la propiedad noSerieFirmante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNoSerieFirmante(String value) {
        this.noSerieFirmante = value;
    }

    /**
     * Obtiene el valor de la propiedad descripcion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Define el valor de la propiedad descripcion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescripcion(String value) {
        this.descripcion = value;
    }

    /**
     * Obtiene el valor de la propiedad state.
     * 
     */
    public int getState() {
        return state;
    }

    /**
     * Define el valor de la propiedad state.
     * 
     */
    public void setState(int value) {
        this.state = value;
    }

    /**
     * Obtiene el valor de la propiedad acuse.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAcuse() {
        return acuse;
    }

    /**
     * Define el valor de la propiedad acuse.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAcuse(String value) {
        this.acuse = value;
    }

    /**
     * Obtiene el valor de la propiedad rutaDescargaAcuse.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRutaDescargaAcuse() {
        return rutaDescargaAcuse;
    }

    /**
     * Define el valor de la propiedad rutaDescargaAcuse.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRutaDescargaAcuse(String value) {
        this.rutaDescargaAcuse = value;
    }

    /**
     * Obtiene el valor de la propiedad esCancelable.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEsCancelable() {
        return esCancelable;
    }

    /**
     * Define el valor de la propiedad esCancelable.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEsCancelable(String value) {
        this.esCancelable = value;
    }

    /**
     * Obtiene el valor de la propiedad estatusCancelacion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEstatusCancelacion() {
        return estatusCancelacion;
    }

    /**
     * Define el valor de la propiedad estatusCancelacion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEstatusCancelacion(String value) {
        this.estatusCancelacion = value;
    }

    /**
     * Obtiene el valor de la propiedad mensaje.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMensaje() {
        return mensaje;
    }

    /**
     * Define el valor de la propiedad mensaje.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMensaje(String value) {
        this.mensaje = value;
    }

}
