package net.giro.cargas.documentos.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;

public class ImpuestosLocales implements Serializable {
	private static final long serialVersionUID = 1L;

    @Attribute(name="version",required=false)
    private String version;
    @Attribute(name="totalderetenciones",required=false)
    private BigDecimal totaldeRetenciones;
    @Attribute(name="totaldetraslados",required=false)
    private BigDecimal totaldeTraslados;
	@ElementList(name="retencioneslocales", required=false)
	private List<RetencionesLocales> retencionesLocales;
	@ElementList(name="trasladoslocales", required=false)
	private List<TrasladosLocales> trasladosLocales;


    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        if (this.version == null)
            this.version = "1.0";
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * Gets the value of the totaldeRetenciones property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTotaldeRetenciones() {
        return totaldeRetenciones;
    }

    /**
     * Sets the value of the totaldeRetenciones property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTotaldeRetenciones(BigDecimal value) {
        this.totaldeRetenciones = value;
    }

    /**
     * Gets the value of the totaldeTraslados property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTotaldeTraslados() {
        return totaldeTraslados;
    }

    /**
     * Sets the value of the totaldeTraslados property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTotaldeTraslados(BigDecimal value) {
        this.totaldeTraslados = value;
    }

	public List<RetencionesLocales> getRetencionesLocales() {
		return retencionesLocales;
	}

	public void setRetencionesLocales(List<RetencionesLocales> retencionesLocales) {
		this.retencionesLocales = retencionesLocales;
	}

	public List<TrasladosLocales> getTrasladosLocales() {
		return trasladosLocales;
	}

	public void setTrasladosLocales(List<TrasladosLocales> trasladosLocales) {
		this.trasladosLocales = trasladosLocales;
	}
}
