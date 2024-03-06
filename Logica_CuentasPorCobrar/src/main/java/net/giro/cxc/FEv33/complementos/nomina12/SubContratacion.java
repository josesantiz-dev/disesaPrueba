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
 *       &lt;attribute name="RfcLabora" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_RFC" />
 *       &lt;attribute name="PorcentajeTiempo" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *             &lt;minInclusive value="0.001"/>
 *             &lt;maxInclusive value="100.000"/>
 *             &lt;whiteSpace value="collapse"/>
 *             &lt;pattern value="[0-9]{1,3}(.([0-9]{1,3}))?"/>
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
public class SubContratacion implements Serializable {
	private static final long serialVersionUID = 1L;
    @XmlAttribute(name = "RfcLabora", required = true)
    protected String rfcLabora;
    @XmlAttribute(name = "PorcentajeTiempo", required = true)
    protected BigDecimal porcentajeTiempo;

    /**
     * Gets the value of the rfcLabora property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRfcLabora() {
        return rfcLabora;
    }

    /**
     * Sets the value of the rfcLabora property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRfcLabora(String value) {
        this.rfcLabora = value;
    }

    /**
     * Gets the value of the porcentajeTiempo property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPorcentajeTiempo() {
        return porcentajeTiempo;
    }

    /**
     * Sets the value of the porcentajeTiempo property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPorcentajeTiempo(BigDecimal value) {
        this.porcentajeTiempo = value;
    }
}
