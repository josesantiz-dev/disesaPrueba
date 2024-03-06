package net.giro.plataforma.seguridad.beans;

public class PerfilAccesoDetalle implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	long id;
	long creadoPor;
	java.util.Date fechaCreacion;
	long modificadoPor;
	java.util.Date fechaModificacion;
	net.giro.plataforma.seguridad.beans.PerfilAcceso perfilAcceso;
	java.lang.String tipoPerfil;
	java.lang.String valorPerfil;


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

	public net.giro.plataforma.seguridad.beans.PerfilAcceso getPerfilAcceso() {
		return perfilAcceso;
	}

	public void setPerfilAcceso(net.giro.plataforma.seguridad.beans.PerfilAcceso perfilAcceso) {
		this.perfilAcceso = perfilAcceso;
	}

	public java.lang.String getTipoPerfil() {
		return tipoPerfil;
	}

	public void setTipoPerfil(java.lang.String tipoPerfil) {
		this.tipoPerfil = tipoPerfil;
	}

	public java.lang.String getValorPerfil() {
		return valorPerfil;
	}

	public void setValorPerfil(java.lang.String valorPerfil) {
		this.valorPerfil = valorPerfil;
	}

}
