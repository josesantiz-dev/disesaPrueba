
package org.tempuri.realvirtual.ws.v32;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for StructCancel complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="StructCancel">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RfcSolicitante" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Fecha" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="UUIDs" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NoSerieFirmante" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Descripcion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="state" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "rfcSolicitante",
    "fecha",
    "uuiDs",
    "noSerieFirmante",
    "descripcion",
    "state"
})
public class StructCancel {

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

    /**
     * Gets the value of the rfcSolicitante property.
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
     * Sets the value of the rfcSolicitante property.
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
     * Gets the value of the fecha property.
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
     * Sets the value of the fecha property.
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
     * Gets the value of the uuiDs property.
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
     * Sets the value of the uuiDs property.
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
     * Gets the value of the noSerieFirmante property.
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
     * Sets the value of the noSerieFirmante property.
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
     * Gets the value of the descripcion property.
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
     * Sets the value of the descripcion property.
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
     * Gets the value of the state property.
     * 
     */
    public int getState() {
        return state;
    }

    /**
     * Sets the value of the state property.
     * 
     */
    public void setState(int value) {
        this.state = value;
    }

}
