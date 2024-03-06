package net.giro.plataforma.seguridad.beans;

public class CampoAutorizacion implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	long id;
	long creadoPor;
	java.util.Date fechaCreacion;
	long modificadoPor;
	java.util.Date fechaModificacion;
	long autorizo;
	long usuarioId;
	long expirado;
	long autorizacionUnica;
	java.util.Date fechaIni;
	java.util.Date fechaFin;


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

	public long getAutorizo() {
		return autorizo;
	}

	public void setAutorizo(long autorizo) {
		this.autorizo = autorizo;
	}

	public long getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(long usuarioId) {
		this.usuarioId = usuarioId;
	}

	public long getExpirado() {
		return expirado;
	}

	public void setExpirado(long expirado) {
		this.expirado = expirado;
	}

	public long getAutorizacionUnica() {
		return autorizacionUnica;
	}

	public void setAutorizacionUnica(long autorizacionUnica) {
		this.autorizacionUnica = autorizacionUnica;
	}

	public java.util.Date getFechaIni() {
		return fechaIni;
	}

	public void setFechaIni(java.util.Date fechaIni) {
		this.fechaIni = fechaIni;
	}

	public java.util.Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(java.util.Date fechaFin) {
		this.fechaFin = fechaFin;
	}

}
