package net.giro.cxp.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * b004dc7aa3
 * @author javaz
 *
 */
public class Factura implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long idMovimientosCuentas;
	private Long idProveedor;
	private Long idConcepto;
	private String referencia;
	private Date fecha;
	private BigDecimal subtotal;
	private String observaciones;
	private BigDecimal totalImpuestos;
	private BigDecimal totalRetenciones;
	private String estatus;
	private Long creadoPor;
	private Date fechaCreacion;
	private Long modificadoPor;
	private Date fechaModificacion;
	
	public Factura() {
		this.id = 0L;
		this.referencia = "";
		this.observaciones = "";
		this.subtotal = BigDecimal.ZERO;
		this.totalImpuestos = BigDecimal.ZERO;
		this.totalRetenciones = BigDecimal.ZERO;
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	public Factura(Long id) {
		this();
		this.id = id;
	}

	public long getId() {
		return (this.id == null) ? 0L : this.id.longValue();
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
