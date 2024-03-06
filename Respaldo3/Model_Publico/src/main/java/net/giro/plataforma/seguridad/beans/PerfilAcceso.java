package net.giro.plataforma.seguridad.beans;

import java.io.Serializable;
import java.util.Date;

/**
 * d2286f84c8
 * @author javaz
 *
 */
public class PerfilAcceso implements Serializable {
	private static final long serialVersionUID = 1L;

	private long id;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	private String perfilAcceso;


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

	public java.lang.String getPerfilAcceso() {
		return perfilAcceso;
	}

	public void setPerfilAcceso(java.lang.String perfilAcceso) {
		this.perfilAcceso = perfilAcceso;
	}

}
