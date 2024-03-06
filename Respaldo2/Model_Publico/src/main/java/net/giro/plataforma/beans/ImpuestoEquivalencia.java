package net.giro.plataforma.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class ImpuestoEquivalencia implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Long codigoTransaccion;
	private ConValores idImpuestoOrigen;
	private ConValores idImpuestoDestino;
	private Long creadoPor;
	private Date fechaCreacion;
	private Long modificadoPor;
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

	public Long getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(Long creadoPor) {
		this.creadoPor = creadoPor;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Long getModificadoPor() {
		return modificadoPor;
	}

	public void setModificadoPor(Long modificadoPor) {
		this.modificadoPor = modificadoPor;
	}

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
}
