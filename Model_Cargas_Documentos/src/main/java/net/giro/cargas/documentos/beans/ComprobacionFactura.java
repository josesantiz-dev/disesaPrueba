package net.giro.cargas.documentos.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * c66ff17ccc
 * @author javaz
 */
public class ComprobacionFactura implements Serializable { 
	private static final long serialVersionUID = 1L;
	private Long id;
	private String estado;
	private String codigoEstatus;
	private String expresionImpresa;
	// -------------------------------------------------------------------------------------
	private Date facturaFecha;
	private String facturaFolioFiscal;
	private String facturaSerie;
	private String facturaFolio;
	private String facturaTipo;
	private String facturaMetodo;
	private String facturaFormaPago;
	private String facturaEmisorRfc; 
	private String facturaEmisor; 
	private String facturaReceptorRfc; 
	private String facturaReceptor;  
	private String facturaMoneda;
	private double facturaTipoCambio;
	private double facturaDescuento; 	// obtenido de XML: MXN o USD
	private double facturaSubtotal; 	// obtenido de XML: MXN o USD
	private double facturaImpuestos; 	// obtenido de XML: MXN o USD
	private double facturaRetenciones; 	// obtenido de XML: MXN o USD
	private double facturaTotal; 		// obtenido de XML: MXN o USD
	// -------------------------------------------------------------------------------------
	private double descuentoPesos; 		// descuento en pesos si corresponde
	private double subtotalPesos; 		// subtotal en pesos si corresponde
	private double impuestosPesos; 		// impuestos en pesos si corresponde
	private double retencionesPesos; 	// retenciones en pesos si corresponde
	private double totalPesos;    		// total en pesos si corresponde
	// -------------------------------------------------------------------------------------
	private long idEmpresa;
	private int estatus; // O: Activo, 1: Eliminado
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;

	public ComprobacionFactura() {
		this.estado = "";
		this.codigoEstatus = "";
		this.expresionImpresa = "";
		this.facturaFecha = Calendar.getInstance().getTime();
		this.facturaFolioFiscal = "";
		this.facturaSerie = "";
		this.facturaFolio = "";
		this.facturaTipo = "";
		this.facturaMetodo = "";
		this.facturaFormaPago = "";
		this.facturaEmisorRfc = "";
		this.facturaEmisor = "";
		this.facturaReceptorRfc = "";
		this.facturaReceptor = "";
		this.facturaMoneda = "";
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	/** @value aa */
	public Long getId() {
		return id;
	}

	/** @value aa */
	public void setId(Long id) {
		this.id = id;
	}

	/** @value aa */
	public void setId(long id) {
		this.id = id;
	}

	/** @value ai */
	public String getEstado() {
		return estado;
	}

	/** @value ai */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/** @value ah */
	public String getCodigoEstatus() {
		return codigoEstatus;
	}

	/** @value ah */
	public void setCodigoEstatus(String codigoEstatus) {
		this.codigoEstatus = codigoEstatus;
	}

	/** @value af */
	public String getExpresionImpresa() {
		if (this.expresionImpresa == null)
			this.expresionImpresa = "";
		return expresionImpresa;
	}

	/** @value af */
	public void setExpresionImpresa(String expresionImpresa) {
		this.expresionImpresa = expresionImpresa;
	}

	/** @value ag */
	public Date getFacturaFecha() {
		return facturaFecha;
	}

	/** @value ag */
	public void setFacturaFecha(Date facturaFecha) {
		this.facturaFecha = facturaFecha;
	}

	/** @value factura_uuid */
	public String getFacturaFolioFiscal() {
		return facturaFolioFiscal;
	}

	/** @value factura_uuid */
	public void setFacturaFolioFiscal(String facturaFolioFiscal) {
		this.facturaFolioFiscal = facturaFolioFiscal;
	}

	/** @value factura_serie */
	public String getFacturaSerie() {
		if (this.facturaSerie != null)
			return this.facturaSerie.trim();
		return "";
	}

	/** @value factura_serie */
	public void setFacturaSerie(String facturaSerie) {
		this.facturaSerie = facturaSerie;
	}

	/** @value factura_folio */
	public String getFacturaFolio() {
		if (this.facturaFolio != null)
			return this.facturaFolio.trim();
		return "";
	}

	/** @value factura_folio */
	public void setFacturaFolio(String facturaFolio) {
		this.facturaFolio = facturaFolio;
	}

	/** @value factura_tipo */
	public String getFacturaTipo() {
		if (this.facturaTipo == null)
			this.facturaTipo = "";
		return facturaTipo;
	}

	/** @value factura_tipo */
	public void setFacturaTipo(String facturaTipo) {
		if (facturaTipo == null)
			facturaTipo = "";
		this.facturaTipo = facturaTipo;
	}

	/** @value factura_metodo */
	public String getFacturaMetodo() {
		if (this.facturaMetodo == null)
			this.facturaMetodo = "";
		return facturaMetodo;
	}

	/** @value factura_metodo */
	public void setFacturaMetodo(String facturaMetodo) {
		if (facturaMetodo == null)
			facturaMetodo = "";
		this.facturaMetodo = facturaMetodo;
	}

	/** @value factura_forma_pago */
	public String getFacturaFormaPago() {
		if (this.facturaFormaPago == null)
			this.facturaFormaPago = "";
		return facturaFormaPago;
	}

	/** @value factura_forma_pago */
	public void setFacturaFormaPago(String facturaFormaPago) {
		if (facturaFormaPago == null)
			facturaFormaPago = "";
		this.facturaFormaPago = facturaFormaPago;
	}

	/** @value factura_emisor_rfc */
	public String getFacturaEmisorRfc() {
		return facturaEmisorRfc;
	}

	/** @value factura_emisor_rfc */
	public void setFacturaEmisorRfc(String facturaEmisorRfc) {
		this.facturaEmisorRfc = facturaEmisorRfc;
	}

	/** @value factura_razon_social */
	public String getFacturaEmisor() {
		return facturaEmisor;
	}

	/** @value factura_razon_social */
	public void setFacturaEmisor(String facturaEmisor) {
		this.facturaEmisor = facturaEmisor;
	}

	/** @value factura_receptor_rfc */
	public String getFacturaReceptorRfc() {
		return facturaReceptorRfc;
	}

	/** @value factura_receptor_rfc */
	public void setFacturaReceptorRfc(String facturaReceptorRfc) {
		this.facturaReceptorRfc = facturaReceptorRfc;
	}

	/** @value factura_receptor */
	public String getFacturaReceptor() {
		return facturaReceptor;
	}

	/** @value factura_receptor */
	public void setFacturaReceptor(String facturaReceptor) {
		this.facturaReceptor = facturaReceptor;
	}

	/** @value factura_moneda */
	public String getFacturaMoneda() {
		return facturaMoneda;
	}

	/** @value factura_moneda */
	public void setFacturaMoneda(String facturaMoneda) {
		this.facturaMoneda = facturaMoneda;
	}

	/** @value tipo_cambio */
	public double getFacturaTipoCambio() {
		return facturaTipoCambio;
	}

	/** @value tipo_cambio */
	public void setFacturaTipoCambio(double facturaTipoCambio) {
		this.facturaTipoCambio = facturaTipoCambio;
	}

	/** Descuento de la Factura en XML (pesos o dolares)
	 * @value factura_descuento
	 * @return
	 */
	public double getFacturaDescuento() {
		return facturaDescuento;
	}

	/** Descuento de la Factura en XML (pesos o dolares)
	 * @value factura_descuento
	 * @param facturaDescuento
	 */
	public void setFacturaDescuento(double facturaDescuento) {
		this.facturaDescuento = facturaDescuento;
	}

	/** Subtotal de la Factura en XML (pesos o dolares)
	 * @value factura_subtotal
	 * @return
	 */
	public double getFacturaSubtotal() {
		return facturaSubtotal;
	}

	/** Subtotal de la Factura en XML (pesos o dolares)
	 * @value factura_subtotal
	 * @param facturaSubtotal
	 */
	public void setFacturaSubtotal(double facturaSubtotal) {
		this.facturaSubtotal = facturaSubtotal;
	}

	/** Impuestos de la Factura en XML (pesos o dolares)
	 * @value factura_impuestos
	 * @return
	 */
	public double getFacturaImpuestos() {
		return facturaImpuestos;
	}

	/** Impuestos de la Factura en XML (pesos o dolares)
	 * @value factura_impuestos
	 * @param facturaImpuestos
	 */
	public void setFacturaImpuestos(double facturaImpuestos) {
		this.facturaImpuestos = facturaImpuestos;
	}

	/** Retenciones de la Factura en XML (pesos o dolares)
	 * @value factura_retenciones
	 * @return
	 */
	public double getFacturaRetenciones() {
		return facturaRetenciones;
	}

	/** Retenciones de la Factura en XML (pesos o dolares)
	 * @value factura_retenciones
	 * @param facturaRetenciones
	 */
	public void setFacturaRetenciones(double facturaRetenciones) {
		this.facturaRetenciones = facturaRetenciones;
	}

	/** Total de la Factura en XML (pesos o dolares)
	 * @value factura_total
	 * @return
	 */
	public double getFacturaTotal() {
		return facturaTotal;
	}

	/**	Total de la Factura en XML (pesos o dolares)
	 * @value factura_total
	 * @param facturaTotal
	 */
	public void setFacturaTotal(double facturaTotal) {
		this.facturaTotal = facturaTotal;
	}

	// -------------------------------------------------------------------------------------
	
	/** Descuento de la Factura MXN, convertido a pesos si corresponde
	 * @value descuento
	 * @return 
	 */
	public double getDescuentoPesos() {
		return this.descuentoPesos;
	}
	
	/** Descuento de la Factura MXN, conversion a pesos si corresponde
	 * @value descuento
	 * @param descuentoPesos
	 */
	public void setDescuentoPesos(double descuentoPesos) {
		this.descuentoPesos = descuentoPesos;
	}

	/** Subtotal de la Factura MXN, convertido a pesos si corresponde
	 * @value subtotal
	 * @return descuento
	 */
	public double getSubtotalPesos() {
		return this.subtotalPesos;
	}
	
	/** Subtotal de la Factura MXN, conversion a pesos si corresponde
	 * @value subtotal
	 * @param subtotalPesos
	 */
	public void setSubtotalPesos(double subtotalPesos) {
		this.subtotalPesos = subtotalPesos;
	}

	/** Impuestos de la Factura MXN, conversion a pesos si corresponde
	 * @value impuestos
	 * @return
	 */
	public double getImpuestosPesos() {
		return impuestosPesos;
	}

	/** Impuestos de la Factura MXN, conversion a pesos si corresponde
	 * @value impuestos
	 * @param impuestosPesos
	 */
	public void setImpuestosPesos(double impuestosPesos) {
		this.impuestosPesos = impuestosPesos;
	}

	/** Retenciones de la Factura MXN, conversion a pesos si corresponde
	 * @value retenciones
	 * @return
	 */
	public double getRetencionesPesos() {
		return retencionesPesos;
	}

	/** Retenciones de la Factura MXN, conversion a pesos si corresponde
	 * @value retenciones
	 * @param retencionesPesos
	 */
	public void setRetencionesPesos(double retencionesPesos) {
		this.retencionesPesos = retencionesPesos;
	}

	/** Total de la Factura MXN, conversion a pesos si corresponde
	 * @value total
	 * @return
	 */
	public double getTotalPesos() {
		return this.totalPesos;
	}
	
	/** Total de la Factura MXN, convertido a pesos si corresponde
	 * @value total
	 * @param totalPesos
	 */
	public void setTotalPesos(double totalPesos) {
		this.totalPesos = totalPesos;
	}

	// -------------------------------------------------------------------------------------

	/** @value id_empresa */
	public long getIdEmpresa() {
		return idEmpresa;
	}

	/** @value id_empresa */
	public void setIdEmpresa(long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	/** O: Activo, 1: Eliminado
	 * @value estatus
	 * @return
	 */
	public int getEstatus() {
		return estatus;
	}

	/** O: Activo, 1: Eliminado
	 * @value estatus
	 * @param estatus
	 */
	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}

	/** @value ab */
	public long getCreadoPor() {
		return creadoPor;
	}

	/** @value ab */
	public void setCreadoPor(long creadoPor) {
		this.creadoPor = creadoPor;
	}

	/** @value ac */
	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	/** @value ac */
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	/** @value ad */
	public long getModificadoPor() {
		return modificadoPor;
	}

	/** @value ad */
	public void setModificadoPor(long modificadoPor) {
		this.modificadoPor = modificadoPor;
	}

	/** @value ae */
	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	/** @value ae */
	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	// -----------------------------------------------------------------
	// EXTENDIDOS
	// -----------------------------------------------------------------
	
	public String getFactura() {
		String val = ""; 
		String aux = "";
		
		if (this.expresionImpresa == null || "".equals(this.expresionImpresa.trim()))
			return "";

		aux = getFacturaSerie();
		if (aux != null && ! "".equals(aux))
			val = aux.trim();
		
		aux = getFacturaFolio();
		if (aux != null && ! "".equals(aux))
			val = (! "".equals(val.trim()) ? "-" : "") + aux.trim();
		
		return val;
	}
	
	public void setFactura(String value) {}
	
	public String getRazonSocialORfc() {
		if (this.expresionImpresa != null && ! "".equals(this.expresionImpresa.trim())) {
			if (this.facturaEmisor == null || "".equals(this.facturaEmisor.trim()))
				return getRfcEmisor();
			return this.facturaEmisor;
		}
			
		return "";
	}
	
	public void setRazonSocialORfc(String value) {}
	
	public String getRfcEmisor() {
		if (this.expresionImpresa != null && ! "".equals(this.expresionImpresa.trim()))
			return this.expresionImpresa.substring(this.expresionImpresa.indexOf("?re=") + 4, this.expresionImpresa.indexOf("&rr="));
		return "";
	}
	
	public void setRfcEmisor(String value) {}
	
	public String getRfcReceptor() {
		if (this.expresionImpresa != null && ! "".equals(this.expresionImpresa.trim()))
			return this.expresionImpresa.substring(this.expresionImpresa.indexOf("&rr=") + 4, this.expresionImpresa.indexOf("&tt="));
		return "";
	}
	
	public void setRfcReceptor(String value) {}
	
	public String getUuid() {
		if (this.expresionImpresa != null && ! "".equals(this.expresionImpresa.trim()))
			return this.expresionImpresa.substring(this.expresionImpresa.indexOf("&id=") + 4);
		return "";
	}
	
	public void setUuid(String value) {}

	public void setFacturaDescuento(String value) {
		if (value == null || "".equals(value.trim()))
			value = "0";
		setFacturaDescuento(Double.parseDouble(value));
	}

	public void setFacturaSubtotal(String value) {
		if (value == null || "".equals(value.trim()))
			value = "0";
		setFacturaSubtotal(Double.parseDouble(value));
	}

	public void setFacturaImpuestos(String value) {
		if (value == null || "".equals(value.trim()))
			value = "0";
		setFacturaImpuestos(Double.parseDouble(value));
	}

	public void setFacturaRetenciones(String value) {
		if (value == null || "".equals(value.trim()))
			value = "0";
		setFacturaRetenciones(Double.parseDouble(value));
	}

	public void setFacturaTotal(String value) {
		if (value == null || "".equals(value.trim()))
			value = "0";
		setFacturaTotal(Double.parseDouble(value));
	}

	// -----------------------------------------------------------------
	
	public void conversionPesos() {
		this.facturaTipoCambio 	= (this.facturaTipoCambio > 1 ? this.facturaTipoCambio : 1);
		this.descuentoPesos 	= this.facturaTipoCambio * this.facturaDescuento;
		this.subtotalPesos 		= this.facturaTipoCambio * this.facturaSubtotal;
		this.impuestosPesos 	= this.facturaTipoCambio * this.facturaImpuestos;
		this.retencionesPesos 	= this.facturaTipoCambio * this.facturaRetenciones;
		this.totalPesos 		= this.facturaTipoCambio * this.facturaTotal;
	}
}