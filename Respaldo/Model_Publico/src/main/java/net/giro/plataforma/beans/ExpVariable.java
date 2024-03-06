package net.giro.plataforma.beans;

public class ExpVariable implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	long id;
	java.lang.String nombre;
	java.lang.String valor;
	java.lang.String propiedad;
	java.util.Date fechaCreacion;
	java.util.Date fechaModificacion;
	long creadoPor;
	long modificadoPor;


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public java.lang.String getNombre() {
		return nombre;
	}

	public void setNombre(java.lang.String nombre) {
		this.nombre = nombre;
	}

	public java.lang.String getValor() {
		return valor;
	}

	public void setValor(java.lang.String valor) {
		this.valor = valor;
	}

	public java.lang.String getPropiedad() {
		return propiedad;
	}

	public void setPropiedad(java.lang.String propiedad) {
		this.propiedad = propiedad;
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

}
