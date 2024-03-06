package net.giro.plataforma.seguridad.beans;

public class CampoTablaGrupo implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	long id;
	long creadoPor;
	java.util.Date fechaCreacion;
	long modificadoPor;
	java.util.Date fechaModificacion;
	net.giro.plataforma.seguridad.beans.CampoTabla tabla;
	net.giro.plataforma.seguridad.beans.CampoGrupo grupo;
	net.giro.plataforma.seguridad.beans.CampoAutorizacion autorizacion;


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

	public net.giro.plataforma.seguridad.beans.CampoTabla getTabla() {
		return tabla;
	}

	public void setTabla(net.giro.plataforma.seguridad.beans.CampoTabla tabla) {
		this.tabla = tabla;
	}

	public net.giro.plataforma.seguridad.beans.CampoGrupo getGrupo() {
		return grupo;
	}

	public void setGrupo(net.giro.plataforma.seguridad.beans.CampoGrupo grupo) {
		this.grupo = grupo;
	}

	public net.giro.plataforma.seguridad.beans.CampoAutorizacion getAutorizacion() {
		return autorizacion;
	}

	public void setAutorizacion(net.giro.plataforma.seguridad.beans.CampoAutorizacion autorizacion) {
		this.autorizacion = autorizacion;
	}

}
