package net.giro.cxp.beans;

import java.io.Serializable;
import java.util.Date;

import net.giro.plataforma.beans.ConValores;

public class GastosImpuestoExt implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Long idGasto;
	private ConValores idImpuesto;
	private Long creadoPor;
	private Date fechaCreacion;
	private Long modificadoPor;
	private Date fechaModificacion;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getGastoId() {
		return idGasto;
	}

	public void setGastoId(Long gastoId) {
		this.idGasto = gastoId;
	}
	
	public ConValores getImpuestoId() {
		return idImpuesto;
	}

	public void setImpuestoId(ConValores impuestoId) {
		this.idImpuesto = impuestoId;
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
