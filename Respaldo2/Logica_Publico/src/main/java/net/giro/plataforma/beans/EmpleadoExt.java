package net.giro.plataforma.beans;

public class EmpleadoExt implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	java.lang.Long id;
	java.lang.Long creadoPor;
	java.util.Date fechaCreacion;
	java.lang.Long modificadoPor;
	java.util.Date fechaModificacion;
	Long persona;
	Long sucursal;
	Long empresa;
	AreaExt area;
	PuestoCategoriaExt puestoCategoria;
	java.lang.String nombreCasoEmergencia;
	java.lang.Long externo;


	public java.lang.Long getId() {
		return id;
	}

	public void setId(java.lang.Long id) {
		this.id = id;
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

	public Long getPersona() {
		return persona;
	}

	public void setPersona(Long persona) {
		this.persona = persona;
	}

	public Long getSucursal() {
		return sucursal;
	}

	public void setSucursal(Long sucursal) {
		this.sucursal = sucursal;
	}

	public Long getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Long empresa) {
		this.empresa = empresa;
	}

	public AreaExt getArea() {
		return area;
	}

	public void setArea(AreaExt area) {
		this.area = area;
	}

	public PuestoCategoriaExt getPuestoCategoria() {
		return puestoCategoria;
	}

	public void setPuestoCategoria(PuestoCategoriaExt puestoCategoria) {
		this.puestoCategoria = puestoCategoria;
	}

	public java.lang.String getNombreCasoEmergencia() {
		return nombreCasoEmergencia;
	}

	public void setNombreCasoEmergencia(java.lang.String nombreCasoEmergencia) {
		this.nombreCasoEmergencia = nombreCasoEmergencia;
	}

	public java.lang.Long getExterno() {
		return externo;
	}

	public void setExterno(java.lang.Long externo) {
		this.externo = externo;
	}

}
