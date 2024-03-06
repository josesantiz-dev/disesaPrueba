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
 *       &lt;attribute name="OrigenRecurso" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/catalogos/Nomina}c_OrigenRecurso" />
 *       &lt;attribute name="MontoRecursoPropio" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_ImporteMXN" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
public class EntidadSNCF implements Serializable {
	private static final long serialVersionUID = 1L;
	@XmlAttribute(name = "OrigenRecurso", required = true)
    protected COrigenRecurso origenRecurso;
    @XmlAttribute(name = "MontoRecursoPropio")
    protected BigDecimal montoRecursoPropio;

    /**
     * Gets the value of the origenRecurso property.
     * 
     * @return
     *     possible object is
     *     {@link COrigenRecurso }
     *     
     */
    public COrigenRecurso getOrigenRecurso() {
        return origenRecurso;
    }

    /**
     * Sets the value of the origenRecurso property.
     * 
     * @param value
     *     allowed object is
     *     {@link COrigenRecurso }
     *     
     */
    public void setOrigenRecurso(COrigenRecurso value) {
        this.origenRecurso = value;
    }

    /**
     * Gets the value of the montoRecursoPropio property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMontoRecursoPropio() {
        return montoRecursoPropio;
    }

    /**
     * Sets the value of the montoRecursoPropio property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMontoRecursoPropio(BigDecimal value) {
        this.montoRecursoPropio = value;
    }
}
