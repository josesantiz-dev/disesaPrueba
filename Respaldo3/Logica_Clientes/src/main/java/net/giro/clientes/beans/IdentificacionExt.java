package net.giro.clientes.beans;

import java.io.Serializable;
import java.util.Date;

public class IdentificacionExt implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private long id;
	private long creadoPor;
	private java.util.Date fechaCreacion;
	private long modificadoPor;
	private java.util.Date fechaModificacion;
	private net.giro.clientes.beans.Persona persona;
	private java.lang.Long identificacion;
	private String identificacionDesc;
	private java.lang.String numeroIdentificacion;
	
	public IdentificacionExt(){
		
	}
	
	public IdentificacionExt(long id, long creadoPor, Date fechaCreacion,
			long modificadoPor, Date fechaModificacion, Persona persona,
			Long identificacion, String identificacionDesc,
			String numeroIdentificacion) {
		this.id = id;
		this.creadoPor = creadoPor;
		this.fechaCreacion = fechaCreacion;
		this.modificadoPor = modificadoPor;
		this.fechaModificacion = fechaModificacion;
		this.persona = persona;
		this.identificacion = identificacion;
		this.identificacionDesc = identificacionDesc;
		this.numeroIdentificacion = numeroIdentificacion;
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

	public net.giro.clientes.beans.Persona getPersona() {
		return persona;
	}

	public void setPersona(net.giro.clientes.beans.Persona persona) {
		this.persona = persona;
	}

	public java.lang.Long getIdentificacion() {
		return identificacion;
	}

	public void setIdentificacion(java.lang.Long identificacion) {
		this.identificacion = identificacion;
	}

	public String getIdentificacionDesc() {
		return identificacionDesc;
	}

	public void setIdentificacionDesc(String identificacionDesc) {
		this.identificacionDesc = identificacionDesc;
	}

	public java.lang.String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}

	public void setNumeroIdentificacion(java.lang.String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}

}
