package net.giro.cxp.beans;

public class Retenciones implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	java.lang.Long id;
	java.lang.String retencion;
	java.lang.String ctaContable;
	java.lang.Long creadoPor;
	java.util.Date fechaCreacion;
	java.lang.Long modificadoPor;
	java.util.Date fechaModificacion;
	java.lang.String impuesto;
	java.lang.String ctaImpuesto;


	public java.lang.Long getId() {
		return id;
	}

	public void setId(java.lang.Long id) {
		this.id = id;
	}

	public java.lang.String getRetencion() {
		return retencion;
	}

	public void setRetencion(java.lang.String retencion) {
		this.retencion = retencion;
	}

	public java.lang.String getCtaContable() {
		return ctaContable;
	}

	public void setCtaContable(java.lang.String ctaContable) {
		this.ctaContable = ctaContable;
	}

	public java.lang.Long getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(java.lang.Long creadoPor) {
		this.creadoPor = creadoPor;
	}

	public java.util.Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(java.util.Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public java.lang.Long getModificadoPor() {
		return modificadoPor;
	}

	public void setModificadoPor(java.lang.Long modificadoPor) {
		this.modificadoPor = modificadoPor;
	}

	public java.util.Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(java.util.Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public java.lang.String getImpuesto() {
		return impuesto;
	}

	public void setImpuesto(java.lang.String impuesto) {
		this.impuesto = impuesto;
	}

	public java.lang.String getCtaImpuesto() {
		return ctaImpuesto;
	}

	public void setCtaImpuesto(java.lang.String ctaImpuesto) {
		this.ctaImpuesto = ctaImpuesto;
	}

}
