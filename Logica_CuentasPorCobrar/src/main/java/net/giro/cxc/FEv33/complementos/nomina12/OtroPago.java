package net.giro.cxc.FEv33.complementos.nomina12;

import java.io.Serializable;
import java.math.BigDecimal;

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
 *         &lt;element name="SubsidioAlEmpleo" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="SubsidioCausado" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_ImporteMXN" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="CompensacionSaldosAFavor" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="SaldoAFavor" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_ImporteMXN" />
 *                 &lt;attribute name="Año" use="required">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}short">
 *                       &lt;minInclusive value="2016"/>
 *                       &lt;whiteSpace value="collapse"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *                 &lt;attribute name="RemanenteSalFav" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_ImporteMXN" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="TipoOtroPago" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/catalogos/Nomina}c_TipoOtroPago" />
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
 *       &lt;attribute name="Importe" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_ImporteMXN" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "subsidioAlEmpleo",
    "compensacionSaldosAFavor"
})
public class OtroPago implements Serializable {
	private static final long serialVersionUID = 1L;
    @XmlElement(name = "SubsidioAlEmpleo")
    protected SubsidioAlEmpleo subsidioAlEmpleo;
    @XmlElement(name = "CompensacionSaldosAFavor")
    protected CompensacionSaldosAFavor compensacionSaldosAFavor;
    @XmlAttribute(name = "TipoOtroPago", required = true)
    protected String tipoOtroPago;
    @XmlAttribute(name = "Clave", required = true)
    protected String clave;
    @XmlAttribute(name = "Concepto", required = true)
    protected String concepto;
    @XmlAttribute(name = "Importe", required = true)
    protected BigDecimal importe;

    /**
     * Gets the value of the subsidioAlEmpleo property.
     * 
     * @return
     *     possible object is
     *     {@link SubsidioAlEmpleo }
     *     
     */
    public SubsidioAlEmpleo getSubsidioAlEmpleo() {
        return subsidioAlEmpleo;
    }

    /**
     * Sets the value of the subsidioAlEmpleo property.
     * 
     * @param value
     *     allowed object is
     *     {@link SubsidioAlEmpleo }
     *     
     */
    public void setSubsidioAlEmpleo(SubsidioAlEmpleo value) {
        this.subsidioAlEmpleo = value;
    }

    /**
     * Gets the value of the compensacionSaldosAFavor property.
     * 
     * @return
     *     possible object is
     *     {@link CompensacionSaldosAFavor }
     *     
     */
    public CompensacionSaldosAFavor getCompensacionSaldosAFavor() {
        return compensacionSaldosAFavor;
    }

    /**
     * Sets the value of the compensacionSaldosAFavor property.
     * 
     * @param value
     *     allowed object is
     *     {@link CompensacionSaldosAFavor }
     *     
     */
    public void setCompensacionSaldosAFavor(CompensacionSaldosAFavor value) {
        this.compensacionSaldosAFavor = value;
    }

    /**
     * Gets the value of the tipoOtroPago property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoOtroPago() {
        return tipoOtroPago;
    }

    /**
     * Sets the value of the tipoOtroPago property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoOtroPago(String value) {
        this.tipoOtroPago = value;
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
     * Gets the value of the importe property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getImporte() {
        return importe;
    }

    /**
     * Sets the value of the importe property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setImporte(BigDecimal value) {
        this.importe = value;
    }
}
