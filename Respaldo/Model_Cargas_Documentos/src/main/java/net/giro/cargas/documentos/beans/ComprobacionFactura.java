package net.giro.cargas.documentos.beans;

import java.io.Serializable;

public class ComprobacionFactura implements Serializable { 

	private static final long serialVersionUID = 1L;

	private long id;
	private java.lang.Long creadoPor;
	private java.util.Date fechaCreacion;
	private java.lang.Long modificadoPor;
	private java.util.Date fechaModificacion;
	private java.lang.String expresionImpresa;
	private java.util.Date fechaComprobacion;
	private java.lang.String codigoEstatus;
	private java.lang.String estado;

	public ComprobacionFactura() {}

 	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public java.lang.Long getCreadoPor() {
		return creadoPor;
	}
	public void setCreadoPor(java.lang.Long creadoPor) {
		this.creadoPor = creadoPor;
	}
	public java.util.Date getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(java.util.Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public java.lang.Long getModificadoPor() {
		return modificadoPor;
	}
	public void setModificadoPor(java.lang.Long modificadoPor) {
		this.modificadoPor = modificadoPor;
	}
	public java.util.Date getFechaModificacion() {
		return fechaModificacion;
	}
	public void setFechaModificacion(java.util.Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	public java.lang.String getExpresionImpresa() {
		return expresionImpresa;
	}
	public void setExpresionImpresa(java.lang.String expresionImpresa) {
		this.expresionImpresa = expresionImpresa;
	}
	public java.util.Date getFechaComprobacion() {
		return fechaComprobacion;
	}
	public void setFechaComprobacion(java.util.Date fechaComprobacion) {
		this.fechaComprobacion = fechaComprobacion;
	}
	public java.lang.String getCodigoEstatus() {
		return codigoEstatus;
	}
	public void setCodigoEstatus(java.lang.String codigoEstatus) {
		this.codigoEstatus = codigoEstatus;
	}
	public java.lang.String getEstado() {
		return estado;
	}
	public void setEstado(java.lang.String estado) {
		this.estado = estado;
	}
}