package net.giro.clientes.beans;

public class PersonaEstudio implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	long id;
	long creadoPor;
	java.util.Date fechaCreacion;
	long modificadoPor;
	java.util.Date fechaModificacion;
	net.giro.clientes.beans.Persona persona;
	java.lang.Long estudio;
	long aniosEstudio;
	long terminoEstudio;
	java.lang.Long carrera;
	java.lang.String escuela;
	java.util.Date fechaIngreso;
	java.util.Date fechaEgreso;


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

	public java.lang.Long getEstudio() {
		return estudio;
	}

	public void setEstudio(java.lang.Long estudio) {
		this.estudio = estudio;
	}

	public long getAniosEstudio() {
		return aniosEstudio;
	}

	public void setAniosEstudio(long aniosEstudio) {
		this.aniosEstudio = aniosEstudio;
	}

	public long getTerminoEstudio() {
		return terminoEstudio;
	}

	public void setTerminoEstudio(long terminoEstudio) {
		this.terminoEstudio = terminoEstudio;
	}

	public java.lang.Long getCarrera() {
		return carrera;
	}

	public void setCarrera(java.lang.Long carrera) {
		this.carrera = carrera;
	}

	public java.lang.String getEscuela() {
		return escuela;
	}

	public void setEscuela(java.lang.String escuela) {
		this.escuela = escuela;
	}

	public java.util.Date getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(java.util.Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public java.util.Date getFechaEgreso() {
		return fechaEgreso;
	}

	public void setFechaEgreso(java.util.Date fechaEgreso) {
		this.fechaEgreso = fechaEgreso;
	}

}
