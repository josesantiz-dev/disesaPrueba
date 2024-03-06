package net.giro.adp.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * obra_subcontratista
 * @author javaz
 */
public class ObraSubcontratista implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Date fecha;
	private String tipoSubClave;
	private String descripcion;
	private Obra idObra;
	private long idEmpleado;
	private long idOrdenCompra;
	private String folioOrdenCompra;
	private BigDecimal totalOrdenCompra;
	private BigDecimal saldoOrdenCompra;
	private long idMoneda;
	private String moneda;
	private double tipoCambio;
	private double porcentajeAnticipo;
	private double porcentajeRetencion;
	private BigDecimal anticipo;
	private BigDecimal estimacion;
	private BigDecimal amortizacion;
	private BigDecimal fondoGarantia;
	private BigDecimal pagoFondoGarantia;
	private BigDecimal subtotal;
	private BigDecimal impuestos;
	private BigDecimal retenciones;
	private BigDecimal cargos;
	private BigDecimal total;
	// Factura 
	private Long idFactura;
	private String folioFactura;
	private long idCliente;
	private String nombreCliente;
	private String rfcCliente;
	private long idConcepto;
	private String concepto;
	private BigDecimal facturaTotal;
	private BigDecimal facturaTotalPesos;
	// Auditoria 
	private long idEmpresa;
	private int estatus;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;

	public ObraSubcontratista() {
		this.fecha = Calendar.getInstance().getTime();
		this.descripcion = "";
		this.folioOrdenCompra = "";
		this.tipoSubClave = "";
		this.moneda = "";
		this.totalOrdenCompra = BigDecimal.ZERO;
		this.saldoOrdenCompra = BigDecimal.ZERO;
		this.anticipo = BigDecimal.ZERO;
		this.estimacion = BigDecimal.ZERO;
		this.amortizacion = BigDecimal.ZERO;
		this.fondoGarantia = BigDecimal.ZERO;
		this.pagoFondoGarantia = BigDecimal.ZERO;
		this.subtotal = BigDecimal.ZERO;
		this.impuestos = BigDecimal.ZERO;
		this.retenciones = BigDecimal.ZERO;
		this.cargos = BigDecimal.ZERO;
		this.total = BigDecimal.ZERO;
		// -------------------------------------------------------
		this.folioFactura = "";
		this.nombreCliente = "";
		this.rfcCliente = "";
		this.concepto = "";
		this.facturaTotal = BigDecimal.ZERO;
		this.facturaTotalPesos = BigDecimal.ZERO;
		// -------------------------------------------------------
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

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		fecha = (fecha != null ? fecha : Calendar.getInstance().getTime());
		this.fecha = fecha;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Obra getIdObra() {
		return idObra;
	}

	public void setIdObra(Obra idObra) {
		this.idObra = idObra;
	}

	public long getIdOrdenCompra() {
		return idOrdenCompra;
	}

	public void setIdOrdenCompra(long idOrdenCompra) {
		this.idOrdenCompra = idOrdenCompra;
	}

	public String getFolioOrdenCompra() {
		return folioOrdenCompra;
	}

	public void setFolioOrdenCompra(String folioOrdenCompra) {
		this.folioOrdenCompra = folioOrdenCompra;
	}

	public BigDecimal getTotalOrdenCompra() {
		return totalOrdenCompra;
	}

	public void setTotalOrdenCompra(BigDecimal totalOrdenCompra) {
		this.totalOrdenCompra = totalOrdenCompra;
	}

	public BigDecimal getSaldoOrdenCompra() {
		return saldoOrdenCompra;
	}

	public void setSaldoOrdenCompra(BigDecimal saldoOrdenCompra) {
		this.saldoOrdenCompra = saldoOrdenCompra;
	}

	public long getIdMoneda() {
		return idMoneda;
	}

	public void setIdMoneda(long idMoneda) {
		this.idMoneda = idMoneda;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		moneda = (moneda != null ? moneda : "");
		this.moneda = moneda;
	}

	public double getTipoCambio() {
		return tipoCambio;
	}

	public void setTipoCambio(double tipoCambio) {
		this.tipoCambio = tipoCambio;
	}

	public double getPorcentajeAnticipo() {
		return porcentajeAnticipo;
	}

	public void setPorcentajeAnticipo(double porcentajeAnticipo) {
		this.porcentajeAnticipo = porcentajeAnticipo;
	}

	public double getPorcentajeRetencion() {
		return porcentajeRetencion;
	}

	public void setPorcentajeRetencion(double porcentajeRetencion) {
		this.porcentajeRetencion = porcentajeRetencion;
	}

	public BigDecimal getAnticipo() {
		return anticipo;
	}

	public void setAnticipo(BigDecimal anticipo) {
		anticipo = (anticipo != null ? anticipo : BigDecimal.ZERO);
		this.anticipo = anticipo;
	}

	public BigDecimal getEstimacion() {
		return estimacion;
	}

	public void setEstimacion(BigDecimal estimacion) {
		estimacion = (estimacion != null ? estimacion : BigDecimal.ZERO);
		this.estimacion = estimacion;
	}

	public BigDecimal getAmortizacion() {
		return amortizacion;
	}

	public void setAmortizacion(BigDecimal amortizacion) {
		amortizacion = (amortizacion != null ? amortizacion : BigDecimal.ZERO);
		this.amortizacion = amortizacion;
	}

	public BigDecimal getFondoGarantia() {
		return fondoGarantia;
	}

	public void setFondoGarantia(BigDecimal fondoGarantia) {
		fondoGarantia = (fondoGarantia != null ? fondoGarantia : BigDecimal.ZERO);
		this.fondoGarantia = fondoGarantia;
	}

	public BigDecimal getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(BigDecimal subtotal) {
		subtotal = (subtotal != null ? subtotal : BigDecimal.ZERO);
		this.subtotal = subtotal;
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

	public BigDecimal getCargos() {
		return cargos;
	}

	public void setCargos(BigDecimal cargos) {
		cargos = (cargos != null ? cargos : BigDecimal.ZERO);
		this.cargos = cargos;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		total = (total != null ? total : BigDecimal.ZERO);
		this.total = total;
	}

	// -------------------------------------------------------
	
	public Long getIdFactura() {
		return idFactura;
	}

	public void setIdFactura(Long idFactura) {
		idFactura = (idFactura != null ? idFactura : 0L);
		this.idFactura = idFactura;
	}

	public String getFolioFactura() {
		return folioFactura;
	}

	public void setFolioFactura(String folioFactura) {
		folioFactura = (folioFactura != null ? folioFactura : "");
		this.folioFactura = folioFactura;
	}

	public long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(long idCliente) {
		this.idCliente = idCliente;
	}

	public String getNombreCliente() {
		return nombreCliente;
	}

	public void setNombreCliente(String nombreCliente) {
		nombreCliente = (nombreCliente != null ? nombreCliente : "");
		this.nombreCliente = nombreCliente;
	}

	public String getRfcCliente() {
		return rfcCliente;
	}

	public void setRfcCliente(String rfcCliente) {
		rfcCliente = (rfcCliente != null ? rfcCliente : "");
		this.rfcCliente = rfcCliente;
	}

	public long getIdConcepto() {
		return idConcepto;
	}

	public void setIdConcepto(long idConcepto) {
		this.idConcepto = idConcepto;
	}
	
	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		concepto = (concepto != null ? concepto : "");
		this.concepto = concepto;
	}

	public BigDecimal getFacturaTotal() {
		return facturaTotal;
	}

	public void setFacturaTotal(BigDecimal facturaTotal) {
		facturaTotal = (facturaTotal != null ? facturaTotal : BigDecimal.ZERO);
		this.facturaTotal = facturaTotal;
	}

	public BigDecimal getFacturaTotalPesos() {
		return facturaTotalPesos;
	}

	public void setFacturaTotalPesos(BigDecimal facturaTotalPesos) {
		facturaTotalPesos = (facturaTotalPesos != null ? facturaTotalPesos : BigDecimal.ZERO);
		this.facturaTotalPesos = facturaTotalPesos;
	}

	// -------------------------------------------------------
	
	public long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public int getEstatus() {
		return estatus;
	}

	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}

	public long getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(long creadoPor) {
		this.creadoPor = creadoPor;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public long getModificadoPor() {
		return modificadoPor;
	}

	public void setModificadoPor(long modificadoPor) {
		this.modificadoPor = modificadoPor;
	}

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public BigDecimal getPagoFondoGarantia() {
		return pagoFondoGarantia;
	}

	public void setPagoFondoGarantia(BigDecimal pagoFondoGarantia) {
		pagoFondoGarantia = (pagoFondoGarantia != null ? pagoFondoGarantia : BigDecimal.ZERO);
		this.pagoFondoGarantia = pagoFondoGarantia;
	}

	public String getTipoSubClave() {
		return tipoSubClave;
	}

	public void setTipoSubClave(String tipoSubClave) {
		this.tipoSubClave = tipoSubClave;
	}

	public long getIdEmpleado() {
		return idEmpleado;
	}

	public void setIdEmpleado(long idEmpleado) {
		this.idEmpleado = idEmpleado;
	}
}
