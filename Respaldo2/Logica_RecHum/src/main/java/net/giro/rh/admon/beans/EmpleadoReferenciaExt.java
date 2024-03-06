package net.giro.rh.admon.beans;

import java.io.Serializable;
import java.util.Date;

import net.giro.clientes.beans.Persona;

public class EmpleadoReferenciaExt implements Serializable{

	private static final long serialVersionUID = 1L;
	
	
	// Fields
	private Long id;
	private Long creadoPor;
	private Date fechaCreacion;
	private Long modificadoPor;
	private Date fechaModificacion;
	private Empleado empleado;
	private Persona persona;
	private String tiempoDeConocerlo;
	
	public EmpleadoReferenciaExt() {}

	public Long getId() {
		return id;
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

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	public String getTiempoDeConocerlo() {
		return tiempoDeConocerlo;
	}

	public void setTiempoDeConocerlo(String tiempoDeConocerlo) {
		this.tiempoDeConocerlo = tiempoDeConocerlo;
	}
	
	
	
}
