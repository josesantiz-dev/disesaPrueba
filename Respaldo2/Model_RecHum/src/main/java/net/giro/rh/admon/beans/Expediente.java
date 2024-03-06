package net.giro.rh.admon.beans;

import java.util.Date;

public class Expediente implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	// Fields

	private Long id;
	private Long creadoPor;		
	private Date fechaCreacion;		
	private Long modificadoPor;	
	private Date fechaModificacion;
	private int idEmpleado;	//private Empleado empleado;              //mapeado
	private Long documento;
	private String observaciones;
	private String extension;
	private Long version;
	
	
	public Expediente() {
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


	public int getIdEmpleado() {
		return idEmpleado;
	}


	public void setIdEmpleado(int idEmpleado) {
		this.idEmpleado = idEmpleado;
	}


	public Long getDocumento() {
		return documento;
	}


	public void setDocumento(Long documento) {
		this.documento = documento;
	}


	public String getObservaciones() {
		return observaciones;
	}


	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}


	public String getExtension() {
		return extension;
	}


	public void setExtension(String extension) {
		this.extension = extension;
	}


	public Long getVersion() {
		return version;
	}


	public void setVersion(Long version) {
		this.version = version;
	}
	
}