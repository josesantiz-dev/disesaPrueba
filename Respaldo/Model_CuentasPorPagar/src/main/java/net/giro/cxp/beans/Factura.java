package net.giro.cxp.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Factura implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	long id;
	Long creadoPor;
	Date fechaCreacion;
	Long modificadoPor;
	Date fechaModificacion;
	Long idMovimientosCuentas;
	Long idProveedor;
	String referencia;
	Date fecha;
	BigDecimal subtotal;
	String observaciones;
	Long idConcepto;
	BigDecimal totalImpuestos;
	BigDecimal totalRetenciones;
	String estatus;
	
	public Factura(){
		
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

	public Long getIdMovimientosCuentas() {
		return idMovimientosCuentas;
	}

	public void setIdMovimientosCuentas(Long idMovimientosCuentas) {
		this.idMovimientosCuentas = idMovimientosCuentas;
	}

	public Long getIdProveedor() {
		return idProveedor;
	}

	public void setIdProveedor(Long idProveedor) {
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

	public Long getIdConcepto() {
		return idConcepto;
	}

	public void setIdConcepto(Long idConcepto) {
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
