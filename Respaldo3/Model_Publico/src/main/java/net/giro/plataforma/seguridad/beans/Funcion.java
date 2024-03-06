package net.giro.plataforma.seguridad.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * f9e8e882aa8
 * @author javaz
 */
public class Funcion implements Serializable {
	private static final long serialVersionUID = 1L;
	private long id;
	private String nombre;
	private String descripcion;
	private String forma;
	private Long icono;
	private String parametros;
	private Aplicacion aplicacion;
	private long version;
	private Long creadoPor;
	private Date fechaCreacion;
	private Long modificadoPor;
	private Date fechaModificacion;

	public Funcion() {
		super();
		this.nombre = "";
		this.descripcion = "";
		this.forma = "";
		this.parametros = "";
		this.icono = 0L;
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getForma() {
		return forma;
	}

	public void setForma(String forma) {
		this.forma = forma;
	}

	public Long getIcono() {
		return icono;
	}

	public void setIcono(Long icono) {
		this.icono = icono;
	}

	public String getParametros() {
		return parametros;
	}

	public void setParametros(String parametros) {
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

	public Long getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(Long creadoPor) {
		this.creadoPor = creadoPor;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Long getModificadoPor() {
		return modificadoPor;
	}

	public void setModificadoPor(Long modificadoPor) {
		this.modificadoPor = modificadoPor;
	}

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
}
