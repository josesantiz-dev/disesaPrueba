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
 *       &lt;attribute name="DiasIncapacidad" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *             &lt;minInclusive value="1"/>
 *             &lt;whiteSpace value="collapse"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="TipoIncapacidad" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/catalogos/Nomina}c_TipoIncapacidad" />
 *       &lt;attribute name="ImporteMonetario" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_ImporteMXN" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
public class Incapacidad implements Serializable {
	private static final long serialVersionUID = 1L;
    @XmlAttribute(name = "DiasIncapacidad", required = true)
    protected int diasIncapacidad;
    @XmlAttribute(name = "TipoIncapacidad", required = true)
    protected String tipoIncapacidad;
    @XmlAttribute(name = "ImporteMonetario")
    protected BigDecimal importeMonetario;

    /**
     * Gets the value of the diasIncapacidad property.
     * 
     */
    public int getDiasIncapacidad() {
        return diasIncapacidad;
    }

    /**
     * Sets the value of the diasIncapacidad property.
     * 
     */
    public void setDiasIncapacidad(int value) {
        this.diasIncapacidad = value;
    }

    /**
     * Gets the value of the tipoIncapacidad property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoIncapacidad() {
        return tipoIncapacidad;
    }

    /**
     * Sets the value of the tipoIncapacidad property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoIncapacidad(String value) {
        this.tipoIncapacidad = value;
    }

    /**
     * Gets the value of the importeMonetario property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getImporteMonetario() {
        return importeMonetario;
    }

    /**
     * Sets the value of the importeMonetario property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setImporteMonetario(BigDecimal value) {
        this.importeMonetario = value;
    }
}
