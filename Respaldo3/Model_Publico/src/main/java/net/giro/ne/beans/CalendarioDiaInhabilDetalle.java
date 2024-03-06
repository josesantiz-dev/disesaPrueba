package net.giro.ne.beans;

import java.io.Serializable;
import java.util.Date;

/**
 * A21A2849B9
 * @author javaz
 *
 */
public class CalendarioDiaInhabilDetalle implements Serializable {
	private static final long serialVersionUID = 1L;

	private long id;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	private String descripcion;
	private Date fecha;
	private CalendarioDiaInhabil calendarioDiaInhabil;


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

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public net.giro.ne.beans.CalendarioDiaInhabil getCalendarioDiaInhabil() {
		return calendarioDiaInhabil;
	}

	public void setCalendarioDiaInhabil(net.giro.ne.beans.CalendarioDiaInhabil calendarioDiaInhabil) {
		this.calendarioDiaInhabil = calendarioDiaInhabil;
	}

}
