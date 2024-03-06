package net.giro.tyg.admon;

public class FormasPagos implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	long id;
	java.lang.String formaPago;
	java.lang.String aplicarAutomaticamente;
	java.lang.Long ahorro;
	java.lang.Long creadoPor;
	java.util.Date fechaCreacion;
	java.lang.Long modificadoPor;
	java.util.Date fechaModificacion;


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public java.lang.String getFormaPago() {
		return formaPago;
	}

	public void setFormaPago(java.lang.String formaPago) {
		this.formaPago = formaPago;
	}

	public java.lang.String getAplicarAutomaticamente() {
		return aplicarAutomaticamente;
	}

	public void setAplicarAutomaticamente(java.lang.String aplicarAutomaticamente) {
		this.aplicarAutomaticamente = aplicarAutomaticamente;
	}

	public java.lang.Long getAhorro() {
		return ahorro;
	}

	public void setAhorro(java.lang.Long ahorro) {
		this.ahorro = ahorro;
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
