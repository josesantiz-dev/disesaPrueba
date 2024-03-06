package net.giro.plataforma.seguridad.beans;

public class Ejecutable implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	long id;
	java.lang.String ejecutable;
	java.lang.String tipo;
	long creadoPor;
	java.util.Date fechaCreacion;
	long modificadoPor;
	java.util.Date fechaModificacion;
	net.giro.plataforma.seguridad.beans.Aplicacion aplicacion;


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public java.lang.String getEjecutable() {
		return ejecutable;
	}

	public void setEjecutable(java.lang.String ejecutable) {
		this.ejecutable = ejecutable;
	}

	public java.lang.String getTipo() {
		return tipo;
	}

	public void setTipo(java.lang.String tipo) {
		this.tipo = tipo;
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

	public net.giro.plataforma.seguridad.beans.Aplicacion getAplicacion() {
		return aplicacion;
	}

	public void setAplicacion(net.giro.plataforma.seguridad.beans.Aplicacion aplicacion) {
		this.aplicacion = aplicacion;
	}

}
