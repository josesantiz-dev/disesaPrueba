package net.giro.rh.admon.beans;

import java.io.Serializable;
import java.util.Date;

import net.giro.clientes.beans.Persona;
import net.giro.plataforma.beans.ConValores;

public class EmpleadoBeneficiarioExt implements Serializable  {
	private static final long serialVersionUID = 1L;
	
	// Fields
	private Long id;
	private Long creadoPor;
	private Date fechaCreacion;
	private Long modificadoPor;
	private Date fechaModificacion;
	private EmpleadoExt empleado;
	private Persona persona;	//net.giro.clientes.beans
	private Long porcentaje;
	private ConValores relacion;
	
	public EmpleadoBeneficiarioExt() {}

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

	public EmpleadoExt getEmpleado() {
		return empleado;
	}

	public void setEmpleado(EmpleadoExt empleado) {
		this.empleado = empleado;
	}

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	public Long getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(Long porcentaje) {
		this.porcentaje = porcentaje;
	}

	public ConValores getRelacion() {
		return relacion;
	}

	public void setRelacion(ConValores relacion) {
		this.relacion = relacion;
	}
}
