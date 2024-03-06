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
	private Long idFactura;
	private String folio;
	private Date fecha;
	private Long idConcepto;
	private String concepto;
	private BigDecimal monto;
	private BigDecimal anticipo;
	private BigDecimal estimacion;
	private BigDecimal amortizacion;
	private BigDecimal fondoGarantia;
	private BigDecimal subtotal;
	private BigDecimal iva;
	private BigDecimal total;
	private BigDecimal porcentajeAnticipo;
	private BigDecimal porcentajeRetencion;
	private BigDecimal facturaSaldo;
	private BigDecimal facturaCobrado;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	
	public ObraCobranza() {
		this.fecha = Calendar.getInstance().getTime();
		this.monto = BigDecimal.ZERO;
		this.anticipo = BigDecimal.ZERO;
		this.estimacion = BigDecimal.ZERO;
		this.amortizacion = BigDecimal.ZERO;
		this.fondoGarantia = BigDecimal.ZERO;
		this.subtotal =  BigDecimal.ZERO;
		this.iva =  BigDecimal.ZERO;
		this.total = BigDecimal.ZERO;
		this.facturaSaldo = BigDecimal.ZERO;
		this.facturaCobrado = BigDecimal.ZERO;
		this.porcentajeAnticipo = BigDecimal.ZERO;
		this.porcentajeRetencion = BigDecimal.ZERO;
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	public ObraCobranza(Long id) {
		this();
		this.id = id;
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
	}

	public String getNombreObra() {
		return nombreObra;
	}

	public void setNombreObra(String nombreObra) {
		this.nombreObra = nombreObra;
	}

	public Long getIdFactura() {
		return idFactura;
	}

	public void setIdFactura(Long idFactura) {
		this.idFactura = idFactura;
	}

	public String getFolio() {
		return folio;
	}

	public void setFolio(String folio) {
		this.folio = folio;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getConcepto() {
		return concepto;
	}

	public Long getIdConcepto() {
		return idConcepto;
	}

	public void setIdConcepto(Long idConcepto) {
		this.idConcepto = idConcepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	public BigDecimal getAnticipo() {
		return anticipo;
	}

	public void setAnticipo(BigDecimal anticipo) {
		this.anticipo = anticipo;
	}

	public BigDecimal getEstimacion() {
		return estimacion;
	}

	public void setEstimacion(BigDecimal estimacion) {
		this.estimacion = estimacion;
	}

	public BigDecimal getAmortizacion() {
		return amortizacion;
	}

	public void setAmortizacion(BigDecimal amortizacion) {
		this.amortizacion = amortizacion;
	}

	public BigDecimal getFondoGarantia() {
		return fondoGarantia;
	}

	public void setFondoGarantia(BigDecimal fondoGarantia) {
		this.fondoGarantia = fondoGarantia;
	}

	public BigDecimal getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}

	public BigDecimal getIva() {
		return iva;
	}

	public void setIva(BigDecimal iva) {
		this.iva = iva;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal getFacturaSaldo() {
		return facturaSaldo;
	}

	public void setFacturaSaldo(BigDecimal facturaSaldo) {
		this.facturaSaldo = facturaSaldo;
	}

	public BigDecimal getFacturaCobrado() {
		return facturaCobrado;
	}

	public void setFacturaCobrado(BigDecimal facturaCobrado) {
		this.facturaCobrado = facturaCobrado;
	}

	public BigDecimal getPorcentajeAnticipo() {
		return porcentajeAnticipo;
	}

	public void setPorcentajeAnticipo(BigDecimal porcentajeAnticipo) {
		this.porcentajeAnticipo = porcentajeAnticipo;
	}

	public BigDecimal getPorcentajeRetencion() {
		return porcentajeRetencion;
	}

	public void setPorcentajeRetencion(BigDecimal porcentajeRetencion) {
		this.porcentajeRetencion = porcentajeRetencion;
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
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */