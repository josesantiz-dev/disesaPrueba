package net.giro.adp.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * obra_cobranza
 * @author javaz
 *
 */
public class ObraCobranzaHistorico implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private long idOrigen;
	private long idObra;
	private String nombreObra;
	private BigDecimal porcentajeIva;
	private BigDecimal porcentajeAnticipo;
	private BigDecimal porcentajeRetencion;
	private long idFactura;
	private Date fecha;
	private String folio;
	private BigDecimal facturaTotal;
	private BigDecimal facturaSaldo;
	private BigDecimal facturaCobrado;
	private BigDecimal facturaCobradoPesos;
	private double tipoCambio;
	private long idMoneda;
	private String monedaNombre;
	private String monedaAbreviacion;
	private long idConcepto;
	private String concepto;
	//private BigDecimal monto;
	private BigDecimal anticipo;
	private BigDecimal estimacion;
	private BigDecimal amortizacion;
	private BigDecimal fondoGarantia;
	private BigDecimal pagoFondoGarantia;
	private BigDecimal subtotal;
	private BigDecimal iva;
	private BigDecimal cargos;
	private BigDecimal total;
	private long creadoPor;
	private Date fechaCreacion;
	
	public ObraCobranzaHistorico() {
		this.fecha = Calendar.getInstance().getTime();
		//this.monto = BigDecimal.ZERO;
		this.anticipo = BigDecimal.ZERO;
		this.estimacion = BigDecimal.ZERO;
		this.amortizacion = BigDecimal.ZERO;
		this.fondoGarantia = BigDecimal.ZERO;
		this.pagoFondoGarantia = BigDecimal.ZERO;
		this.subtotal =  BigDecimal.ZERO;
		this.iva =  BigDecimal.ZERO;
		this.total = BigDecimal.ZERO;
		this.facturaSaldo = BigDecimal.ZERO;
		this.facturaCobrado = BigDecimal.ZERO;
		this.porcentajeIva = BigDecimal.ZERO;
		this.porcentajeAnticipo = BigDecimal.ZERO;
		this.porcentajeRetencion = BigDecimal.ZERO;
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.nombreObra = "";
		this.folio = "";
		this.concepto = "";
		this.monedaNombre = "";
		this.monedaAbreviacion = "";
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

	public long getIdOrigen() {
		return idOrigen;
	}

	public void setIdOrigen(long idOrigen) {
		this.idOrigen = idOrigen;
	}

	public long getIdObra() {
		return idObra;
	}

	public void setIdObra(long idObra) {
		this.idObra = idObra;
	}

	public String getNombreObra() {
		return nombreObra;
	}

	public void setNombreObra(String nombreObra) {
		this.nombreObra = nombreObra;
	}

	public BigDecimal getPorcentajeIva() {
		return porcentajeIva;
	}

	public void setPorcentajeIva(BigDecimal porcentajeIva) {
		if (porcentajeIva == null)
			porcentajeIva = BigDecimal.ZERO;
		this.porcentajeIva = porcentajeIva;
	}

	public BigDecimal getPorcentajeAnticipo() {
		return porcentajeAnticipo;
	}

	public void setPorcentajeAnticipo(BigDecimal porcentajeAnticipo) {
		if (porcentajeAnticipo == null)
			porcentajeAnticipo = BigDecimal.ZERO;
		this.porcentajeAnticipo = porcentajeAnticipo;
	}

	public BigDecimal getPorcentajeRetencion() {
		return porcentajeRetencion;
	}

	public void setPorcentajeRetencion(BigDecimal porcentajeRetencion) {
		if (porcentajeRetencion == null)
			porcentajeRetencion = BigDecimal.ZERO;
		this.porcentajeRetencion = porcentajeRetencion;
	}

	public long getIdFactura() {
		return idFactura;
	}

	public void setIdFactura(long idFactura) {
		this.idFactura = idFactura;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getFolio() {
		return folio;
	}

	public void setFolio(String folio) {
		this.folio = folio;
	}

	public BigDecimal getFacturaTotal() {
		return facturaTotal;
	}

	public void setFacturaTotal(BigDecimal facturaTotal) {
		if (facturaTotal == null)
			facturaTotal = BigDecimal.ZERO;
		this.facturaTotal = facturaTotal;
	}

	public BigDecimal getFacturaSaldo() {
		return facturaSaldo;
	}

	public void setFacturaSaldo(BigDecimal facturaSaldo) {
		if (facturaSaldo == null)
			facturaSaldo = BigDecimal.ZERO;
		this.facturaSaldo = facturaSaldo;
	}

	public BigDecimal getFacturaCobrado() {
		return facturaCobrado;
	}

	public void setFacturaCobrado(BigDecimal facturaCobrado) {
		if (facturaCobrado == null)
			facturaCobrado = BigDecimal.ZERO;
		this.facturaCobrado = facturaCobrado;
	}

	public BigDecimal getFacturaCobradoPesos() {
		return facturaCobradoPesos;
	}

	public void setFacturaCobradoPesos(BigDecimal facturaCobradoPesos) {
		if (facturaCobradoPesos == null)
			facturaCobradoPesos = BigDecimal.ZERO;
		this.facturaCobradoPesos = facturaCobradoPesos;
	}

	public double getTipoCambio() {
		return tipoCambio;
	}

	public void setTipoCambio(double tipoCambio) {
		this.tipoCambio = tipoCambio;
	}

	public long getIdMoneda() {
		return idMoneda;
	}

	public void setIdMoneda(long idMoneda) {
		this.idMoneda = idMoneda;
	}

	public String getMonedaNombre() {
		return monedaNombre;
	}

	public void setMonedaNombre(String monedaNombre) {
		this.monedaNombre = monedaNombre;
	}

	public String getMonedaAbreviacion() {
		return monedaAbreviacion;
	}

	public void setMonedaAbreviacion(String monedaAbreviacion) {
		this.monedaAbreviacion = monedaAbreviacion;
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
		this.concepto = concepto;
	}

	/*public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		if (monto == null)
			monto = BigDecimal.ZERO;
		this.monto = monto;
	}*/

	public BigDecimal getAnticipo() {
		return anticipo;
	}

	public void setAnticipo(BigDecimal anticipo) {
		if (anticipo == null)
			anticipo = BigDecimal.ZERO;
		this.anticipo = anticipo;
	}

	public BigDecimal getEstimacion() {
		return estimacion;
	}

	public void setEstimacion(BigDecimal estimacion) {
		if (estimacion == null)
			estimacion = BigDecimal.ZERO;
		this.estimacion = estimacion;
	}

	public BigDecimal getAmortizacion() {
		return amortizacion;
	}

	public void setAmortizacion(BigDecimal amortizacion) {
		if (amortizacion == null)
			amortizacion = BigDecimal.ZERO;
		this.amortizacion = amortizacion;
	}

	public BigDecimal getFondoGarantia() {
		return fondoGarantia;
	}

	public void setFondoGarantia(BigDecimal fondoGarantia) {
		if (fondoGarantia == null)
			fondoGarantia = BigDecimal.ZERO;
		this.fondoGarantia = fondoGarantia;
	}

	public BigDecimal getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(BigDecimal subtotal) {
		if (subtotal == null)
			subtotal = BigDecimal.ZERO;
		this.subtotal = subtotal;
	}

	public BigDecimal getIva() {
		return iva;
	}

	public void setIva(BigDecimal iva) {
		if (iva == null)
			iva = BigDecimal.ZERO;
		this.iva = iva;
	}

	public BigDecimal getCargos() {
		return cargos;
	}

	public void setCargos(BigDecimal cargos) {
		if (cargos == null)
			cargos = BigDecimal.ZERO;
		this.cargos = cargos;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		if (total == null)
			total = BigDecimal.ZERO;
		this.total = total;
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

	public BigDecimal getPagoFondoGarantia() {
		return pagoFondoGarantia;
	}

	public void setPagoFondoGarantia(BigDecimal pagoFondoGarantia) {
		if (pagoFondoGarantia == null)
			pagoFondoGarantia = BigDecimal.ZERO;
		this.pagoFondoGarantia = pagoFondoGarantia;
	}
	// --------------------------------------------------------------------
	// METODOS
	// --------------------------------------------------------------------
	
	public void fromCobranza(ObraCobranza itemCobranza) {
		if (itemCobranza == null)
			return;
		this.idOrigen = itemCobranza.getId();
		this.idObra = itemCobranza.getIdObra().getId();
		this.nombreObra = itemCobranza.getNombreObra();
		this.porcentajeIva = itemCobranza.getPorcentajeIva();
		this.porcentajeAnticipo = itemCobranza.getPorcentajeAnticipo();
		this.porcentajeRetencion = itemCobranza.getPorcentajeRetencion();
		this.idFactura = itemCobranza.getIdFactura();
		this.fecha = itemCobranza.getFecha();
		this.folio = itemCobranza.getFolio();
		this.facturaTotal = itemCobranza.getFacturaTotal();
		this.facturaSaldo = itemCobranza.getFacturaSaldo();
		this.facturaCobrado = itemCobranza.getFacturaCobrado();
		this.tipoCambio = itemCobranza.getTipoCambio();
		this.idMoneda = itemCobranza.getIdMoneda();
		this.monedaNombre = itemCobranza.getMonedaNombre();
		this.monedaAbreviacion = itemCobranza.getMonedaAbreviacion();
		this.idConcepto = itemCobranza.getIdConcepto();
		this.concepto = itemCobranza.getConcepto();
		//this.monto = itemCobranza.getMonto();
		this.anticipo = itemCobranza.getAnticipo();
		this.estimacion = itemCobranza.getEstimacion();
		this.amortizacion = itemCobranza.getAmortizacion();
		this.fondoGarantia = itemCobranza.getFondoGarantia();
		this.pagoFondoGarantia = itemCobranza.getPagoFondoGarantia();
		this.subtotal = itemCobranza.getSubtotal();
		this.iva = itemCobranza.getIva();
		this.cargos = itemCobranza.getCargos();
		this.total = itemCobranza.getTotal();
		this.creadoPor = itemCobranza.getModificadoPor();
		this.fechaCreacion = itemCobranza.getFechaCreacion();
	}

}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */