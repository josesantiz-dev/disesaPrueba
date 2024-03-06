package net.giro.cargas.documentos.beans;

import java.io.Serializable;
import java.math.BigDecimal;

import org.simpleframework.xml.Attribute;

public class RetencionesLocales implements Serializable {
	private static final long serialVersionUID = -974882843388661734L;
	
	@Attribute(name = "implocretenido", required=false)
    private String impLocRetenido;
    @Attribute(name = "tasaderetencion", required=false)
    private BigDecimal tasadeRetencion;
    @Attribute(name = "importe", required=false)
    private BigDecimal importe;

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
