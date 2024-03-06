package net.giro.ne.beans;

public class Moneda implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	long id;
	java.lang.String nombre;
	long creadoPor;
	java.util.Date fechaCreacion;
	long modificadoPor;
	java.util.Date fechaModificacion;
	java.lang.String abreviacion;
	java.lang.String atributo1;
	java.lang.String atributo2;
	java.lang.String atributo3;
	java.lang.String atributo4;
	java.lang.String atributo5;


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

	public java.lang.String getAbreviacion() {
		return abreviacion;
	}

	public void setAbreviacion(java.lang.String abreviacion) {
		this.abreviacion = abreviacion;
	}

	public java.lang.String getAtributo1() {
		return atributo1;
	}

	public void setAtributo1(java.lang.String atributo1) {
		this.atributo1 = atributo1;
	}

	public java.lang.String getAtributo2() {
		return atributo2;
	}

	public void setAtributo2(java.lang.String atributo2) {
		this.atributo2 = atributo2;
	}

	public java.lang.String getAtributo3() {
		return atributo3;
	}

	public void setAtributo3(java.lang.String atributo3) {
		this.atributo3 = atributo3;
	}

	public java.lang.String getAtributo4() {
		return atributo4;
	}

	public void setAtributo4(java.lang.String atributo4) {
		this.atributo4 = atributo4;
	}

	public java.lang.String getAtributo5() {
		return atributo5;
	}

	public void setAtributo5(java.lang.String atributo5) {
		this.atributo5 = atributo5;
	}

}
