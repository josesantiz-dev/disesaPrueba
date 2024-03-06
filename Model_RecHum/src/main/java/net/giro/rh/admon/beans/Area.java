package net.giro.rh.admon.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

//import cde.publico.datos.Empresa;

/**
 * aec729bd13
 * @author javaz
 *
 */
public class Area implements Serializable  {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String descripcion;
	private long idResponsable; //private Empleado responsable;
	private long idEmpresa;
	private int estatus;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	public Area() {
		this.id = 0L;
		this.descripcion = "";
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	public Area(Long id) {
		this();
		this.id = id;
	}
	
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public void setId(long id) {	//meotodo necesario para tomar el correlativo
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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

	public long getIdResponsable() {
		return idResponsable;
	}

	public void setIdResponsable(long idResponsable) {
		this.idResponsable = idResponsable;
	}
	
	public int getEstatus() {
		return estatus;
	}

	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}

	public long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}
}