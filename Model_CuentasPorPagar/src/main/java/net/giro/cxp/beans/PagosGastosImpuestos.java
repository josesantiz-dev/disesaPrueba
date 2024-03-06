package net.giro.cxp.beans;

import java.io.Serializable;
import java.math.BigDecimal;

public class PagosGastosImpuestos implements Serializable {
	private static final long serialVersionUID = 1L;
	private long id;
	private Long idMovimientosCuentas;
	private java.lang.Long idValorImpuesto;
	private BigDecimal valor;
	private java.lang.Long creadoPor;
	private java.util.Date fechaCreacion;
	private java.lang.Long modificadoPor;
	private java.util.Date fechaModificacion;

	public PagosGastosImpuestos() {
		this.id = 0L;}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public java.lang.Long getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(java.lang.Long creadoPor) {
		this.creadoPor = creadoPor;
	}

	public java.util.Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(java.util.Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public java.lang.Long getModificadoPor() {
		return modificadoPor;
	}

	public void setModificadoPor(java.lang.Long modificadoPor) {
		this.modificadoPor = modificadoPor;
	}

	public java.util.Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(java.util.Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public Long getIdMovimientosCuentas() {
		return idMovimientosCuentas;
	}

	public void setIdMovimientosCuentas(Long idMovimientosCuentas) {
		this.idMovimientosCuentas = idMovimientosCuentas;
	}

	public java.lang.Long getIdValorImpuesto() {
		return idValorImpuesto;
	}

	public void setIdValorImpuesto(java.lang.Long idValorImpuesto) {
		this.idValorImpuesto = idValorImpuesto;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
}
