package net.giro.plataforma.seguridad.beans;

public class Responsabilidad implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	long id;
	java.lang.String responsabilidad;
	java.lang.String descripcion;
	long creadoPor;
	java.util.Date fechaCreacion;
	long modificadoPor;
	java.util.Date fechaModificacion;
	long grupoReporteId;
	net.giro.plataforma.seguridad.beans.Menu menu;
	net.giro.plataforma.seguridad.beans.Aplicacion aplicacion;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public java.lang.String getResponsabilidad() {
		return responsabilidad;
	}

	public void setResponsabilidad(java.lang.String responsabilidad) {
		this.responsabilidad = responsabilidad;
	}

	public java.lang.String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(java.lang.String descripcion) {
		this.descripcion = descripcion;
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

	public long getGrupoReporteId() {
		return grupoReporteId;
	}

	public void setGrupoReporteId(long grupoReporteId) {
		this.grupoReporteId = grupoReporteId;
	}

	public net.giro.plataforma.seguridad.beans.Menu getMenu() {
		return menu;
	}

	public void setMenu(net.giro.plataforma.seguridad.beans.Menu menu) {
		this.menu = menu;
	}

	public net.giro.plataforma.seguridad.beans.Aplicacion getAplicacion() {
		return aplicacion;
	}

	public void setAplicacion(net.giro.plataforma.seguridad.beans.Aplicacion aplicacion) {
		this.aplicacion = aplicacion;
	}

}
