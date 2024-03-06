package net.giro.plataforma.seguridad.beans;

import java.util.Date;

/**
 * f9e8e882aa8
 * @author javaz
 *
 */
public class Funcion implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	long id;
	private  java.lang.String nombre;
	private java.lang.String descripcion;
	private java.lang.String forma;
	private java.lang.Long creadoPor;
	private java.util.Date fechaCreacion;
	private java.lang.Long modificadoPor;
	private java.util.Date fechaModificacion;
	java.lang.Long icono;
	java.lang.String parametros;
	private Aplicacion aplicacion;
	long version;
	
	
	
	public Funcion(long id, String nombre, String descripcion, String forma,
			Long creadoPor, Date fechaCreacion, Long modificadoPor,
			Date fechaModificacion, Long icono, String parametros, long version) {
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.forma = forma;
		this.creadoPor = creadoPor;
		this.fechaCreacion = fechaCreacion;
		this.modificadoPor = modificadoPor;
		this.fechaModificacion = fechaModificacion;
		this.icono = icono;
		this.parametros = parametros;
		this.version = version;
	}
	
	public Funcion() {
		super();
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

	public java.lang.String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(java.lang.String descripcion) {
		this.descripcion = descripcion;
	}

	public java.lang.String getForma() {
		return forma;
	}

	public void setForma(java.lang.String forma) {
		this.forma = forma;
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

	public java.lang.Long getIcono() {
		return icono;
	}

	public void setIcono(java.lang.Long icono) {
		this.icono = icono;
	}

	public java.lang.String getParametros() {
		return parametros;
	}

	public void setParametros(java.lang.String parametros) {
		this.parametros = parametros;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public Aplicacion getAplicacion() {
		return aplicacion;
	}

	public void setAplicacion(Aplicacion aplicacion) {
		this.aplicacion = aplicacion;
	}

	





	

	 
}
