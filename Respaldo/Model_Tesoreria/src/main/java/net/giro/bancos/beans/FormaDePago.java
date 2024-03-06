package net.giro.bancos.beans;

public class FormaDePago implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	long id;
	long creadoPor;
	java.util.Date fechaCreacion;
	long modificadoPor;
	java.util.Date fechaModificacion;
	java.lang.String formaDePago;
	long aplicaAutomaticamente;
	long ahorro;


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(long creadoPor) {
		this.creadoPor = creadoPor;
	}

	public java.util.Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(java.util.Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public long getModificadoPor() {
		return modificadoPor;
	}

	public void setModificadoPor(long modificadoPor) {
		this.modificadoPor = modificadoPor;
	}

	public java.util.Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(java.util.Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public java.lang.String getFormaDePago() {
		return formaDePago;
	}

	public void setFormaDePago(java.lang.String formaDePago) {
		this.formaDePago = formaDePago;
	}

	public long getAplicaAutomaticamente() {
		return aplicaAutomaticamente;
	}

	public void setAplicaAutomaticamente(long aplicaAutomaticamente) {
		this.aplicaAutomaticamente = aplicaAutomaticamente;
	}

	public long getAhorro() {
		return ahorro;
	}

	public void setAhorro(long ahorro) {
		this.ahorro = ahorro;
	}

}
