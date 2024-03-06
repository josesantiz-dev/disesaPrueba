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
 *       &lt;attribute name="ValorMercado" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *             &lt;fractionDigits value="6"/>
 *             &lt;minInclusive value="0.000001"/>
 *             &lt;whiteSpace value="collapse"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="PrecioAlOtorgarse" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *             &lt;fractionDigits value="6"/>
 *             &lt;minInclusive value="0.000001"/>
 *             &lt;whiteSpace value="collapse"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
public class AccionesOTitulos implements Serializable {
	private static final long serialVersionUID = 1L;
    @XmlAttribute(name = "ValorMercado", required = true)
    protected BigDecimal valorMercado;
    @XmlAttribute(name = "PrecioAlOtorgarse", required = true)
    protected BigDecimal precioAlOtorgarse;

    /**
     * Gets the value of the valorMercado property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getValorMercado() {
        return valorMercado;
    }

    /**
     * Sets the value of the valorMercado property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setValorMercado(BigDecimal value) {
        this.valorMercado = value;
    }

    /**
     * Gets the value of the precioAlOtorgarse property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPrecioAlOtorgarse() {
        return precioAlOtorgarse;
    }

    /**
     * Sets the value of the precioAlOtorgarse property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPrecioAlOtorgarse(BigDecimal value) {
        this.precioAlOtorgarse = value;
    }
}
