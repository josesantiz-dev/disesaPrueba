package net.giro.clientes.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import net.giro.plataforma.ubicacion.beans.Colonia;

public class DomicilioExt implements Serializable {
	private static final long serialVersionUID = 1L;
	private long id;
	private PersonaExt persona;
	private Negocio negocio;
	private Colonia colonia;
	private String nombreColonia;
	private String Domicilio;
	private Long catDomicilio1;
	private Long catDomicilio2;
	private Long catDomicilio3;
	private Long catDomicilio4;
	private Long catDomicilio5;
	private String descripcionDomicilio1;
	private String descripcionDomicilio2;
	private String descripcionDomicilio3;
	private String descripcionDomicilio4;
	private String descripcionDomicilio5;
	private String observaciones;
	private Boolean estatus;
	private Boolean principal;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	public DomicilioExt() {
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
		this.nombreColonia = "";
		this.Domicilio = "";
		this.catDomicilio1 = 0L;
		this.catDomicilio2 = 0L;
		this.catDomicilio3 = 0L;
		this.catDomicilio4 = 0L;
		this.catDomicilio5 = 0L;
		this.descripcionDomicilio1 = "";
		this.descripcionDomicilio2 = "";
		this.descripcionDomicilio3 = "";
		this.descripcionDomicilio4 = "";
		this.descripcionDomicilio5 = "";
		this.observaciones = "";
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public PersonaExt getPersona() {
		return persona;
	}

	public void setPersona(PersonaExt persona) {
		this.persona = persona;
	}

	public Negocio getNegocio() {
		return negocio;
	}

	public void setNegocio(Negocio negocio) {
		this.negocio = negocio;
	}

	public Colonia getColonia() {
		return colonia;
	}

	public void setColonia(Colonia colonia) {
		this.colonia = colonia;
	}

	public String getNombreColonia() {
		return nombreColonia;
	}

	public void setNombreColonia(String nombreColonia) {
		this.nombreColonia = nombreColonia;
	}

	public String getDomicilio() {
		return Domicilio;
	}

	public void setDomicilio(String domicilio) {
		Domicilio = domicilio;
	}

	public Long getCatDomicilio1() {
		return catDomicilio1;
	}

	public void setCatDomicilio1(Long catDomicilio1) {
		this.catDomicilio1 = catDomicilio1;
	}

	public Long getCatDomicilio2() {
		return catDomicilio2;
	}

	public void setCatDomicilio2(Long catDomicilio2) {
		this.catDomicilio2 = catDomicilio2;
	}

	public Long getCatDomicilio3() {
		return catDomicilio3;
	}

	public void setCatDomicilio3(Long catDomicilio3) {
		this.catDomicilio3 = catDomicilio3;
	}

	public Long getCatDomicilio4() {
		return catDomicilio4;
	}

	public void setCatDomicilio4(Long catDomicilio4) {
		this.catDomicilio4 = catDomicilio4;
	}

	public Long getCatDomicilio5() {
		return catDomicilio5;
	}

	public void setCatDomicilio5(Long catDomicilio5) {
		this.catDomicilio5 = catDomicilio5;
	}

	public String getDescripcionDomicilio1() {
		return descripcionDomicilio1;
	}

	public void setDescripcionDomicilio1(String descripcionDomicilio1) {
		this.descripcionDomicilio1 = descripcionDomicilio1;
	}

	public String getDescripcionDomicilio2() {
		return descripcionDomicilio2;
	}

	public void setDescripcionDomicilio2(String descripcionDomicilio2) {
		this.descripcionDomicilio2 = descripcionDomicilio2;
	}

	public String getDescripcionDomicilio3() {
		return descripcionDomicilio3;
	}

	public void setDescripcionDomicilio3(String descripcionDomicilio3) {
		this.descripcionDomicilio3 = descripcionDomicilio3;
	}

	public String getDescripcionDomicilio4() {
		return descripcionDomicilio4;
	}

	public void setDescripcionDomicilio4(String descripcionDomicilio4) {
		this.descripcionDomicilio4 = descripcionDomicilio4;
	}

	public String getDescripcionDomicilio5() {
		return descripcionDomicilio5;
	}

	public void setDescripcionDomicilio5(String descripcionDomicilio5) {
		this.descripcionDomicilio5 = descripcionDomicilio5;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Boolean getEstatus() {
		return estatus;
	}

	public void setEstatus(Boolean estatus) {
		this.estatus = estatus;
	}

	public Boolean getPrincipal() {
		return principal;
	}

	public void setPrincipal(Boolean principal) {
		this.principal = principal;
	}

	public long getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(long creadoPor) {
		this.creadoPor = creadoPor;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public long getModificadoPor() {
		return modificadoPor;
	}

	public void setModificadoPor(long modificadoPor) {
		this.modificadoPor = modificadoPor;
	}

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
}
