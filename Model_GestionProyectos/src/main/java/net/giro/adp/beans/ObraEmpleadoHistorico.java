package net.giro.adp.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class ObraEmpleadoHistorico implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private long idObra;
	private long idEmpleado;
	private Date fechaAsignacion;
	private Date fechaTerminacion;
	private long eliminadoPor;
	
	public ObraEmpleadoHistorico() {
		this.id = 0L;
		this.fechaAsignacion = Calendar.getInstance().getTime();
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

	public long getIdObra() {
		return idObra;
	}

	public void setIdObra(long idObra) {
		this.idObra = idObra;
	}

	public long getIdEmpleado() {
		return idEmpleado;
	}

	public void setIdEmpleado(long idEmpleado) {
		this.idEmpleado = idEmpleado;
	}

	public Date getFechaAsignacion() {
		return fechaAsignacion;
	}

	public void setFechaAsignacion(Date fechaAsignacion) {
		this.fechaAsignacion = fechaAsignacion;
	}

	public Date getFechaTerminacion() {
		return fechaTerminacion;
	}

	public void setFechaTerminacion(Date fechaTerminacion) {
		this.fechaTerminacion = fechaTerminacion;
	}

	public long getEliminadoPor() {
		return eliminadoPor;
	}

	public void setEliminadoPor(long eliminadoPor) {
		this.eliminadoPor = eliminadoPor;
	}
}
