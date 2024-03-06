package net.giro.cxc.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import net.giro.plataforma.beans.ConValores;

public class FacturaDetalleExt implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private FacturaExt idFactura;
	private ConceptoFacturacion idConcepto;
	private String descripcionConcepto;
	private String claveSat;
	private ConValores idUnidadMedida;
	private double porcentajeIva;
	private double porcentajeRetencion;
	private BigDecimal cantidad;
	private BigDecimal costo;
	private BigDecimal impuestos;
	private BigDecimal retenciones;
	private BigDecimal total;
	private Date fechaCreacion;
	private long creadoPor;
	private Date fechaModificacion;
	private long modificadoPor; 
	
	
	public FacturaDetalleExt() {
		this.descripcionConcepto = "";
		this.claveSat = "";
		this.cantidad = BigDecimal.ZERO;
		this.costo = BigDecimal.ZERO;
		this.impuestos = BigDecimal.ZERO;
		this.retenciones = BigDecimal.ZERO;
		this.total = BigDecimal.ZERO;
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	public FacturaDetalleExt(Long id) {
		this();
		this.id = id;
	}
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public FacturaExt getIdFactura() {
		return idFactura;
	}

	public void setIdFactura(FacturaExt idFactura) {
		this.idFactura = idFactura;
	}

	public ConceptoFacturacion getIdConcepto() {
		return idConcepto;
	}

	public void setIdConcepto(ConceptoFacturacion idConcepto) {
		this.idConcepto = idConcepto;
		if (this.idConcepto != null) {
			this.descripcionConcepto = this.idConcepto.getDescripcion();
			this.claveSat = this.idConcepto.getClaveSat();
		}
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

	public ConValores getIdUnidadMedida() {
		return idUnidadMedida;
	}

	public void setIdUnidadMedida(ConValores idUnidadMedida) {
		this.idUnidadMedida = idUnidadMedida;
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
		this.cantidad = BigDecimal.ZERO;
		if (cantidad != null)
			this.cantidad = cantidad;
		this.getTotal();
	}

	public BigDecimal getCosto() {
		return costo;
	}

	public void setCosto(BigDecimal costo) {
		this.costo = BigDecimal.ZERO;
		if (costo != null)
			this.costo = costo;
		this.getTotal();
	}

	public BigDecimal getImpuestos() {
		return impuestos;
	}

	public void setImpuestos(BigDecimal impuestos) {
		this.impuestos = BigDecimal.ZERO;
		if (impuestos != null)
			this.impuestos = impuestos;
	}

	public BigDecimal getRetenciones() {
		return retenciones;
	}

	public void setRetenciones(BigDecimal retenciones) {
		this.retenciones = BigDecimal.ZERO;
		if (retenciones != null)
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

	public BigDecimal getImporte() {
		if ((this.costo != null && this.costo.doubleValue() > 0) && (this.cantidad != null && this.cantidad.doubleValue() > 0))
			return this.costo.multiply(this.cantidad);
		return BigDecimal.ZERO;
	}

	public void setImporte(BigDecimal importe) { }

	public BigDecimal getTotal() {
		this.total = this.getImporte();
		if (this.total.doubleValue() > 0) 
			this.total = this.total.add(this.impuestos.subtract(this.retenciones));
		if (this.total.doubleValue() < 0)
			this.total = BigDecimal.ZERO;
		return this.total;
	}

	public void setTotal(BigDecimal total) { }
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */