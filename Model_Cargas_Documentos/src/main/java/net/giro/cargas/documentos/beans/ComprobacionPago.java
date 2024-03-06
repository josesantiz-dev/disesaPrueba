package net.giro.cargas.documentos.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

public class ComprobacionPago implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private int estatus;
	private String descripcion;
	private String expresionImpresa;
	private String serie;
	private String folio;
	private Date fecha;
	private Date fechaTimbrado;
	private String folioFiscal;
	// Emisor
	private String emisor;
	private String emisorRfc;
	private String emisorRegimen;
	// Receptor
	private String receptor;
	private String receptorRfc;
	private String receptorUsoCfdi;
	// Pago
	private Date pagoFecha;
	private String pagoFormaPago;
	private String pagoMoneda;
	private BigDecimal pagoMonto;
	private String pagoNumOperacion;
	// Factura
	private String facturaFolioFiscal;
	private String facturaSerie;
	private String facturaFolio;
	private String facturaParcialidad;
	private BigDecimal facturaPagado;
	private BigDecimal facturaSaldoAnterior;
	private BigDecimal facturaSaldoInsoluto;
	private String facturaMetodoPago;
	private String facturaMoneda;
	// Auditoria
	private long idEmpresa;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	public ComprobacionPago() {
		this.descripcion = "";
		this.expresionImpresa = "";
		this.serie = "";
		this.folio = "";
		this.fecha = Calendar.getInstance().getTime();
		this.fechaTimbrado = Calendar.getInstance().getTime();
		this.folioFiscal = "";
		// Emisor
		this.emisor = "";
		this.emisorRfc = "";
		this.emisorRegimen = "";
		// Receptor
		this.receptor = "";
		this.receptorRfc = "";
		this.receptorUsoCfdi = "";
		// Pago
		this.pagoFecha = Calendar.getInstance().getTime();
		this.pagoFormaPago = "";
		this.pagoMoneda = "";
		this.pagoMonto = BigDecimal.ZERO;
		this.pagoNumOperacion = "";
		// Factura
		this.facturaFolioFiscal = "";
		this.facturaSerie = "";
		this.facturaFolio = "";
		this.facturaParcialidad = "";
		this.facturaPagado = BigDecimal.ZERO;
		this.facturaSaldoAnterior = BigDecimal.ZERO;
		this.facturaSaldoInsoluto = BigDecimal.ZERO;
		this.facturaMetodoPago = "";
		this.facturaMoneda = "";
		// Auditoria
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

	public int getEstatus() {
		return estatus;
	}

	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getExpresionImpresa() {
		return expresionImpresa;
	}

	public void setExpresionImpresa(String expresionImpresa) {
		this.expresionImpresa = expresionImpresa;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
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

	public Date getFechaTimbrado() {
		return fechaTimbrado;
	}

	public void setFechaTimbrado(Date fechaTimbrado) {
		this.fechaTimbrado = fechaTimbrado;
	}

	public String getFolioFiscal() {
		return folioFiscal;
	}

	public void setFolioFiscal(String folioFiscal) {
		this.folioFiscal = folioFiscal;
	}

	public String getEmisor() {
		return emisor;
	}

	public void setEmisor(String emisor) {
		this.emisor = emisor;
	}

	public String getEmisorRfc() {
		return emisorRfc;
	}

	public void setEmisorRfc(String emisorRfc) {
		this.emisorRfc = emisorRfc;
	}

	public String getEmisorRegimen() {
		return emisorRegimen;
	}

	public void setEmisorRegimen(String emisorRegimen) {
		this.emisorRegimen = emisorRegimen;
	}

	public String getReceptor() {
		return receptor;
	}

	public void setReceptor(String receptor) {
		this.receptor = receptor;
	}

	public String getReceptorRfc() {
		return receptorRfc;
	}

	public void setReceptorRfc(String receptorRfc) {
		this.receptorRfc = receptorRfc;
	}

	public String getReceptorUsoCfdi() {
		return receptorUsoCfdi;
	}

	public void setReceptorUsoCfdi(String receptorUsoCfdi) {
		this.receptorUsoCfdi = receptorUsoCfdi;
	}

	public Date getPagoFecha() {
		return pagoFecha;
	}

	public void setPagoFecha(Date pagoFecha) {
		this.pagoFecha = pagoFecha;
	}

	public String getPagoFormaPago() {
		return pagoFormaPago;
	}

	public void setPagoFormaPago(String pagoFormaPago) {
		this.pagoFormaPago = pagoFormaPago;
	}

	public String getPagoMoneda() {
		return pagoMoneda;
	}

	public void setPagoMoneda(String pagoMoneda) {
		this.pagoMoneda = pagoMoneda;
	}

	public BigDecimal getPagoMonto() {
		return pagoMonto;
	}

	public void setPagoMonto(BigDecimal pagoMonto) {
		this.pagoMonto = pagoMonto;
	}

	public String getPagoNumOperacion() {
		return pagoNumOperacion;
	}

	public void setPagoNumOperacion(String pagoNumOperacion) {
		this.pagoNumOperacion = pagoNumOperacion;
	}

	public String getFacturaFolioFiscal() {
		return facturaFolioFiscal;
	}

	public void setFacturaFolioFiscal(String facturaFolioFiscal) {
		this.facturaFolioFiscal = facturaFolioFiscal;
	}

	public String getFacturaSerie() {
		return facturaSerie;
	}

	public void setFacturaSerie(String facturaSerie) {
		this.facturaSerie = facturaSerie;
	}

	public String getFacturaFolio() {
		return facturaFolio;
	}

	public void setFacturaFolio(String facturaFolio) {
		this.facturaFolio = facturaFolio;
	}

	public String getFacturaParcialidad() {
		return facturaParcialidad;
	}

	public void setFacturaParcialidad(String facturaParcialidad) {
		this.facturaParcialidad = facturaParcialidad;
	}

	public BigDecimal getFacturaPagado() {
		return facturaPagado;
	}

	public void setFacturaPagado(BigDecimal facturaPagado) {
		this.facturaPagado = facturaPagado;
	}

	public BigDecimal getFacturaSaldoAnterior() {
		return facturaSaldoAnterior;
	}

	public void setFacturaSaldoAnterior(BigDecimal facturaSaldoAnterior) {
		this.facturaSaldoAnterior = facturaSaldoAnterior;
	}

	public BigDecimal getFacturaSaldoInsoluto() {
		return facturaSaldoInsoluto;
	}

	public void setFacturaSaldoInsoluto(BigDecimal facturaSaldoInsoluto) {
		this.facturaSaldoInsoluto = facturaSaldoInsoluto;
	}

	public String getFacturaMetodoPago() {
		return facturaMetodoPago;
	}

	public void setFacturaMetodoPago(String facturaMetodoPago) {
		this.facturaMetodoPago = facturaMetodoPago;
	}

	public String getFacturaMoneda() {
		return facturaMoneda;
	}

	public void setFacturaMoneda(String facturaMoneda) {
		this.facturaMoneda = facturaMoneda;
	}

	public long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(long idEmpresa) {
		this.idEmpresa = idEmpresa;
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
