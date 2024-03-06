package net.giro.clientes.beans;

import java.io.Serializable;
import java.util.Date;

public class PersonaNombresAlternoExt implements Serializable {
	private static final long serialVersionUID = 1L;

	private long id;
	private long creadoPor;
	private java.util.Date fechaCreacion;
	private long modificadoPor;
	private java.util.Date fechaModificacion;
	private PersonaExt persona;
	private java.lang.String nombre;
	private java.lang.String primerApellido;
	private java.lang.String segundoApellido;
	
	public PersonaNombresAlternoExt(){
		
	}

	public PersonaNombresAlternoExt(long id, long creadoPor,
			Date fechaCreacion, long modificadoPor, Date fechaModificacion,
			PersonaExt persona, String nombre, String primerApellido,
			String segundoApellido) {
		this.id = id;
		this.creadoPor = creadoPor;
		this.fechaCreacion = fechaCreacion;
		this.modificadoPor = modificadoPor;
		this.fechaModificacion = fechaModificacion;
		this.persona = persona;
		this.nombre = nombre;
		this.primerApellido = primerApellido;
		this.segundoApellido = segundoApellido;
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

	public PersonaExt getPersona() {
		return persona;
	}

	public void setPersona(PersonaExt persona) {
		this.persona = persona;
	}

	public java.lang.String getNombre() {
		return nombre;
	}

	public void setNombre(java.lang.String nombre) {
		this.nombre = nombre;
	}

	public java.lang.String getPrimerApellido() {
		return primerApellido;
	}

	public void setPrimerApellido(java.lang.String primerApellido) {
		this.primerApellido = primerApellido;
	}

	public java.lang.String getSegundoApellido() {
		return segundoApellido;
	}

	public void setSegundoApellido(java.lang.String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}

	
}
