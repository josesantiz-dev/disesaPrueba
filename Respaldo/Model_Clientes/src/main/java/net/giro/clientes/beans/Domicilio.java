package net.giro.clientes.beans;

public class Domicilio implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	long id;
	long creadoPor;
	java.util.Date fechaCreacion;
	long modificadoPor;
	java.util.Date fechaModificacion;
	net.giro.clientes.beans.Persona persona;
	java.lang.Long colonia;
	java.lang.Long catDomicilio1;
	java.lang.Long catDomicilio2;
	java.lang.Long catDomicilio3;
	java.lang.Long catDomicilio4;
	java.lang.Long catDomicilio5;
	java.lang.String descripcionDomicilio1;
	java.lang.String descripcionDomicilio2;
	java.lang.String descripcionDomicilio3;
	java.lang.String descripcionDomicilio4;
	java.lang.String descripcionDomicilio5;
	java.lang.String observaciones;
	long estatus;
	long principal;
	net.giro.clientes.beans.Negocio negocio;


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

	public java.lang.Long getColonia() {
		return colonia;
	}

	public void setColonia(java.lang.Long colonia) {
		this.colonia = colonia;
	}

	public java.lang.Long getCatDomicilio1() {
		return catDomicilio1;
	}

	public void setCatDomicilio1(java.lang.Long catDomicilio1) {
		this.catDomicilio1 = catDomicilio1;
	}

	public java.lang.Long getCatDomicilio2() {
		return catDomicilio2;
	}

	public void setCatDomicilio2(java.lang.Long catDomicilio2) {
		this.catDomicilio2 = catDomicilio2;
	}

	public java.lang.Long getCatDomicilio3() {
		return catDomicilio3;
	}

	public void setCatDomicilio3(java.lang.Long catDomicilio3) {
		this.catDomicilio3 = catDomicilio3;
	}

	public java.lang.Long getCatDomicilio4() {
		return catDomicilio4;
	}

	public void setCatDomicilio4(java.lang.Long catDomicilio4) {
		this.catDomicilio4 = catDomicilio4;
	}

	public java.lang.Long getCatDomicilio5() {
		return catDomicilio5;
	}

	public void setCatDomicilio5(java.lang.Long catDomicilio5) {
		this.catDomicilio5 = catDomicilio5;
	}

	public java.lang.String getDescripcionDomicilio1() {
		return descripcionDomicilio1;
	}

	public void setDescripcionDomicilio1(java.lang.String descripcionDomicilio1) {
		this.descripcionDomicilio1 = descripcionDomicilio1;
	}

	public java.lang.String getDescripcionDomicilio2() {
		return descripcionDomicilio2;
	}

	public void setDescripcionDomicilio2(java.lang.String descripcionDomicilio2) {
		this.descripcionDomicilio2 = descripcionDomicilio2;
	}

	public java.lang.String getDescripcionDomicilio3() {
		return descripcionDomicilio3;
	}

	public void setDescripcionDomicilio3(java.lang.String descripcionDomicilio3) {
		this.descripcionDomicilio3 = descripcionDomicilio3;
	}

	public java.lang.String getDescripcionDomicilio4() {
		return descripcionDomicilio4;
	}

	public void setDescripcionDomicilio4(java.lang.String descripcionDomicilio4) {
		this.descripcionDomicilio4 = descripcionDomicilio4;
	}

	public java.lang.String getDescripcionDomicilio5() {
		return descripcionDomicilio5;
	}

	public void setDescripcionDomicilio5(java.lang.String descripcionDomicilio5) {
		this.descripcionDomicilio5 = descripcionDomicilio5;
	}

	public java.lang.String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(java.lang.String observaciones) {
		this.observaciones = observaciones;
	}

	public long getEstatus() {
		return estatus;
	}

	public void setEstatus(long estatus) {
		this.estatus = estatus;
	}

	public long getPrincipal() {
		return principal;
	}

	public void setPrincipal(long principal) {
		this.principal = principal;
	}

	public net.giro.clientes.beans.Negocio getNegocio() {
		return negocio;
	}

	public void setNegocio(net.giro.clientes.beans.Negocio negocio) {
		this.negocio = negocio;
	}

}
