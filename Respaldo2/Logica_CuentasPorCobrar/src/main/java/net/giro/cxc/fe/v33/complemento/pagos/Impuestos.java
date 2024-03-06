package net.giro.cxc.fe.v33.complemento.pagos;

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
 *         &lt;element name="Retenciones" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Retencion" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="Impuesto" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/catalogos}c_Impuesto" />
 *                           &lt;attribute name="Importe" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_Importe" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Traslados" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Traslado" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="Impuesto" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/catalogos}c_Impuesto" />
 *                           &lt;attribute name="TipoFactor" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/catalogos}c_TipoFactor" />
 *                           &lt;attribute name="TasaOCuota" use="required">
 *                             &lt;simpleType>
 *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                                 &lt;fractionDigits value="6"/>
 *                                 &lt;minInclusive value="0.000000"/>
 *                                 &lt;whiteSpace value="collapse"/>
 *                               &lt;/restriction>
 *                             &lt;/simpleType>
 *                           &lt;/attribute>
 *                           &lt;attribute name="Importe" use="required" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_Importe" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="TotalImpuestosRetenidos" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_Importe" />
 *       &lt;attribute name="TotalImpuestosTrasladados" type="{http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI}t_Importe" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlType(name="", propOrder={"retenciones", "traslados"})
@XmlAccessorType(XmlAccessType.FIELD)
public class Impuestos implements Serializable {
	private static final long serialVersionUID = 4048608142412693614L;
	
	@XmlElement(name="Retenciones")
    protected Retenciones retenciones;
    @XmlElement(name="Traslados")
    protected Traslados traslados;
    @XmlAttribute(name="TotalImpuestosRetenidos")
    protected BigDecimal totalImpuestosRetenidos;
    @XmlAttribute(name="TotalImpuestosTrasladados")
    protected BigDecimal totalImpuestosTrasladados;

    /**
     * Gets the value of the retenciones property.
     * 
     * @return
     *     possible object is
     *     {@link Pagos.Pago.Impuestos.Retenciones }
     *     
     */
    public Retenciones getRetenciones() {
        return retenciones;
    }

    /**
     * Sets the value of the retenciones property.
     * 
     * @param value
     *     allowed object is
     *     {@link Pagos.Pago.Impuestos.Retenciones }
     *     
     */
    public void setRetenciones(Retenciones value) {
        this.retenciones=value;
    }

    /**
     * Gets the value of the traslados property.
     * 
     * @return
     *     possible object is
     *     {@link Pagos.Pago.Impuestos.Traslados }
     *     
     */
    public Traslados getTraslados() {
        return traslados;
    }

    /**
     * Sets the value of the traslados property.
     * 
     * @param value
     *     allowed object is
     *     {@link Pagos.Pago.Impuestos.Traslados }
     *     
     */
    public void setTraslados(Traslados value) {
        this.traslados=value;
    }

    /**
     * Gets the value of the totalImpuestosRetenidos property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTotalImpuestosRetenidos() {
        return totalImpuestosRetenidos;
    }

    /**
     * Sets the value of the totalImpuestosRetenidos property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTotalImpuestosRetenidos(BigDecimal value) {
        this.totalImpuestosRetenidos=value;
    }

    /**
     * Gets the value of the totalImpuestosTrasladados property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTotalImpuestosTrasladados() {
        return totalImpuestosTrasladados;
    }

    /**
     * Sets the value of the totalImpuestosTrasladados property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTotalImpuestosTrasladados(BigDecimal value) {
        this.totalImpuestosTrasladados=value;
    }
}
