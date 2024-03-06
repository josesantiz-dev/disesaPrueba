package net.giro.rh.admon.beans;

import java.util.Date;

//import cde.publico.valores.datos.ConValores;

public class EmpleadoEstudio implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	// Fields

	private Long id;
	private Long creadoPor;		
	private Date fechaCreacion;		
	private Long modificadoPor;	
	private Date fechaModificacion;	
	private Empleado empleado;            
	
	private int idEstudio;	//private ConValores estudio;
	
	private Long aniosEstudio;
	private Long terminoEstudio;
	
	private int idCarrera; //private ConValores carrera;
	
	private String escuela;
	private Date fechaIngreso;
	private Date fechaEgreso;
	
	public EmpleadoEstudio() {
	}

	public Long getId() {
		return id;
	}
	
	public void setId(long id) {	//meotodo necesatio para tomar el correlativo
		this.id = id;
	}
	
	public void setId(Long id) {
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

	public Empleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}

	public int getIdEstudio() {
		return idEstudio;
	}

	public void setIdEstudio(int idEstudio) {
		this.idEstudio = idEstudio;
	}

	public Long getAniosEstudio() {
		return aniosEstudio;
	}

	public void setAniosEstudio(Long aniosEstudio) {
		this.aniosEstudio = aniosEstudio;
	}

	public Long getTerminoEstudio() {
		return terminoEstudio;
	}

	public void setTerminoEstudio(Long terminoEstudio) {
		this.terminoEstudio = terminoEstudio;
	}

	public int getIdCarrera() {
		return idCarrera;
	}

	public void setIdCarrera(int idCarrera) {
		this.idCarrera = idCarrera;
	}

	public String getEscuela() {
		return escuela;
	}

	public void setEscuela(String escuela) {
		this.escuela = escuela;
	}

	public Date getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public Date getFechaEgreso() {
		return fechaEgreso;
	}

	public void setFechaEgreso(Date fechaEgreso) {
		this.fechaEgreso = fechaEgreso;
	}
	
	
}