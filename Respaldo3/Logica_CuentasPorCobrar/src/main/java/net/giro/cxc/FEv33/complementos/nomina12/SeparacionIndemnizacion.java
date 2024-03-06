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
 *       &lt;attribute name="TotalPagado" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_ImporteMXN" />
 *       &lt;attribute name="NumAñosServicio" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *             &lt;minInclusive value="0"/>
 *             &lt;maxInclusive value="99"/>
 *             &lt;whiteSpace value="collapse"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="UltimoSueldoMensOrd" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_ImporteMXN" />
 *       &lt;attribute name="IngresoAcumulable" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_ImporteMXN" />
 *       &lt;attribute name="IngresoNoAcumulable" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_ImporteMXN" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
public class SeparacionIndemnizacion implements Serializable {
	private static final long serialVersionUID = 1L;
	@XmlAttribute(name = "TotalPagado", required = true)
    protected BigDecimal totalPagado;
    @XmlAttribute(name = "NumA\u00f1osServicio", required = true)
    protected int numAñosServicio;
    @XmlAttribute(name = "UltimoSueldoMensOrd", required = true)
    protected BigDecimal ultimoSueldoMensOrd;
    @XmlAttribute(name = "IngresoAcumulable", required = true)
    protected BigDecimal ingresoAcumulable;
    @XmlAttribute(name = "IngresoNoAcumulable", required = true)
    protected BigDecimal ingresoNoAcumulable;

    /**
     * Gets the value of the totalPagado property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTotalPagado() {
        return totalPagado;
    }

    /**
     * Sets the value of the totalPagado property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTotalPagado(BigDecimal value) {
        this.totalPagado = value;
    }

    /**
     * Gets the value of the numAñosServicio property.
     * 
     */
    public int getNumAñosServicio() {
        return numAñosServicio;
    }

    /**
     * Sets the value of the numAñosServicio property.
     * 
     */
    public void setNumAñosServicio(int value) {
        this.numAñosServicio = value;
    }

    /**
     * Gets the value of the ultimoSueldoMensOrd property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getUltimoSueldoMensOrd() {
        return ultimoSueldoMensOrd;
    }

    /**
     * Sets the value of the ultimoSueldoMensOrd property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setUltimoSueldoMensOrd(BigDecimal value) {
        this.ultimoSueldoMensOrd = value;
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
