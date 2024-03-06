package net.giro.plataforma.seguridad.beans;

public class Menu implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	long id;
	net.giro.plataforma.seguridad.beans.Aplicacion aplicaciones;
	java.lang.String menu;
	java.lang.String descripcion;
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

	public net.giro.plataforma.seguridad.beans.Aplicacion getAplicaciones() {
		return aplicaciones;
	}

	public void setAplicaciones(net.giro.plataforma.seguridad.beans.Aplicacion aplicaciones) {
		this.aplicaciones = aplicaciones;
	}

	public java.lang.String getMenu() {
		return menu;
	}

	public void setMenu(java.lang.String menu) {
		this.menu = menu;
	}

	public java.lang.String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(java.lang.String descripcion) {
		this.descripcion = descripcion;
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
