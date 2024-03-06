package net.giro.clientes.beans;

/**
 * d1f8448222
 * @author javaz
 *
 */
public class PersonaDocumento implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	long id;
	long creadoPor;
	java.util.Date fechaCreacion;
	long modificadoPor;
	java.util.Date fechaModificacion;
	String documentoDigitalizado;
	net.giro.clientes.beans.Persona persona;
	java.lang.String observacion;


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

	 

	public String getDocumentoDigitalizado() {
		return documentoDigitalizado;
	}

	public void setDocumentoDigitalizado(String documentoDigitalizado) {
		this.documentoDigitalizado = documentoDigitalizado;
	}

	public net.giro.clientes.beans.Persona getPersona() {
		return persona;
	}

	public void setPersona(net.giro.clientes.beans.Persona persona) {
		this.persona = persona;
	}

	public java.lang.String getObservacion() {
		return observacion;
	}

	public void setObservacion(java.lang.String observacion) {
		this.observacion = observacion;
	}

}
