package net.giro.plataforma.beans;

public class ExpRegla implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	long id;
	java.lang.String nombre;
	java.lang.String expresion;
	java.lang.String expresionVerdadero;
	java.lang.String expresionFalso;
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

	public java.lang.String getExpresion() {
		return expresion;
	}

	public void setExpresion(java.lang.String expresion) {
		this.expresion = expresion;
	}

	public java.lang.String getExpresionVerdadero() {
		return expresionVerdadero;
	}

	public void setExpresionVerdadero(java.lang.String expresionVerdadero) {
		this.expresionVerdadero = expresionVerdadero;
	}

	public java.lang.String getExpresionFalso() {
		return expresionFalso;
	}

	public void setExpresionFalso(java.lang.String expresionFalso) {
		this.expresionFalso = expresionFalso;
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
