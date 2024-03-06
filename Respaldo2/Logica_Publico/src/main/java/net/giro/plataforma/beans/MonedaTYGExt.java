package net.giro.plataforma.beans;

import java.util.Date;

public class MonedaTYGExt implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	long id;
	java.lang.String nombre;
	java.lang.Long creadoPor;
	java.util.Date fechaCreacion;
	java.lang.Long modificadoPor;
	java.util.Date fechaModificacion;
	java.lang.String abreviacion;
	java.lang.Long idShf;


	public MonedaTYGExt(long id, String nombre, Long creadoPor, Date fechaCreacion,
			Long modificadoPor, Date fechaModificacion, String abreviacion,
			Long idShf) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.creadoPor = creadoPor;
		this.fechaCreacion = fechaCreacion;
		this.modificadoPor = modificadoPor;
		this.fechaModificacion = fechaModificacion;
		this.abreviacion = abreviacion;
		this.idShf = idShf;
	}
	
	public MonedaTYGExt() {
		
	}


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

	public java.lang.Long getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(java.lang.Long creadoPor) {
		this.creadoPor = creadoPor;
	}

	public java.util.Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(java.util.Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public java.lang.Long getModificadoPor() {
		return modificadoPor;
	}

	public void setModificadoPor(java.lang.Long modificadoPor) {
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

	public java.lang.Long getIdShf() {
		return idShf;
	}

	public void setIdShf(java.lang.Long idShf) {
		this.idShf = idShf;
	}

}
