package net.giro.clientes.beans;

import java.util.Date;


public class Seguimiento implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private long id;
	private Long  estatusId;
	private Prospecto prospectoId;
	private java.util.Date fechaContacto;
	private java.lang.Long modoContactoId;
	private java.lang.Long oficialId;
	private java.lang.String observaciones;
	private java.lang.Long razonRechazoId;
	private java.lang.Long creadoPor;
	private java.util.Date fechaCreacion;
	private java.lang.Long modificadoPor;
	private java.util.Date fechaModificacion;
	
	
	public Seguimiento() {
	}
	
	public Seguimiento(long id, Long estatusId, Prospecto prospectoId,
			Date fechaContacto, Long modoContactoId, Long especialistaId,
			String observaciones, Long razonRechazoId, Long creadoPor,
			Date fechaCreacion, Long modificadoPor, Date fechaModificacion) {
		this.id = id;
		this.estatusId = estatusId;
		this.prospectoId = prospectoId;
		this.fechaContacto = fechaContacto;
		this.modoContactoId = modoContactoId;
		this.oficialId = especialistaId;
		this.observaciones = observaciones;
		this.razonRechazoId = razonRechazoId;
		this.creadoPor = creadoPor;
		this.fechaCreacion = fechaCreacion;
		this.modificadoPor = modificadoPor;
		this.fechaModificacion = fechaModificacion;
	}
	
	
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public java.util.Date getFechaContacto() {
		return fechaContacto;
	}
	public void setFechaContacto(java.util.Date fechaContacto) {
		this.fechaContacto = fechaContacto;
	}
	public java.lang.Long getModoContactoId() {
		return modoContactoId;
	}
	public void setModoContactoId(java.lang.Long modoContactoId) {
		this.modoContactoId = modoContactoId;
	}
	public java.lang.Long getEspecialistaId() {
		return oficialId;
	}
	public void setEspecialistaId(java.lang.Long especialistaId) {
		this.oficialId = especialistaId;
	}
	public java.lang.String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(java.lang.String observaciones) {
		this.observaciones = observaciones;
	}
	public java.lang.Long getRazonRechazoId() {
		return razonRechazoId;
	}
	public void setRazonRechazoId(java.lang.Long razonRechazoId) {
		this.razonRechazoId = razonRechazoId;
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

	public Prospecto getProspectoId() {
		return prospectoId;
	}

	public void setProspectoId(Prospecto prospectoId) {
		this.prospectoId = prospectoId;
	}

	public Long getEstatusId() {
		return estatusId;
	}

	public void setEstatusId(Long estatusId) {
		this.estatusId = estatusId;
	}






	
	

}
