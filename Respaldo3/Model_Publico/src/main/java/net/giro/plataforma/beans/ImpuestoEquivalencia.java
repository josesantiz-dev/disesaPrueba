package net.giro.plataforma.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * impuesto_equivalencia
 * @author javaz
 *
 */
public class ImpuestoEquivalencia implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private long codigoTransaccion;
	private ConValores idImpuestoOrigen;
	private ConValores idImpuestoDestino;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	public ImpuestoEquivalencia() {
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Long getCodigoTransaccion() {
		return codigoTransaccion;
	}

	public void setCodigoTransaccion(Long codigoTransaccion) {
		codigoTransaccion = (codigoTransaccion != null) ? codigoTransaccion : 0L;
		this.codigoTransaccion = codigoTransaccion;
	}

	public ConValores getIdImpuestoOrigen() {
		return idImpuestoOrigen;
	}

	public void setIdImpuestoOrigen(ConValores idImpuestoOrigen) {
		this.idImpuestoOrigen = idImpuestoOrigen;
	}

	public ConValores getIdImpuestoDestino() {
		return idImpuestoDestino;
	}

	public void setIdImpuestoDestino(ConValores idImpuestoDestino) {
		this.idImpuestoDestino = idImpuestoDestino;
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
}
