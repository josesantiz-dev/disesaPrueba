package net.giro.clientes.beans;

import java.util.Date;

/**
 * ff7041bfd7
 * @author javaz
 *
 */
public class ContactoPersona implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/*IDENTIFICADOR DE LA TABLA*/
	private long id;

	/*ESTATUS DE BAJA DEL REGISTRO*/
	private java.lang.Long estatusId;

	/*FECHA HORA EN QUE SE CREO EL REGISTRO*/
	private java.util.Date fechaCreacion;

	/*ID DEL USUARIO QUE GENERA EL REGISTRO*/
	private java.lang.Long creadoPor;

	/*FECHA DE MODIFICACION DEL REGISTRO*/
	private java.util.Date fechaModificacion;

	/*ID DEL USUARIO QUE MODIFICA EL REGISTRO*/
	private java.lang.Long modificadoPor;

	/*ID DE LA PERSONA A LA QUE PERTENECE EL CONTACTO*/
	private Persona idPersona;

	/*ID DE LA PERSONA QUE FIGURA COMO CONTACTO*/
	private Persona idPersonaContacto;

	/*PUESTO DEL CONTACTO*/
	private java.lang.String puesto;

	
	public ContactoPersona(){
		
	}


	public ContactoPersona(long id, Long estatusId, Date fechaCreacion,
			Long creadoPor, Date fechaModificacion, Long modificadoPor,
			Persona idPersona, Persona idPersonaContacto, String puesto) {
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


	public long getId() {
		return id;
	}


	public void setId(long id) {
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


	public Persona getIdPersona() {
		return idPersona;
	}


	public void setIdPersona(Persona idPersona) {
		this.idPersona = idPersona;
	}


	public Persona getIdPersonaContacto() {
		return idPersonaContacto;
	}


	public void setIdPersonaContacto(Persona idPersonaContacto) {
		this.idPersonaContacto = idPersonaContacto;
	}


	public java.lang.String getPuesto() {
		return puesto;
	}


	public void setPuesto(java.lang.String puesto) {
		this.puesto = puesto;
	}

}
