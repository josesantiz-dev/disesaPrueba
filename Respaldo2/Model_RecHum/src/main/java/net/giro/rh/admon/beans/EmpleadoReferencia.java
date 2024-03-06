package net.giro.rh.admon.beans;

import java.io.Serializable;
import java.util.Date;

/**
 * d76eb5cbd9
 * @author javaz
 *
 */
public class EmpleadoReferencia implements Serializable {
	private static final long serialVersionUID = 1L;
	
	// Fields
	private Long id;
	private Long creadoPor;
	private Date fechaCreacion;
	private Long modificadoPor;
	private Date fechaModificacion;
	private Empleado idEmpleado;	//private Empleado empleado;
	private long idPersona;	//private Persona persona;
	private String tiempoDeConocerlo;
	// Constructors

	
	public EmpleadoReferencia() {
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public void setId(long id) {	//meotodo necesatio para tomar el correlativo
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

	public Empleado getIdEmpleado() {
		return this.idEmpleado;
	}

	public void setIdEmpleado(Empleado idEmpleado) {
		this.idEmpleado = idEmpleado;
	}

	public long getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(long idPersona) {
		this.idPersona = idPersona;
	}

	public String getTiempoDeConocerlo() {
		return tiempoDeConocerlo;
	}

	public void setTiempoDeConocerlo(String tiempoDeConocerlo) {
		this.tiempoDeConocerlo = tiempoDeConocerlo;
	}

}