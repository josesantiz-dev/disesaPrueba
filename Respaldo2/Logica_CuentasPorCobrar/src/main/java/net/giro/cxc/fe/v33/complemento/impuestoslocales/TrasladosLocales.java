package net.giro.cxc.fe.v33.complemento.impuestoslocales;

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
 *       &lt;attribute name="ImpLocTrasladado" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="TasadeTraslado" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *             &lt;fractionDigits value="2"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="Importe" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *             &lt;fractionDigits value="2"/>
 *             &lt;whiteSpace value="collapse"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
public class TrasladosLocales implements Serializable {
	private static final long serialVersionUID = 5270858056617869629L;

    @XmlAttribute(name = "ImpLocTrasladado", required = true)
    protected String impLocTrasladado;
    @XmlAttribute(name = "TasadeTraslado", required = true)
    protected BigDecimal tasadeTraslado;
    @XmlAttribute(name = "Importe", required = true)
    protected BigDecimal importe;

    /**
     * Gets the value of the impLocTrasladado property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getImpLocTrasladado() {
        return impLocTrasladado;
    }

    /**
     * Sets the value of the impLocTrasladado property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImpLocTrasladado(String value) {
        this.impLocTrasladado = value;
    }

    /**
     * Gets the value of the tasadeTraslado property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTasadeTraslado() {
        return tasadeTraslado;
    }

    /**
     * Sets the value of the tasadeTraslado property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTasadeTraslado(BigDecimal value) {
        this.tasadeTraslado = value;
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
