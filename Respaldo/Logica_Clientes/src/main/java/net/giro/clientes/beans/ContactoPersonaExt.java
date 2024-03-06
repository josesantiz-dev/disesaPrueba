package net.giro.clientes.beans;

import java.util.Date;


public class ContactoPersonaExt implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	
	private long id;
	private java.lang.Long estatusId;
	private java.util.Date fechaCreacion;
	private java.lang.Long creadoPor;
	private java.util.Date fechaModificacion;
	private java.lang.Long modificadoPor;
	private PersonaExt idPersona;
	private PersonaExt idPersonaContacto;
	private java.lang.String puesto;
	
	public ContactoPersonaExt(){
		
	}

	public ContactoPersonaExt(Long id, Long estatusId, Date fechaCreacion,
			Long creadoPor, Date fechaModificacion, Long modificadoPor,
			PersonaExt idPersona, PersonaExt idPersonaContacto, String puesto) {
		this.id = id;
		this.estatusId = estatusId;
		this.fechaCreacion = fechaCreacion;
		this.creadoPor = creadoPor;
		this.fechaModificacion = fechaModificacion;
		this.modificadoPor = modificadoPor;
		this.idPersona = idPersona;
		this.idPersonaContacto = idPersonaContacto;
		this.puesto = puesto;
	}

	public java.lang.Long getId() {
		return id;
	}

	public void setId(java.lang.Long id) {
		this.id = id;
	}

	public java.lang.Long getEstatusId() {
		return estatusId;
	}

	public void setEstatusId(java.lang.Long estatusId) {
		this.estatusId = estatusId;
	}

	public java.util.Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(java.util.Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public java.lang.Long getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(java.lang.Long creadoPor) {
		this.creadoPor = creadoPor;
	}

	public java.util.Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(java.util.Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public java.lang.Long getModificadoPor() {
		return modificadoPor;
	}

	public void setModificadoPor(java.lang.Long modificadoPor) {
		this.modificadoPor = modificadoPor;
	}

	public PersonaExt getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(PersonaExt idPersona) {
		this.idPersona = idPersona;
	}

	public PersonaExt getIdPersonaContacto() {
		return idPersonaContacto;
	}

	public void setIdPersonaContacto(PersonaExt idPersonaContacto) {
		this.idPersonaContacto = idPersonaContacto;
	}

	public java.lang.String getPuesto() {
		return puesto;
	}

	public void setPuesto(java.lang.String puesto) {
		this.puesto = puesto;
	}

	
}
