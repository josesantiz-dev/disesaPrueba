package net.giro.clientes.beans;

import java.io.Serializable;

/**
 * dec382ce68
 * @author javaz
 *
 */
public class ContactoNegocio implements Serializable { 

	private static final long serialVersionUID = 1L;

	/*IDENTIFICADOR DE LA TABLA*/
	private long id;

	/*ESTATUS DE BAJA DEL REGISTRO*/
	private java.lang.Long estatusId;

	/*FECHA HORA EN QUE SE CREO EL REGISTRO*/
	private java.util.Date fechaCreacion;

	/*ID DEL CONTACTO QUE GENERA EL REGISTRO*/
	private java.lang.Long creadoPor;

	/*FECHA DE MODIFICACION DEL REGISTRO*/
	private java.util.Date fechaModificacion;

	/*ID DEL USUARIO QUE MODIFICA EL REGISTRO*/
	private java.lang.Long modificadoPor;

	/*ID DEL NEGOCIO AL QUE PERTENECE EL CONTACTO*/
	private Negocio idNegocio;

	/*ID DE LA PERSONA QUE FIGURA COMO CONTACTO DEL NEGOCIO*/
	private Persona idPersonaContacto;

	/*PUESTO DEL CONTACTO*/
	private String puesto;




	public ContactoNegocio() {}

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
	public Negocio getIdNegocio() {
		return idNegocio;
	}
	public void setIdNegocio(Negocio idNegocio) {
		this.idNegocio = idNegocio;
	}
	public Persona getIdPersonaContacto() {
		return idPersonaContacto;
	}
	public void setIdPersonaContacto(Persona idPersonaContacto) {
		this.idPersonaContacto = idPersonaContacto;
	}
	public String getPuesto() {
		return puesto;
	}
	public void setPuesto(String puesto) {
		this.puesto = puesto;
	}
}