package net.giro.credito.admon.beans;

import java.util.Date;

public class Oficial implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	long id;
	long creadoPor;
	long modificadoPor;
	java.util.Date fechaCreacion;
	java.util.Date fechaModificacion;
	Long usuarioId;
	Long empleado;

	public Oficial(){
		
	}

	public Oficial(long id, long creadoPor, long modificadoPor,
			Date fechaCreacion, Date fechaModificacion, Long usuarioId,
			Long empleado) {
		this.id = id;
		this.creadoPor = creadoPor;
		this.modificadoPor = modificadoPor;
		this.fechaCreacion = fechaCreacion;
		this.fechaModificacion = fechaModificacion;
		this.usuarioId = usuarioId;
		this.empleado = empleado;
	}
	

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

	public long getModificadoPor() {
		return modificadoPor;
	}

	public void setModificadoPor(long modificadoPor) {
		this.modificadoPor = modificadoPor;
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

	public Long getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(Long usuarioId) {
		this.usuarioId = usuarioId;
	}

	public Long getEmpleado() {
		return empleado;
	}

	public void setEmpleado(Long empleado) {
		this.empleado = empleado;
	}

}
