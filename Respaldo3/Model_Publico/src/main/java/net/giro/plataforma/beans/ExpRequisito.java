package net.giro.plataforma.beans;

/**
 * exp_requisito
 * @author javaz
 *
 */
public class ExpRequisito implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	long id;
	java.lang.String mensajeError;
	java.lang.String aplica;
	net.giro.plataforma.beans.ExpRegla regla;
	java.util.Date fechaCreacion;
	java.util.Date fechaModificacion;
	long creadoPor;
	long modificadoPor;


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public java.lang.String getMensajeError() {
		return mensajeError;
	}

	public void setMensajeError(java.lang.String mensajeError) {
		this.mensajeError = mensajeError;
	}

	public java.lang.String getAplica() {
		return aplica;
	}

	public void setAplica(java.lang.String aplica) {
		this.aplica = aplica;
	}

	public net.giro.plataforma.beans.ExpRegla getRegla() {
		return regla;
	}

	public void setRegla(net.giro.plataforma.beans.ExpRegla regla) {
		this.regla = regla;
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

}
