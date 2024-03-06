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
 *       &lt;attribute name="Dias" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *             &lt;minInclusive value="1"/>
 *             &lt;whiteSpace value="collapse"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="TipoHoras" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/catalogos/Nomina}c_TipoHoras" />
 *       &lt;attribute name="HorasExtra" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *             &lt;minInclusive value="1"/>
 *             &lt;whiteSpace value="collapse"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="ImportePagado" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_ImporteMXN" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
public class HorasExtra implements Serializable {
	private static final long serialVersionUID = 1L;
    @XmlAttribute(name = "Dias", required = true)
    protected int dias;
    @XmlAttribute(name = "TipoHoras", required = true)
    protected String tipoHoras;
    @XmlAttribute(name = "HorasExtra", required = true)
    protected int horasExtra;
    @XmlAttribute(name = "ImportePagado", required = true)
    protected BigDecimal importePagado;

    /**
     * Gets the value of the dias property.
     * 
     */
    public int getDias() {
        return dias;
    }

    /**
     * Sets the value of the dias property.
     * 
     */
    public void setDias(int value) {
        this.dias = value;
    }

    /**
     * Gets the value of the tipoHoras property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoHoras() {
        return tipoHoras;
    }

    /**
     * Sets the value of the tipoHoras property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoHoras(String value) {
        this.tipoHoras = value;
    }

    /**
     * Gets the value of the horasExtra property.
     * 
     */
    public int getHorasExtra() {
        return horasExtra;
    }

    /**
     * Sets the value of the horasExtra property.
     * 
     */
    public void setHorasExtra(int value) {
        this.horasExtra = value;
    }

    /**
     * Gets the value of the importePagado property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getImportePagado() {
        return importePagado;
    }

    /**
     * Sets the value of the importePagado property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setImportePagado(BigDecimal value) {
        this.importePagado = value;
    }
}
