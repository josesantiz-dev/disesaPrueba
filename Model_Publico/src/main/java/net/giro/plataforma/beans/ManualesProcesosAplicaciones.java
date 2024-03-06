package net.giro.plataforma.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import net.giro.plataforma.seguridad.beans.Aplicacion;

/**
 * manuales_procesos_aplicaciones
 * @author javaz
 *
 */
public class ManualesProcesosAplicaciones implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private ManualesProcesos idManualesProcesos;
	private Aplicacion idAplicacion;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;

	public ManualesProcesosAplicaciones() {
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}

	public ManualesProcesosAplicaciones(ManualesProcesos idManualesProcesos, Aplicacion idAplicacion) {
		this();
		this.idManualesProcesos = idManualesProcesos;
		this.idAplicacion = idAplicacion;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public ManualesProcesos getIdManualesProcesos() {
		return idManualesProcesos;
	}
	
	public void setIdManualesProcesos(ManualesProcesos idManualesProcesos) {
		this.idManualesProcesos = idManualesProcesos;
	}
	
	public Aplicacion getIdAplicacion() {
		return idAplicacion;
	}
	
	public void setIdAplicacion(Aplicacion idAplicacion) {
		this.idAplicacion = idAplicacion;
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
}
