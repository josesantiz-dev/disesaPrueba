package net.giro.ne.beans;

import java.io.Serializable;
import java.util.Date;

/**
 * F847F9A387
 * @author javaz
 *
 */
public class CalendarioDiaInhabil implements Serializable {
	private static final long serialVersionUID = 1L;

	private long id;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	private String calendarioDiaInhabil;


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

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public long getModificadoPor() {
		return modificadoPor;
	}

	public void setModificadoPor(long modificadoPor) {
		this.modificadoPor = modificadoPor;
	}

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public String getCalendarioDiaInhabil() {
		return calendarioDiaInhabil;
	}

	public void setCalendarioDiaInhabil(String calendarioDiaInhabil) {
		this.calendarioDiaInhabil = calendarioDiaInhabil;
	}

}
