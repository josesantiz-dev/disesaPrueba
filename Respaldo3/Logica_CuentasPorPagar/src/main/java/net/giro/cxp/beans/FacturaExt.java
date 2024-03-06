package net.giro.cxp.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import net.giro.plataforma.beans.ConValores;

public class FacturaExt implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	long id;
	Long creadoPor;
	Date fechaCreacion;
	Long modificadoPor;
	Date fechaModificacion;
	//MovimientosCuentasExt idMovimientosCuentas;
	PagosGastos idMovimientosCuentas;
	PersonaExt idProveedor;
	String referencia;
	Date fecha;
	BigDecimal subtotal;
	String observaciones;
	ConValores idConcepto;
	BigDecimal totalImpuestos;
	BigDecimal totalRetenciones;
	String estatus;
	
	public FacturaExt(){
		this.subtotal = new BigDecimal(0L);
		this.totalImpuestos = new BigDecimal(0L);
		this.totalRetenciones = new BigDecimal(0L);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
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

	public PagosGastos getIdMovimientosCuentas() {
		return idMovimientosCuentas;
	}

	public void setIdMovimientosCuentas(PagosGastos idMovimientosCuentas) {
		this.idMovimientosCuentas = idMovimientosCuentas;
	}

	public PersonaExt getIdProveedor() {
		return idProveedor;
	}

	public void setIdProveedor(PersonaExt idProveedor) {
		this.idProveedor = idProveedor;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public BigDecimal getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public ConValores getIdConcepto() {
		return idConcepto;
	}

	public void setIdConcepto(ConValores idConcepto) {
		this.idConcepto = idConcepto;
	}

	public BigDecimal getTotalImpuestos() {
		return totalImpuestos;
	}

	public void setTotalImpuestos(BigDecimal totalImpuestos) {
		this.totalImpuestos = totalImpuestos;
	}

	public BigDecimal getTotalRetenciones() {
		return totalRetenciones;
	}

	public void setTotalRetenciones(BigDecimal totalRetenciones) {
		this.totalRetenciones = totalRetenciones;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}
}
