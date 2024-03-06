package net.giro.plataforma.beans;

public class PuestoExt implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	long id;
	long creadoPor;
	java.util.Date fechaCreacion;
	long modificadoPor;
	java.util.Date fechaModificacion;
	java.lang.String descripcion;
	java.lang.String correoPermisos;
	java.lang.String correoPrestamos;
	java.lang.String correoVacaciones;
	java.lang.String correoViaticos;
	java.lang.String correoIncidencias;
	java.lang.String correoActas;


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

	public java.lang.String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(java.lang.String descripcion) {
		this.descripcion = descripcion;
	}

	public java.lang.String getCorreoPermisos() {
		return correoPermisos;
	}

	public void setCorreoPermisos(java.lang.String correoPermisos) {
		this.correoPermisos = correoPermisos;
	}

	public java.lang.String getCorreoPrestamos() {
		return correoPrestamos;
	}

	public void setCorreoPrestamos(java.lang.String correoPrestamos) {
		this.correoPrestamos = correoPrestamos;
	}

	public java.lang.String getCorreoVacaciones() {
		return correoVacaciones;
	}

	public void setCorreoVacaciones(java.lang.String correoVacaciones) {
		this.correoVacaciones = correoVacaciones;
	}

	public java.lang.String getCorreoViaticos() {
		return correoViaticos;
	}

	public void setCorreoViaticos(java.lang.String correoViaticos) {
		this.correoViaticos = correoViaticos;
	}

	public java.lang.String getCorreoIncidencias() {
		return correoIncidencias;
	}

	public void setCorreoIncidencias(java.lang.String correoIncidencias) {
		this.correoIncidencias = correoIncidencias;
	}

	public java.lang.String getCorreoActas() {
		return correoActas;
	}

	public void setCorreoActas(java.lang.String correoActas) {
		this.correoActas = correoActas;
	}

}
