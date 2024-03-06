package net.giro.cxc.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import net.giro.plataforma.beans.ConValores;

public class FacturaDetalleImpuestoExt implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private FacturaDetalle idFacturaDetalle;
	private ConValores idImpuesto;
	private BigDecimal monto;
	private Date fechaCreacion;
	private long creadoPor;
	private Date fechaModificacion;
	private long modificadoPor;

	public FacturaDetalleImpuestoExt() {
		this.monto = BigDecimal.ZERO;
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public FacturaDetalle getIdFacturaDetalle() {
		return idFacturaDetalle;
	}

	public void setIdFacturaDetalle(FacturaDetalle idFacturaDetalle) {
		this.idFacturaDetalle = idFacturaDetalle;
	}

	public ConValores getIdImpuesto() {
		return idImpuesto;
	}

	public void setIdImpuesto(ConValores idImpuesto) {
		this.idImpuesto = idImpuesto;
	}

	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public long getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(long creadoPor) {
		this.creadoPor = creadoPor;
	}

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public long getModificadoPor() {
		return modificadoPor;
	}

	public void setModificadoPor(long modificadoPor) {
		this.modificadoPor = modificadoPor;
	}
}
