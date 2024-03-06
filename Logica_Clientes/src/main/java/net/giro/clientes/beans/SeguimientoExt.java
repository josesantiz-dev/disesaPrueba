package net.giro.clientes.beans;

import java.io.Serializable;
import java.util.Date;

import net.giro.plataforma.beans.ConValores;

public class SeguimientoExt implements Serializable {
	private static final long serialVersionUID = 1L;
	
	long id;
	private Long  estatusId;
	ProspectoExt prospectoId;
	private java.util.Date fechaContacto;
	ConValores modoContactoId;
	OficialExt especialistaId;
	private java.lang.String observaciones;
	ConValores razonRechazoId;
	private java.lang.Long creadoPor;
	private java.util.Date fechaCreacion;
	private java.lang.Long modificadoPor;
	private java.util.Date fechaModificacion;
	EstatusSeguimientoExt estatus;
	


	public SeguimientoExt()	{
	}
	
	
	public SeguimientoExt(Long id, Long estatusId, ProspectoExt prospectoId,
			Date fechaContacto, ConValores modoContactoId, OficialExt especialistaId,
			String observaciones, ConValores razonRechazoId, Long creadoPor,
			Date fechaCreacion, Long modificadoPor, Date fechaModificacion,EstatusSeguimientoExt estatus) {
		this.id = id;
		this.estatusId = estatusId;
		this.prospectoId = prospectoId;
		this.fechaContacto = fechaContacto;
		this.modoContactoId = modoContactoId;
		this.especialistaId = especialistaId;
		this.observaciones = observaciones;
		this.razonRechazoId = razonRechazoId;
		this.creadoPor = creadoPor;
		this.fechaCreacion = fechaCreacion;
		this.modificadoPor = modificadoPor;
		this.fechaModificacion = fechaModificacion;
		this.estatus=estatus;
	}

	
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Long getEstatusId() {
		return estatusId;
	}
	public void setEstatusId(Long estatusId) {
		this.estatusId = estatusId;
	}

	public Date getFechaContacto() {
		return fechaContacto;
	}
	public void setFechaContacto(Date fechaContacto) {
		this.fechaContacto = fechaContacto;
	}
	

	public java.lang.String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(java.lang.String observaciones) {
		this.observaciones = observaciones;
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


	public EstatusSeguimientoExt getEstatus() {
		return estatus;
	}


	public void setEstatus(EstatusSeguimientoExt estatus) {
		this.estatus = estatus;
	}


	public ConValores getModoContactoId() {
		return modoContactoId;
	}


	public void setModoContactoId(ConValores modoContactoId) {
		this.modoContactoId = modoContactoId;
	}


	public ConValores getRazonRechazoId() {
		return razonRechazoId;
	}


	public void setRazonRechazoId(ConValores razonRechazoId) {
		this.razonRechazoId = razonRechazoId;
	}


	public OficialExt getEspecialistaId() {
		return especialistaId;
	}


	public void setEspecialistaId(OficialExt especialistaId) {
		this.especialistaId = especialistaId;
	}


	public ProspectoExt getProspectoId() {
		return prospectoId;
	}

	public void setProspectoId(ProspectoExt prospectoId) {
		this.prospectoId = prospectoId;
	}
}
