package net.giro.clientes.beans;

public class Grupo implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	long id;
	long creadoPor;
	java.util.Date fechaCreacion;
	long modificadoPor;
	java.util.Date fechaModificacion;
	java.lang.String nombre;
	net.giro.clientes.beans.Persona representante;
	java.lang.String domicilio;
	java.lang.Long colonia;
	net.giro.clientes.beans.Persona secretario;
	net.giro.clientes.beans.Persona tesorero;


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

	public java.lang.String getNombre() {
		return nombre;
	}

	public void setNombre(java.lang.String nombre) {
		this.nombre = nombre;
	}

	public net.giro.clientes.beans.Persona getRepresentante() {
		return representante;
	}

	public void setRepresentante(net.giro.clientes.beans.Persona representante) {
		this.representante = representante;
	}

	public java.lang.String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(java.lang.String domicilio) {
		this.domicilio = domicilio;
	}

	public java.lang.Long getColonia() {
		return colonia;
	}

	public void setColonia(java.lang.Long colonia) {
		this.colonia = colonia;
	}

	public net.giro.clientes.beans.Persona getSecretario() {
		return secretario;
	}

	public void setSecretario(net.giro.clientes.beans.Persona secretario) {
		this.secretario = secretario;
	}

	public net.giro.clientes.beans.Persona getTesorero() {
		return tesorero;
	}

	public void setTesorero(net.giro.clientes.beans.Persona tesorero) {
		this.tesorero = tesorero;
	}

}
