package net.giro.rh.admon.beans;

import java.io.Serializable;
import java.util.Date;

/**
 * a0bf10ff1f
 * @author javaz
 *
 */
public class EmpleadoBeneficiario implements Serializable {
	private static final long serialVersionUID = 1L;
	
	// Fields
	private Long id;
	private Long creadoPor;
	private Date fechaCreacion;
	private Long modificadoPor;
	private Date fechaModificacion;
	private Empleado idEmpleado;	//private Empleado empleado;
	private long idPersona;//private Persona persona;
	private Long porcentaje;
	private long idRelacion;	//private ConValores relacion;
	

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
		return idEmpleado;
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

	public Long getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(Long porcentaje) {
		this.porcentaje = porcentaje;
	}

	public long getIdRelacion() {
		return idRelacion;
	}

	public void setIdRelacion(long idRelacion) {
		this.idRelacion = idRelacion;
	}

	public EmpleadoBeneficiario() {
	}

	public EmpleadoBeneficiario(Long id, String descripcion) {
		this.id = id;
	}
	
}