package net.giro.bancos.beans;

public class SucursalBancaria implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	long id;
	long creadoPor;
	java.util.Date fechaCreacion;
	long modificadoPor;
	java.util.Date fechaModificacion;
	java.lang.String nombre;
	java.lang.String domicilio;
	Long estado;
	net.giro.bancos.beans.InstitucionBancaria institucionBancaria;


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

	public java.lang.String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(java.lang.String domicilio) {
		this.domicilio = domicilio;
	}

	public Long getEstado() {
		return estado;
	}

	public void setEstado(Long estado) {
		this.estado = estado;
	}

	public net.giro.bancos.beans.InstitucionBancaria getInstitucionBancaria() {
		return institucionBancaria;
	}

	public void setInstitucionBancaria(net.giro.bancos.beans.InstitucionBancaria institucionBancaria) {
		this.institucionBancaria = institucionBancaria;
	}

}
