package net.giro.plataforma.beans;

public class AreaExt implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	long id;
	java.lang.String descripcion;
	long creadoPor;
	java.util.Date fechaCreacion;
	long modificadoPor;
	java.util.Date fechaModificacion;
	EmpleadoExt responsable;
	Long empresa;


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public EmpleadoExt getResponsable() {
		return responsable;
	}

	public void setResponsable(EmpleadoExt responsable) {
		this.responsable = responsable;
	}

	public Long getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Long empresa) {
		this.empresa = empresa;
	}

}
