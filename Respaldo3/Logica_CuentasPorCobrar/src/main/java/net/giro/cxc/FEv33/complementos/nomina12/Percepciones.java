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
 *         &lt;element name="Percepcion" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="AccionesOTitulos" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="ValorMercado" use="required">
 *                             &lt;simpleType>
 *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                                 &lt;fractionDigits value="6"/>
 *                                 &lt;minInclusive value="0.000001"/>
 *                                 &lt;whiteSpace value="collapse"/>
 *                               &lt;/restriction>
 *                             &lt;/simpleType>
 *                           &lt;/attribute>
 *                           &lt;attribute name="PrecioAlOtorgarse" use="required">
 *                             &lt;simpleType>
 *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                                 &lt;fractionDigits value="6"/>
 *                                 &lt;minInclusive value="0.000001"/>
 *                                 &lt;whiteSpace value="collapse"/>
 *                               &lt;/restriction>
 *                             &lt;/simpleType>
 *                           &lt;/attribute>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="HorasExtra" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="Dias" use="required">
 *                             &lt;simpleType>
 *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *                                 &lt;minInclusive value="1"/>
 *                                 &lt;whiteSpace value="collapse"/>
 *                               &lt;/restriction>
 *                             &lt;/simpleType>
 *                           &lt;/attribute>
 *                           &lt;attribute name="TipoHoras" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/catalogos/Nomina}c_TipoHoras" />
 *                           &lt;attribute name="HorasExtra" use="required">
 *                             &lt;simpleType>
 *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *                                 &lt;minInclusive value="1"/>
 *                                 &lt;whiteSpace value="collapse"/>
 *                               &lt;/restriction>
 *                             &lt;/simpleType>
 *                           &lt;/attribute>
 *                           &lt;attribute name="ImportePagado" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_ImporteMXN" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *                 &lt;attribute name="TipoPercepcion" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/catalogos/Nomina}c_TipoPercepcion" />
 *                 &lt;attribute name="Clave" use="required">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                       &lt;minLength value="3"/>
 *                       &lt;maxLength value="15"/>
 *                       &lt;whiteSpace value="collapse"/>
 *                       &lt;pattern value="[^|]{3,15}"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *                 &lt;attribute name="Concepto" use="required">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                       &lt;minLength value="1"/>
 *                       &lt;maxLength value="100"/>
 *                       &lt;whiteSpace value="collapse"/>
 *                       &lt;pattern value="[^|]{1,100}"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *                 &lt;attribute name="ImporteGravado" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_ImporteMXN" />
 *                 &lt;attribute name="ImporteExento" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_ImporteMXN" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="JubilacionPensionRetiro" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="TotalUnaExhibicion" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_ImporteMXN" />
 *                 &lt;attribute name="TotalParcialidad" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_ImporteMXN" />
 *                 &lt;attribute name="MontoDiario" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_ImporteMXN" />
 *                 &lt;attribute name="IngresoAcumulable" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_ImporteMXN" />
 *                 &lt;attribute name="IngresoNoAcumulable" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_ImporteMXN" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="SeparacionIndemnizacion" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="TotalPagado" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_ImporteMXN" />
 *                 &lt;attribute name="NumAñosServicio" use="required">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *                       &lt;minInclusive value="0"/>
 *                       &lt;maxInclusive value="99"/>
 *                       &lt;whiteSpace value="collapse"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *                 &lt;attribute name="UltimoSueldoMensOrd" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_ImporteMXN" />
 *                 &lt;attribute name="IngresoAcumulable" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_ImporteMXN" />
 *                 &lt;attribute name="IngresoNoAcumulable" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_ImporteMXN" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="TotalSueldos" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_ImporteMXN" />
 *       &lt;attribute name="TotalSeparacionIndemnizacion" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_ImporteMXN" />
 *       &lt;attribute name="TotalJubilacionPensionRetiro" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_ImporteMXN" />
 *       &lt;attribute name="TotalGravado" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_ImporteMXN" />
 *       &lt;attribute name="TotalExento" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_ImporteMXN" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "percepcion",
    "jubilacionPensionRetiro",
    "separacionIndemnizacion"
})
public class Percepciones implements Serializable {
	private static final long serialVersionUID = 1L;
    @XmlElement(name = "Percepcion", required = true)
    protected List<Percepcion> percepcion;
    @XmlElement(name = "JubilacionPensionRetiro")
    protected JubilacionPensionRetiro jubilacionPensionRetiro;
    @XmlElement(name = "SeparacionIndemnizacion")
    protected SeparacionIndemnizacion separacionIndemnizacion;
    @XmlAttribute(name = "TotalSueldos")
    protected BigDecimal totalSueldos;
    @XmlAttribute(name = "TotalSeparacionIndemnizacion")
    protected BigDecimal totalSeparacionIndemnizacion;
    @XmlAttribute(name = "TotalJubilacionPensionRetiro")
    protected BigDecimal totalJubilacionPensionRetiro;
    @XmlAttribute(name = "TotalGravado", required = true)
    protected BigDecimal totalGravado;
    @XmlAttribute(name = "TotalExento", required = true)
    protected BigDecimal totalExento;

