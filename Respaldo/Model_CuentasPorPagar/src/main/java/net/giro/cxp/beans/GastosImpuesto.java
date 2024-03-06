package net.giro.cxp.beans;

import java.util.Date;

public class GastosImpuesto implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	Long id;
	Long gastoId;
	Long impuestoId;
	Long creadoPor;
	Date fechaCreacion;
	Long modificadoPor;
	Date fechaModificacion;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Long getGastoId() {
		return gastoId;
	}

	public void setGastoId(Long gastoId) {
		this.gastoId = gastoId;
	}

	public Long getImpuestoId() {
		return impuestoId;
	}

	public void setImpuestoId(Long impuestoId) {
		this.impuestoId = impuestoId;
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

}
