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
 *       &lt;attribute name="SaldoAFavor" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_ImporteMXN" />
 *       &lt;attribute name="Año" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}short">
 *             &lt;minInclusive value="2016"/>
 *             &lt;whiteSpace value="collapse"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="RemanenteSalFav" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_ImporteMXN" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
public class CompensacionSaldosAFavor implements Serializable {
	private static final long serialVersionUID = 1L;
    @XmlAttribute(name = "SaldoAFavor", required = true)
    protected BigDecimal saldoAFavor;
    @XmlAttribute(name = "A\u00f1o", required = true)
    protected short año;
    @XmlAttribute(name = "RemanenteSalFav", required = true)
    protected BigDecimal remanenteSalFav;

    /**
     * Gets the value of the saldoAFavor property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSaldoAFavor() {
        return saldoAFavor;
    }

    /**
     * Sets the value of the saldoAFavor property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSaldoAFavor(BigDecimal value) {
        this.saldoAFavor = value;
    }

    /**
     * Gets the value of the año property.
     * 
     */
    public short getAño() {
        return año;
    }

    /**
     * Sets the value of the año property.
     * 
     */
    public void setAño(short value) {
        this.año = value;
    }

    /**
     * Gets the value of the remanenteSalFav property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRemanenteSalFav() {
        return remanenteSalFav;
    }

    /**
     * Sets the value of the remanenteSalFav property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRemanenteSalFav(BigDecimal value) {
        this.remanenteSalFav = value;
    }
}
