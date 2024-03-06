package net.giro.cxc.FEv33.complementos.nomina12;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element name="AccionesOTitulos" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="ValorMercado" use="required">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                       &lt;fractionDigits value="6"/>
 *                       &lt;minInclusive value="0.000001"/>
 *                       &lt;whiteSpace value="collapse"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *                 &lt;attribute name="PrecioAlOtorgarse" use="required">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                       &lt;fractionDigits value="6"/>
 *                       &lt;minInclusive value="0.000001"/>
 *                       &lt;whiteSpace value="collapse"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="HorasExtra" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="Dias" use="required">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *                       &lt;minInclusive value="1"/>
 *                       &lt;whiteSpace value="collapse"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *                 &lt;attribute name="TipoHoras" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/catalogos/Nomina}c_TipoHoras" />
 *                 &lt;attribute name="HorasExtra" use="required">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *                       &lt;minInclusive value="1"/>
 *                       &lt;whiteSpace value="collapse"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *                 &lt;attribute name="ImportePagado" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_ImporteMXN" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="TipoPercepcion" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/catalogos/Nomina}c_TipoPercepcion" />
 *       &lt;attribute name="Clave" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;minLength value="3"/>
 *             &lt;maxLength value="15"/>
 *             &lt;whiteSpace value="collapse"/>
 *             &lt;pattern value="[^|]{3,15}"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="Concepto" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;minLength value="1"/>
 *             &lt;maxLength value="100"/>
 *             &lt;whiteSpace value="collapse"/>
 *             &lt;pattern value="[^|]{1,100}"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="ImporteGravado" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_ImporteMXN" />
 *       &lt;attribute name="ImporteExento" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_ImporteMXN" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "accionesOTitulos",
    "horasExtra"
})
public class Percepcion implements Serializable {
	private static final long serialVersionUID = 1L;
    @XmlElement(name = "AccionesOTitulos")
    protected AccionesOTitulos accionesOTitulos;
    @XmlElement(name = "HorasExtra")
    protected List<HorasExtra> horasExtra;
    @XmlAttribute(name = "TipoPercepcion", required = true)
    protected String tipoPercepcion;
    @XmlAttribute(name = "Clave", required = true)
    protected String clave;
    @XmlAttribute(name = "Concepto", required = true)
    protected String concepto;
    @XmlAttribute(name = "ImporteGravado", required = true)
    protected BigDecimal importeGravado;
    @XmlAttribute(name = "ImporteExento", required = true)
    protected BigDecimal importeExento;

    /**
     * Gets the value of the accionesOTitulos property.
     * 
     * @return
     *     possible object is
     *     {@link Nomina.Percepciones.Percepcion.AccionesOTitulos }
     *     
     */
    public AccionesOTitulos getAccionesOTitulos() {
        return accionesOTitulos;
    }

    /**
     * Sets the value of the accionesOTitulos property.
     * 
     * @param value
     *     allowed object is
     *     {@link Nomina.Percepciones.Percepcion.AccionesOTitulos }
     *     
     */
    public void setAccionesOTitulos(AccionesOTitulos value) {
        this.accionesOTitulos = value;
    }

    /**
     * Gets the value of the horasExtra property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the horasExtra property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHorasExtra().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Nomina.Percepciones.Percepcion.HorasExtra }
     * 
     * 
     */
    public List<HorasExtra> getHorasExtra() {
        if (this.horasExtra == null)
        	this.horasExtra = new ArrayList<HorasExtra>();
        return this.horasExtra;
    }

    /**
     * Gets the value of the tipoPercepcion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoPercepcion() {
        return tipoPercepcion;
    }

    /**
     * Sets the value of the tipoPercepcion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoPercepcion(String value) {
        this.tipoPercepcion = value;
    }

    /**
     * Gets the value of the clave property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClave() {
        return clave;
    }

    /**
     * Sets the value of the clave property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClave(String value) {
        this.clave = value;
    }

    /**
     * Gets the value of the concepto property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConcepto() {
        return concepto;
    }

    /**
     * Sets the value of the concepto property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConcepto(String value) {
        this.concepto = value;
    }

    /**
     * Gets the value of the importeGravado property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getImporteGravado() {
        return importeGravado;
    }

    /**
     * Sets the value of the importeGravado property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setImporteGravado(BigDecimal value) {
        this.importeGravado = value;
    }

    /**
     * Gets the value of the importeExento property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getImporteExento() {
        return importeExento;
    }

    /**
     * Sets the value of the importeExento property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setImporteExento(BigDecimal value) {
        this.importeExento = value;
    }
}
