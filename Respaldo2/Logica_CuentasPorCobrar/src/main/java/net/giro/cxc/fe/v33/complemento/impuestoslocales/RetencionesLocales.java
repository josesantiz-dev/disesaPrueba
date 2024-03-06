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
 *       &lt;attribute name="ImpLocRetenido" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="TasadeRetencion" use="required">
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
public class RetencionesLocales implements Serializable {
	private static final long serialVersionUID = -974882843388661734L;
	
	@XmlAttribute(name = "ImpLocRetenido", required = true)
    protected String impLocRetenido;
    @XmlAttribute(name = "TasadeRetencion", required = true)
    protected BigDecimal tasadeRetencion;
    @XmlAttribute(name = "Importe", required = true)
    protected BigDecimal importe;

    /**
     * Gets the value of the impLocRetenido property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getImpLocRetenido() {
        return impLocRetenido;
    }

    /**
     * Sets the value of the impLocRetenido property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImpLocRetenido(String value) {
        this.impLocRetenido = value;
    }

    /**
     * Gets the value of the tasadeRetencion property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTasadeRetencion() {
        return tasadeRetencion;
    }

    /**
     * Sets the value of the tasadeRetencion property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTasadeRetencion(BigDecimal value) {
        this.tasadeRetencion = value;
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
