package net.giro.cxc.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * factura_detalle
 * @author javaz
 *
 */
public class FacturaDetalle implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private long idFactura;
	private long idConcepto;
	private String descripcionConcepto;
	private String claveSat;
	private long idUnidadMedida;
	private String claveUnidadMedida;
	private double porcentajeIva;
	private double porcentajeRetencion;
	private BigDecimal cantidad;
	private BigDecimal costo;
	private BigDecimal importe;
	private BigDecimal total;
	private BigDecimal impuestos;
	private BigDecimal retenciones;
	private Date fechaCreacion;
	private long creadoPor;
	private Date fechaModificacion;
	private long modificadoPor;
	
	public FacturaDetalle() {
		this.id = 0L;
		this.descripcionConcepto = "";
		this.claveUnidadMedida = "";
		this.claveSat = "";
		this.cantidad = BigDecimal.ZERO;
		this.costo = BigDecimal.ZERO;
		this.total = BigDecimal.ZERO;
		this.impuestos = BigDecimal.ZERO;
		this.retenciones = BigDecimal.ZERO;
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

	public long getIdFactura() {
		return idFactura;
	}

	public void setIdFactura(long idFactura) {
		this.idFactura = idFactura;
	}

	public long getIdConcepto() {
		return idConcepto;
	}

	public void setIdConcepto(long idConcepto) {
		this.idConcepto = idConcepto;
	}

	public String getDescripcionConcepto() {
		return descripcionConcepto;
	}

	public void setDescripcionConcepto(String descripcionConcepto) {
		this.descripcionConcepto = descripcionConcepto;
	}

	public String getClaveSat() {
		return claveSat;
	}

	public void setClaveSat(String claveSat) {
		this.claveSat = claveSat;
	}

	public long getIdUnidadMedida() {
		return idUnidadMedida;
	}

	public void setIdUnidadMedida(long idUnidadMedida) {
		this.idUnidadMedida = idUnidadMedida;
	}

	public String getClaveUnidadMedida() {
		return claveUnidadMedida;
	}

	public void setClaveUnidadMedida(String claveUnidadMedida) {
		this.claveUnidadMedida = claveUnidadMedida;
	}

	public double getPorcentajeIva() {
		return porcentajeIva;
	}

	public void setPorcentajeIva(double porcentajeIva) {
		this.porcentajeIva = porcentajeIva;
	}

	public double getPorcentajeRetencion() {
		return porcentajeRetencion;
	}

	public void setPorcentajeRetencion(double porcentajeRetencion) {
		this.porcentajeRetencion = porcentajeRetencion;
	}

	public BigDecimal getCantidad() {
		return cantidad;
	}

	public void setCantidad(BigDecimal cantidad) {
		cantidad = (cantidad != null ? cantidad : BigDecimal.ZERO);
		this.cantidad = cantidad;
		this.getTotal();
	}

	public BigDecimal getCosto() {
		return costo;
	}

	public void setCosto(BigDecimal costo) {
		costo = (costo != null ? costo : BigDecimal.ZERO);
		this.costo = costo;
		this.getTotal();
	}

	public BigDecimal getImporte() {
		return importe;
	}

	public void setImporte(BigDecimal importe) { 
		importe = (importe != null ? importe : BigDecimal.ZERO);
		this.importe = importe;
	}

	public BigDecimal getImpuestos() {
		return impuestos;
	}

	public void setImpuestos(BigDecimal impuestos) {
		impuestos = (impuestos != null ? impuestos : BigDecimal.ZERO);
		this.impuestos = impuestos;
	}

	public BigDecimal getRetenciones() {
		return retenciones;
	}

	public void setRetenciones(BigDecimal retenciones) {
		retenciones = (retenciones != null ? retenciones : BigDecimal.ZERO);
		this.retenciones = retenciones;
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

	// --------------------------------------------------------------------------------------------------------------------------
	// EXTENDIDO
	// --------------------------------------------------------------------------------------------------------------------------

	public BigDecimal getTotal() {
		this.importe = (this.importe != null && this.importe.doubleValue() > 0 ? this.importe : new BigDecimal(this.cantidad.doubleValue() * this.costo.doubleValue()));
		this.total = this.getImporte();
		if (this.total.doubleValue() > 0) 
			this.total = this.total.add(this.impuestos.subtract(this.retenciones));
		if (this.total.doubleValue() < 0)
			this.total = BigDecimal.ZERO;
		return this.total;
	}

	public void setTotal(BigDecimal total) {
		total = (total != null ? total : BigDecimal.ZERO);
		this.total = total;
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */