package net.giro.plataforma.seguridad.beans;

public class CampoTabla implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	long id;
	long creadoPor;
	java.util.Date fechaCreacion;
	long modificadoPor;
	java.util.Date fechaModificacion;
	net.giro.plataforma.beans.ConValores tabla;
	java.lang.String campo;
	long validar;


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

	public net.giro.plataforma.beans.ConValores getTabla() {
		return tabla;
	}

	public void setTabla(net.giro.plataforma.beans.ConValores tabla) {
		this.tabla = tabla;
	}

	public java.lang.String getCampo() {
		return campo;
	}

	public void setCampo(java.lang.String campo) {
		this.campo = campo;
	}

	public long getValidar() {
		return validar;
	}

	public void setValidar(long validar) {
		this.validar = validar;
	}

}
