package net.giro.plataforma.seguridad.beans;

public class GrupoReportesDet implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	long id;
	net.giro.plataforma.seguridad.beans.GrupoReportes grupoPertenece;
	net.giro.plataforma.seguridad.beans.GrupoReportes grupo;
	net.giro.plataforma.seguridad.beans.Ejecutable ejecutable;
	java.lang.String prompt;
	long creadoPor;
	long modificadoPor;
	java.util.Date fechaCreacion;
	java.util.Date fechaModificacion;


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public net.giro.plataforma.seguridad.beans.GrupoReportes getGrupoPertenece() {
		return grupoPertenece;
	}

	public void setGrupoPertenece(net.giro.plataforma.seguridad.beans.GrupoReportes grupoPertenece) {
		this.grupoPertenece = grupoPertenece;
	}

	public net.giro.plataforma.seguridad.beans.GrupoReportes getGrupo() {
		return grupo;
	}

	public void setGrupo(net.giro.plataforma.seguridad.beans.GrupoReportes grupo) {
		this.grupo = grupo;
	}

	public net.giro.plataforma.seguridad.beans.Ejecutable getEjecutable() {
		return ejecutable;
	}

	public void setEjecutable(net.giro.plataforma.seguridad.beans.Ejecutable ejecutable) {
		this.ejecutable = ejecutable;
	}

	public java.lang.String getPrompt() {
		return prompt;
	}

	public void setPrompt(java.lang.String prompt) {
		this.prompt = prompt;
	}

	public long getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(long creadoPor) {
		this.creadoPor = creadoPor;
	}

	public long getModificadoPor() {
		return modificadoPor;
	}

	public void setModificadoPor(long modificadoPor) {
		this.modificadoPor = modificadoPor;
	}

	public java.util.Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(java.util.Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public java.util.Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(java.util.Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

}
