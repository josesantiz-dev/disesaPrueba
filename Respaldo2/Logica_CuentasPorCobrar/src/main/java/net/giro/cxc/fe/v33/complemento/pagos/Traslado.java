package net.giro.cxc.fe.v33.complemento.pagos;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import net.giro.cxc.fe.v33.CTipoFactor;

/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="Impuesto" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/catalogos}c_Impuesto" />
 *       &lt;attribute name="TipoFactor" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/catalogos}c_TipoFactor" />
 *       &lt;attribute name="TasaOCuota" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *             &lt;fractionDigits value="6"/>
 *             &lt;minInclusive value="0.000000"/>
 *             &lt;whiteSpace value="collapse"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="Importe" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_Importe" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlType(name="")
@XmlAccessorType(XmlAccessType.FIELD)
public class Traslado implements Serializable {
    private static final long serialVersionUID = 2564078182020707778L;
    
	@XmlAttribute(name="Impuesto", required=true)
    protected String impuesto;
    @XmlAttribute(name="TipoFactor", required=true)
    protected CTipoFactor tipoFactor;
    @XmlAttribute(name="TasaOCuota", required=true)
    protected BigDecimal tasaOCuota;
    @XmlAttribute(name="Importe", required=true)
    protected BigDecimal importe;

    /**
     * Gets the value of the impuesto property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getImpuesto() {
        return impuesto;
    }

    /**
     * Sets the value of the impuesto property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImpuesto(String value) {
        this.impuesto=value;
    }

    /**
     * Gets the value of the tipoFactor property.
     * 
     * @return
     *     possible object is
     *     {@link CTipoFactor }
     *     
     */
    public CTipoFactor getTipoFactor() {
        return tipoFactor;
    }

    /**
     * Sets the value of the tipoFactor property.
     * 
     * @param value
     *     allowed object is
     *     {@link CTipoFactor }
     *     
     */
    public void setTipoFactor(CTipoFactor value) {
        this.tipoFactor=value;
    }

    /**
     * Gets the value of the tasaOCuota property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTasaOCuota() {
        return tasaOCuota;
    }

    /**
     * Sets the value of the tasaOCuota property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTasaOCuota(BigDecimal value) {
        this.tasaOCuota=value;
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
        this.importe=value;
    }
}
