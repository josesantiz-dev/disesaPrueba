package net.giro.rh.admon.beans;

import java.util.Date;



public class EmpleadoDocumento implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Long creadoPor;		
	private Date fechaCreacion;		
	private Long modificadoPor;	
	private Date fechaModificacion;	
	
	private int idDocumentoDigitalizado;	// private DocumentoDigitalizado documentoDigitalizado;
	
	private Empleado empleado;	
	private String observacion;

	public EmpleadoDocumento() {
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

	public int getIdDocumentoDigitalizado() {
		return idDocumentoDigitalizado;
	}

	public void setIdDocumentoDigitalizado(int idDocumentoDigitalizado) {
		this.idDocumentoDigitalizado = idDocumentoDigitalizado;
	}

	public Empleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	
}