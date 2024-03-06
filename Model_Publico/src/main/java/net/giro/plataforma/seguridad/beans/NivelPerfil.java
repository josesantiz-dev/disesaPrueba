package net.giro.plataforma.seguridad.beans;

/**
 * aeb38b5f91
 * @author javaz
 *
 */
public class NivelPerfil implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	long id;
	long valor;
	java.lang.String descripcion;
	long creadoPor;
	java.util.Date fechaCreacion;
	long modificadoPor;
	long prioridad;
	java.util.Date fechaModificacion;


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getValor() {
		return valor;
	}

	public void setValor(long valor) {
		this.valor = valor;
	}

	public java.lang.String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(java.lang.String descripcion) {
		this.descripcion = descripcion;
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

	public long getPrioridad() {
		return prioridad;
	}

	public void setPrioridad(long prioridad) {
		this.prioridad = prioridad;
	}

}
