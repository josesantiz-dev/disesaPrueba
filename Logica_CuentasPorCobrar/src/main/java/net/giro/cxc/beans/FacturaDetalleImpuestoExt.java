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
	private double tasa;
	private String tipoFactor;
	private BigDecimal base;
	private BigDecimal importe;
	private Date fechaCreacion;
	private long creadoPor;
	private Date fechaModificacion;
	private long modificadoPor;

	public FacturaDetalleImpuestoExt() {
		this.tipoFactor = "";
		this.base = BigDecimal.ZERO;
		this.importe = BigDecimal.ZERO;
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

	public double getTasa() {
		return tasa;
	}

	public void setTasa(double tasa) {
		this.tasa = tasa;
	}

	public String getTipoFactor() {
		return tipoFactor;
	}

	public void setTipoFactor(String tipoFactor) {
		this.tipoFactor = tipoFactor;
	}

	public BigDecimal getBase() {
		return base;
	}

	public void setBase(BigDecimal base) {
		base = (base != null ? base : BigDecimal.ZERO);
		if (base.doubleValue() > 0 && base.doubleValue() != this.base.doubleValue())
			recalcularImporte(base);
		this.base = base;
	}

	public BigDecimal getImporte() {
		return importe;
	}

	public void setImporte(BigDecimal importe) {
		importe = (importe != null ? importe : BigDecimal.ZERO);
		this.importe = importe;
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
	
	private void recalcularImporte(BigDecimal base) {
		double porcentaje = 0;
		double valor = 0;
		
		this.tasa = (this.tasa > 0 ? this.tasa : 0);
		porcentaje = (this.tasa > 1 ? this.tasa / 100 : this.tasa);
		valor = base.doubleValue() * porcentaje;
		setImporte(new BigDecimal(valor));
	}
}
