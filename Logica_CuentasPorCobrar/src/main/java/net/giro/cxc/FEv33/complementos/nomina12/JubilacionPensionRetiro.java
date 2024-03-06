package net.giro.cxc.FEv33.complementos.nomina12;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
 *       &lt;attribute name="TotalUnaExhibicion" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_ImporteMXN" />
 *       &lt;attribute name="TotalParcialidad" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_ImporteMXN" />
 *       &lt;attribute name="MontoDiario" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_ImporteMXN" />
 *       &lt;attribute name="IngresoAcumulable" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_ImporteMXN" />
 *       &lt;attribute name="IngresoNoAcumulable" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_ImporteMXN" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
public class JubilacionPensionRetiro implements Serializable {
	private static final long serialVersionUID = 1L;
	@XmlAttribute(name = "TotalUnaExhibicion")
    protected BigDecimal totalUnaExhibicion;
    @XmlAttribute(name = "TotalParcialidad")
    protected BigDecimal totalParcialidad;
    @XmlAttribute(name = "MontoDiario")
    protected BigDecimal montoDiario;
    @XmlAttribute(name = "IngresoAcumulable", required = true)
    protected BigDecimal ingresoAcumulable;
    @XmlAttribute(name = "IngresoNoAcumulable", required = true)
    protected BigDecimal ingresoNoAcumulable;

    /**
     * Gets the value of the totalUnaExhibicion property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTotalUnaExhibicion() {
        return totalUnaExhibicion;
    }

    /**
     * Sets the value of the totalUnaExhibicion property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTotalUnaExhibicion(BigDecimal value) {
        this.totalUnaExhibicion = value;
    }

    /**
     * Gets the value of the totalParcialidad property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTotalParcialidad() {
        return totalParcialidad;
    }

    /**
     * Sets the value of the totalParcialidad property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTotalParcialidad(BigDecimal value) {
        this.totalParcialidad = value;
    }

    /**
     * Gets the value of the montoDiario property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMontoDiario() {
        return montoDiario;
    }

    /**
     * Sets the value of the montoDiario property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMontoDiario(BigDecimal value) {
        this.montoDiario = value;
    }

    /**
     * Gets the value of the ingresoAcumulable property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getIngresoAcumulable() {
        return ingresoAcumulable;
    }

    /**
     * Sets the value of the ingresoAcumulable property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setIngresoAcumulable(BigDecimal value) {
        this.ingresoAcumulable = value;
    }

    /**
     * Gets the value of the ingresoNoAcumulable property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getIngresoNoAcumulable() {
        return ingresoNoAcumulable;
    }

    /**
     * Sets the value of the ingresoNoAcumulable property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setIngresoNoAcumulable(BigDecimal value) {
        this.ingresoNoAcumulable = value;
    }
}
