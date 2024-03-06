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
public class ObraCobranza implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Obra idObra;
	private String nombreObra;
	private BigDecimal porcentajeIva;
	private BigDecimal porcentajeAnticipo;
	private BigDecimal porcentajeRetencion;
	private Long idFactura;
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
	private Long idConcepto;
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
	private long modificadoPor;
	private Date fechaModificacion;
	
	public ObraCobranza() {
		this.fecha = Calendar.getInstance().getTime();
		//this.monto = BigDecimal.ZERO;
		this.anticipo = BigDecimal.ZERO;
		this.estimacion = BigDecimal.ZERO;
		this.amortizacion = BigDecimal.ZERO;
		this.fondoGarantia = BigDecimal.ZERO;
		this.setPagoFondoGarantia(BigDecimal.ZERO);
		this.subtotal =  BigDecimal.ZERO;
		this.iva =  BigDecimal.ZERO;
		this.total = BigDecimal.ZERO;
		this.facturaSaldo = BigDecimal.ZERO;
		this.facturaCobrado = BigDecimal.ZERO;
		this.porcentajeIva = BigDecimal.ZERO;
		this.porcentajeAnticipo = BigDecimal.ZERO;
		this.porcentajeRetencion = BigDecimal.ZERO;
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
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

	public Obra getIdObra() {
		return idObra;
	}

	public void setIdObra(Obra idObra) {
		this.idObra = idObra;
		if (idObra != null)
			this.nombreObra = idObra.getNombre();
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

	public void setPorcentajeIva(BigDecimal value) {
		value = (value != null ? value: BigDecimal.ZERO);
		this.porcentajeIva = value;
	}

	public BigDecimal getPorcentajeAnticipo() {
		return porcentajeAnticipo;
	}

	public void setPorcentajeAnticipo(BigDecimal value) {
		value = (value != null ? value: BigDecimal.ZERO);
		this.porcentajeAnticipo = value;
	}

	public BigDecimal getPorcentajeRetencion() {
		return porcentajeRetencion;
	}

	public void setPorcentajeRetencion(BigDecimal value) {
		value = (value != null ? value: BigDecimal.ZERO);
		this.porcentajeRetencion = value;
	}

	public Long getIdFactura() {
		return idFactura;
	}

	public void setIdFactura(Long value) {
		value = (value != null ? value: 0L);
		this.idFactura = value;
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

	public void setFacturaTotal(BigDecimal value) {
		value = (value != null ? value: BigDecimal.ZERO);
		this.facturaTotal = value;
	}

	public BigDecimal getFacturaSaldo() {
		return facturaSaldo;
	}

	public void setFacturaSaldo(BigDecimal value) {
		value = (value != null ? value: BigDecimal.ZERO);
		this.facturaSaldo = value;
	}

	public BigDecimal getFacturaCobrado() {
		return facturaCobrado;
	}

	public void setFacturaCobrado(BigDecimal value) {
		value = (value != null ? value: BigDecimal.ZERO);
		this.facturaCobrado = value;
	}

	public BigDecimal getFacturaCobradoPesos() {
		return facturaCobradoPesos;
	}

	public void setFacturaCobradoPesos(BigDecimal value) {
		value = (value != null ? value: BigDecimal.ZERO);
		this.facturaCobradoPesos = value;
	}

	public double getTipoCambio() {
		return tipoCambio;
	}

	public void setTipoCambio(double tipoCambio) {
		this.tipoCambio = Math.abs(tipoCambio);
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

	public Long getIdConcepto() {
		return idConcepto;
	}

	public void setIdConcepto(Long value) {
		value = (value != null ? value : 0L);
		this.idConcepto = value;
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

	public void setMonto(BigDecimal value) {
		value = (value != null ? value: BigDecimal.ZERO);
		this.monto = new BigDecimal(Math.abs(value.doubleValue()));
	}*/

	public BigDecimal getAnticipo() {
		return anticipo;
	}

	public void setAnticipo(BigDecimal value) {
		value = (value != null ? value: BigDecimal.ZERO);
		this.anticipo = new BigDecimal(Math.abs(value.doubleValue()));
	}

	public BigDecimal getEstimacion() {
		return estimacion;
	}

	public void setEstimacion(BigDecimal value) {
		value = (value != null ? value: BigDecimal.ZERO);
		this.estimacion = new BigDecimal(Math.abs(value.doubleValue()));
	}

	public BigDecimal getAmortizacion() {
		return amortizacion;
	}

	public void setAmortizacion(BigDecimal value) {
		value = (value != null ? value: BigDecimal.ZERO);
		this.amortizacion = new BigDecimal(Math.abs(value.doubleValue()));
	}

	public BigDecimal getFondoGarantia() {
		return fondoGarantia;
	}

	public void setFondoGarantia(BigDecimal value) {
		value = (value != null ? value: BigDecimal.ZERO);
		this.fondoGarantia = new BigDecimal(Math.abs(value.doubleValue()));
	}		


	public BigDecimal getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(BigDecimal value) {
		value = (value != null ? value: BigDecimal.ZERO);
		this.subtotal = new BigDecimal(Math.abs(value.doubleValue()));
	}

	public BigDecimal getIva() {
		return iva;
	}

	public void setIva(BigDecimal value) {
		value = (value != null ? value: BigDecimal.ZERO);
		this.iva = new BigDecimal(Math.abs(value.doubleValue()));
	}

	public BigDecimal getCargos() {
		return cargos;
	}

	public void setCargos(BigDecimal value) {
		value = (value != null ? value: BigDecimal.ZERO);
		this.cargos = new BigDecimal(Math.abs(value.doubleValue()));
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal value) {
		value = (value != null ? value: BigDecimal.ZERO);
		this.total = new BigDecimal(Math.abs(value.doubleValue()));
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
	
	
	// ---------------------------------------------------------------------------
	// EXTENDIDOS
	// ---------------------------------------------------------------------------
	
	public boolean getEditable() {
		if (this.id == null)
			this.id = 0L;
		
		if (this.anticipo == null)
			this.anticipo = BigDecimal.ZERO;
		
		if (this.estimacion == null)
			this.estimacion = BigDecimal.ZERO;
		
		if (this.id <= 0L)
			return true;
		
		return (this.anticipo.doubleValue() <= 0 && this.estimacion.doubleValue() <= 0);
	}
	
	public void setEditable(boolean value) {}

	public BigDecimal getPagoFondoGarantia() {
		return pagoFondoGarantia;
	}

	public void setPagoFondoGarantia(BigDecimal value) {
		value = (value != null ? value: BigDecimal.ZERO);
		this.pagoFondoGarantia = new BigDecimal(Math.abs(value.doubleValue()));
	}




}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */