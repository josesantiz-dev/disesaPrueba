package net.giro.rh.admon.beans;

import java.io.Serializable;
import java.util.Date;

public class Puesto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	// Fields
	private Long id;
	private Long creadoPor;
	private Date fechaCreacion;
	private Long modificadoPor;
	private Date fechaModificacion;
	private String descripcion;
	private String correoPermisos;
	private String correoPrestamos;
	private String correoVacaciones;
	private String correoViaticos;
	private String correoIncidencias;
	private String correoActas;
	private int estatus;
	// Constructors

	public Puesto() {
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}
	
	public void setId(long id) {
		this.id = id;
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


	public String getDescripcion() {
		return descripcion;
	}


	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	public String getCorreoPermisos() {
		return correoPermisos;
	}


	public void setCorreoPermisos(String correoPermisos) {
		this.correoPermisos = correoPermisos;
	}


	public String getCorreoPrestamos() {
		return correoPrestamos;
	}


	public void setCorreoPrestamos(String correoPrestamos) {
		this.correoPrestamos = correoPrestamos;
	}


	public String getCorreoVacaciones() {
		return correoVacaciones;
	}


	public void setCorreoVacaciones(String correoVacaciones) {
		this.correoVacaciones = correoVacaciones;
	}


	public String getCorreoViaticos() {
		return correoViaticos;
	}


	public void setCorreoViaticos(String correoViaticos) {
		this.correoViaticos = correoViaticos;
	}


	public String getCorreoIncidencias() {
		return correoIncidencias;
	}


	public void setCorreoIncidencias(String correoIncidencias) {
		this.correoIncidencias = correoIncidencias;
	}


	public String getCorreoActas() {
		return correoActas;
	}


	public void setCorreoActas(String correoActas) {
		this.correoActas = correoActas;
	}


	public int getEstatus() {
		return estatus;
	}


	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}

}