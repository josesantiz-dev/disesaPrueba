package net.giro.cxp.beans;

public class Gastos implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	java.lang.Long id;
	java.lang.String nombre;
	java.lang.String ctaContable;
	java.math.BigDecimal costo;
	java.lang.Long creadoPor;
	java.util.Date fechaCreacion;
	java.lang.Long modificadoPor;
	java.util.Date fechaModificacion;


	public java.lang.Long getId() {
		return id;
	}

	public void setId(java.lang.Long id) {
		this.id = id;
	}

	public java.lang.String getNombre() {
		return nombre;
	}

	public void setNombre(java.lang.String nombre) {
		this.nombre = nombre;
	}

	public java.lang.String getCtaContable() {
		return ctaContable;
	}

	public void setCtaContable(java.lang.String ctaContable) {
		this.ctaContable = ctaContable;
	}

	public java.math.BigDecimal getCosto() {
		return costo;
	}

	public void setCosto(java.math.BigDecimal costo) {
		this.costo = costo;
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

}
