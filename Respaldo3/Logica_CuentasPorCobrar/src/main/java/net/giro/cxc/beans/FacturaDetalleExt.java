package net.giro.cxc.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
	private BigDecimal importe;
	private BigDecimal impuestos;
	private BigDecimal retenciones;
	private BigDecimal total;
	private Date fechaCreacion;
	private long creadoPor;
	private Date fechaModificacion;
	private long modificadoPor; 
	// -------------------------------------------------------------
	private List<FacturaDetalleImpuestoExt> listImpuestos;
	
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
		// -------------------------------------------------------------
		this.listImpuestos = new ArrayList<FacturaDetalleImpuestoExt>();
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

	public String getTotalOrigen() {
		DecimalFormat format = new DecimalFormat("###,###,##0.00");
		return format.format(getImporte()) + " + (" + format.format(this.impuestos) + " - " + format.format(this.retenciones) + ")";
	}

	public List<FacturaDetalleImpuestoExt> getListImpuestos() {
		return listImpuestos;
	}

	public void setListImpuestos(List<FacturaDetalleImpuestoExt> listImpuestos) {
		listImpuestos = (listImpuestos != null ? listImpuestos : new ArrayList<FacturaDetalleImpuestoExt>());
		this.listImpuestos = listImpuestos;
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */