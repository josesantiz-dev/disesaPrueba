package net.giro.cargas.documentos.beans;

import java.io.Serializable;
import java.math.BigDecimal;

import org.simpleframework.xml.Attribute;


public class TrasladosLocales implements Serializable {
	private static final long serialVersionUID = 5270858056617869629L;

    @Attribute(name = "imploctrasladado", required=false)
    private String impLocTrasladado;
    @Attribute(name = "tasadetraslado", required=false)
    private BigDecimal tasadeTraslado;
    @Attribute(name = "importe", required=false)
    private BigDecimal importe;

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