    /**
     * Gets the value of the percepcion property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the percepcion property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPercepcion().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Percepcion }
     * 
     * 
     */
    public List<Percepcion> getPercepcion() {
        if (percepcion == null) {
            percepcion = new ArrayList<Percepcion>();
        }
        return this.percepcion;
    }

    /**
     * Gets the value of the jubilacionPensionRetiro property.
     * 
     * @return
     *     possible object is
     *     {@link JubilacionPensionRetiro }
     *     
     */
    public JubilacionPensionRetiro getJubilacionPensionRetiro() {
        return jubilacionPensionRetiro;
    }

    /**
     * Sets the value of the jubilacionPensionRetiro property.
     * 
     * @param value
     *     allowed object is
     *     {@link JubilacionPensionRetiro }
     *     
     */
    public void setJubilacionPensionRetiro(JubilacionPensionRetiro value) {
        this.jubilacionPensionRetiro = value;
    }

    /**
     * Gets the value of the separacionIndemnizacion property.
     * 
     * @return
     *     possible object is
     *     {@link SeparacionIndemnizacion }
     *     
     */
    public SeparacionIndemnizacion getSeparacionIndemnizacion() {
        return separacionIndemnizacion;
    }

    /**
     * Sets the value of the separacionIndemnizacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link SeparacionIndemnizacion }
     *     
     */
    public void setSeparacionIndemnizacion(SeparacionIndemnizacion value) {
        this.separacionIndemnizacion = value;
    }

    /**
     * Gets the value of the totalSueldos property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTotalSueldos() {
        return totalSueldos;
    }

    /**
     * Sets the value of the totalSueldos property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTotalSueldos(BigDecimal value) {
        this.totalSueldos = value;
    }

    /**
     * Gets the value of the totalSeparacionIndemnizacion property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTotalSeparacionIndemnizacion() {
        return totalSeparacionIndemnizacion;
    }

    /**
     * Sets the value of the totalSeparacionIndemnizacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTotalSeparacionIndemnizacion(BigDecimal value) {
        this.totalSeparacionIndemnizacion = value;
    }

    /**
     * Gets the value of the totalJubilacionPensionRetiro property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTotalJubilacionPensionRetiro() {
        return totalJubilacionPensionRetiro;
    }

    /**
     * Sets the value of the totalJubilacionPensionRetiro property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTotalJubilacionPensionRetiro(BigDecimal value) {
        this.totalJubilacionPensionRetiro = value;
    }

    /**
     * Gets the value of the totalGravado property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTotalGravado() {
        return totalGravado;
    }

    /**
     * Sets the value of the totalGravado property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTotalGravado(BigDecimal value) {
        this.totalGravado = value;
    }

    /**
     * Gets the value of the totalExento property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTotalExento() {
        return totalExento;
    }

    /**
     * Sets the value of the totalExento property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTotalExento(BigDecimal value) {
        this.totalExento = value;
    }
}
