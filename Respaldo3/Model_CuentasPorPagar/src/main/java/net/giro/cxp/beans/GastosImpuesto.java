package net.giro.cxp.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * gastos_impuesto
 * @author javaz
 *
 */
public class GastosImpuesto implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long gastoId;
	private Long impuestoId;
	private Long creadoPor;
	private Date fechaCreacion;
	private Long modificadoPor;
	private Date fechaModificacion;

	public GastosImpuesto() {
		this.id = 0L;
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
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
