package net.giro.cxp.beans;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import net.giro.adp.beans.Obra;
import net.giro.compras.beans.OrdenCompra;
import net.giro.plataforma.beans.ConValores;

/**
 * pagos_gastos_det
 * @author javaz
 *
 */
public class PagosGastosDetExt implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private PagosGastosExt idPagosGastos;
	private Date fecha;
	private ConValores idConcepto;
	private String descripcionConcepto;
	private Obra idObra; 
	private String nombreObra;  // *
	private OrdenCompra idOrdenCompra;
	private String folioOrdenCompra;  // *
	private boolean esCredito;
	private boolean manoObra;
	private boolean subcontrato;
	// ---------------------------------------------------------------------
	private int facturado;
	private Long idCfdi; // ComprobacionFactura
	private int estatusCfdi; // 0 - Sin Comprobar, 1 - Activo, 2 - Cancelado
	private String referencia;
	private PersonaExt idProveedor;
	private String tipoPersonaProveedor; // P:Persona, N:Negocio
	private String nombreProveedor;
	private String rfcProveedor; 
	private String facturaConcepto; // * descripcion de concepto en CFDI
	private Double importeConcepto; // * importe de concepto en CFDI
	// ---------------------------------------------------------------------
	private Double subtotal;
	private Double totalImpuestos;
	private Double totalRetenciones;
	private Double total;  // *
	private String observaciones;
	private String estatus; // [VACIO]: Activo, E:Enviado
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	// -------------------------------------
	private String expresionImpresa;
	private String facturaTipo; 
	private String facturaUuid; 
	private String facturaSerie; 
	private String facturaFolio;
	private String facturaRfc; 
	private String facturaMoneda; 
	private double facturaTipoCambio; 
	private double facturaTotal; 
	// -------------------------------------
	private String uniqueValue;
	// -------------------------------------
	private boolean seleccionado;

	public PagosGastosDetExt() {
		this.fecha = Calendar.getInstance().getTime();
		this.importeConcepto = 0D;
		this.subtotal = 0D;
		this.totalImpuestos = 0D;
		this.totalRetenciones = 0D;
		this.total = 0D;
		this.descripcionConcepto = "";
		this.nombreObra = "";
		this.folioOrdenCompra = "";
		this.referencia = "";
		this.tipoPersonaProveedor = "";
		this.nombreProveedor = "";
		this.rfcProveedor = "";
		this.facturaConcepto = "";
		this.observaciones = "";
		this.estatus = "";
		this.expresionImpresa = "";
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
		// -------------------------------------
		this.facturaTipo = "";
		this.facturaUuid = "";
		this.facturaSerie = "";
		this.facturaFolio = "";
		this.facturaRfc = "";
		this.facturaMoneda = "";
		// -------------------------------------
		this.seleccionado = false;
		// -------------------------------------
		//this.generarUniqueValue();
	}
	
	public Long getId() {
		if (id == null)
			id = 0L;
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PagosGastosExt getIdPagosGastos() {
		return idPagosGastos;
	}

	public void setIdPagosGastos(PagosGastosExt idPagosGastos) {
		this.idPagosGastos = idPagosGastos;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public ConValores getIdConcepto() {
		return idConcepto;
	}

	public void setIdConcepto(ConValores idConcepto) {
		this.idConcepto = idConcepto;
	}

	public String getDescripcionConcepto() {
		return descripcionConcepto;
	}

	public void setDescripcionConcepto(String descripcionConcepto) {
		this.descripcionConcepto = descripcionConcepto;
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

	public OrdenCompra getIdOrdenCompra() {
		return idOrdenCompra;
	}

	public void setIdOrdenCompra(OrdenCompra idOrdenCompra) {
		this.idOrdenCompra = idOrdenCompra;
	}

	public String getFolioOrdenCompra() {
		return folioOrdenCompra;
	}

	public void setFolioOrdenCompra(String folioOrdenCompra) {
		this.folioOrdenCompra = folioOrdenCompra;
	}

	public boolean getEsCredito() {
		return esCredito;
	}

	public void setEsCredito(boolean esCredito) {
		this.esCredito = esCredito;
	}

	public boolean getManoObra() {
		return manoObra;
	}

	public void setManoObra(boolean manoObra) {
		this.manoObra = manoObra;
	}

	public boolean getSubcontrato() {
		return subcontrato;
	}

	public void setSubcontrato(boolean subcontrato) {
		this.subcontrato = subcontrato;
	}

	public int getFacturado() {
		return facturado;
	}

	public void setFacturado(int facturado) {
		this.facturado = facturado;
	}

	public Long getIdCfdi() {
		return idCfdi;
	}

	public void setIdCfdi(Long idCfdi) {
		this.idCfdi = idCfdi;
	}
	
	/**
	 * Estatus CFDI: 0 - Sin Comprobar, 1 - Activo, 2 - Cancelado
	 * @return
	 */
	public int getEstatusCfdi() {
		return estatusCfdi;
	}
	
	/**
	 * Estatus CFDI: 0 - Sin Comprobar, 1 - Activo, 2 - Cancelado
	 * @param estatusCfdi
	 */
	public void setEstatusCfdi(int estatusCfdi) {
		this.estatusCfdi = estatusCfdi;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public PersonaExt getIdProveedor() {
		return idProveedor;
	}

	public void setIdProveedor(PersonaExt idProveedor) {
		this.idProveedor = idProveedor;
	}

	public String getTipoPersonaProveedor() {
		return tipoPersonaProveedor;
	}

	public void setTipoPersonaProveedor(String tipoPersonaProveedor) {
		this.tipoPersonaProveedor = tipoPersonaProveedor;
	}

	public String getNombreProveedor() {
		return nombreProveedor;
	}

	public void setNombreProveedor(String proveedor) {
		this.nombreProveedor = proveedor;
	}
	
	public String getRfcProveedor() {
		return rfcProveedor;
	}
	
	public void setRfcProveedor(String rfcProveedor) {
		this.rfcProveedor = rfcProveedor;
	}

	public String getFacturaConcepto() {
		return facturaConcepto;
	}

	public void setFacturaConcepto(String facturaConcepto) {
		this.facturaConcepto = facturaConcepto;
	}

	public Double getImporteConcepto() {
		return importeConcepto;
	}

	public void setImporteConcepto(Double importeConcepto) {
		this.importeConcepto = importeConcepto;
	}

	public Double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(Double subtotal) {
		this.subtotal = subtotal;
	}

	public Double getTotalImpuestos() {
		return totalImpuestos;
	}

	public void setTotalImpuestos(Double totalImpuestos) {
		this.totalImpuestos = totalImpuestos;
	}

	public Double getTotalRetenciones() {
		return totalRetenciones;
	}

	public void setTotalRetenciones(Double totalRetenciones) {
		this.totalRetenciones = totalRetenciones;
	}

	public Double getTotal() {
		if (this.total == null || this.total <= 0D) {
			if (this.subtotal == null)
				this.subtotal = 0D;
			if (this.totalImpuestos == null)
				this.totalImpuestos = 0D;
			if (this.totalRetenciones == null)
				this.totalRetenciones = 0D;
			this.total = this.subtotal + (this.totalImpuestos - this.totalRetenciones);
		}
		
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
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

	// ------------------------------------------------------------------
	// CFDI
	// ------------------------------------------------------------------
	
	public String getExpresionImpresa() {
		return (this.expresionImpresa != null ? this.expresionImpresa : "");
	}
	
	public void setExpresionImpresa(String expresionImpresa) {
		this.expresionImpresa = (expresionImpresa != null ? expresionImpresa : "");;
	}
	
	public String getFacturaTipo() {
		return facturaTipo;
	}

	public void setFacturaTipo(String facturaTipo) {
		this.facturaTipo = facturaTipo;
	}

	public String getFacturaUuid() {
		return facturaUuid;
	}

	public void setFacturaUuid(String facturaUuid) {
		this.facturaUuid = facturaUuid;
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

	public String getFacturaRfc() {
		if (this.facturado == 0 && this.idProveedor != null && this.idProveedor.getId() > 0L)
			return this.idProveedor.getRfc();
		return facturaRfc;
	}

	public void setFacturaRfc(String facturaRfc) {
		this.facturaRfc = facturaRfc;
	}

	public String getFacturaMoneda() {
		return facturaMoneda;
	}

	public void setFacturaMoneda(String facturaMoneda) {
		this.facturaMoneda = facturaMoneda;
	}

	public double getFacturaTipoCambio() {
		return facturaTipoCambio;
	}

	public void setFacturaTipoCambio(double facturaTipoCambio) {
		this.facturaTipoCambio = facturaTipoCambio;
	}

	public double getFacturaTotal() {
		return facturaTotal;
	}

	public void setFacturaTotal(double facturaTotal) {
		this.facturaTotal = facturaTotal;
	}

	public boolean isSeleccionado() {
		return seleccionado;
	}

	public void setSeleccionado(boolean seleccionado) {
		this.seleccionado = seleccionado;
	}

	public String getUniqueValue() {
		return uniqueValue;
	}

	public void setUniqueValue(String uniqueValue) {
		this.uniqueValue = uniqueValue;
	}
	
	// ------------------------------------------------------------------
	// EXTENDIDOS
	// ------------------------------------------------------------------

	public String getConcepto() {
		if (this.idConcepto != null && this.idConcepto.getId() > 0L)
			return this.idConcepto.getId() + " - " + this.idConcepto.getDescripcion();
		return "";
	}
	
	public void setConcepto(String value) {}
	
	public String getObra() {
		if (this.idObra != null && this.idObra.getId() != null && this.idObra.getId() > 0L)
			return this.idObra.getId() + " - " + this.idObra.getNombre();
		return "";
	}
	
	public void setObra(String value) {}
	
	public String getOrdenCompra() {
		if (this.idOrdenCompra != null && this.idOrdenCompra.getId() != null && this.idOrdenCompra.getId() > 0L)
			return this.idOrdenCompra.getId() + " - " + this.idOrdenCompra.getFolio() + " (" + (new SimpleDateFormat("dd-MM-yyyy")).format(this.idOrdenCompra.getFecha()) + ")";
		return "";
	}
	
	public void setOrdenCompra(String value) {}
	
	public double getImpuestos() {
		if (this.totalImpuestos == null)
			this.totalImpuestos = 0D;
		if (this.totalRetenciones == null)
			this.totalRetenciones = 0D;
		return this.totalImpuestos - this.totalRetenciones;
	}
	
	public void setImpuestos(double value) {}
	
	public String getProveedor() {
		if (this.idProveedor != null && this.idProveedor.getId() > 0L)
			return this.idProveedor.getId() + " - " + this.idProveedor.getNombre();
		if (this.nombreProveedor != null && ! "".equals(this.nombreProveedor.trim()))
			return this.nombreProveedor;
		return "";
	}
	
	public void setProveedor(String value) {}
	
	public String getImporteConceptoNumber() {
		if (this.importeConcepto == null)
			this.importeConcepto = 0D;
		return (new DecimalFormat("$ ###,###,##0.00")).format(this.importeConcepto);
	}
	
	public void setImporteConceptoNumber(String value) {}
	
	public String getImporteConceptoFormateado() {
		if (this.importeConcepto == null)
			this.importeConcepto = 0D;
		return (new DecimalFormat("#0.00")).format(this.importeConcepto);
	}
	
	public void setImporteConceptoFormateado(String value) {}

	public String getFacturaTotalFormateado() {
		return (new DecimalFormat("$ ###,###,##0.00")).format(this.facturaTotal);
	}
	
	public void setFacturaTotalFormateado(String value) {}
	
	public String getFacturaMonedaTipoCambio() {
		if (this.facturaMoneda != null && ! "".equals(this.facturaMoneda.trim()))
			return this.facturaMoneda + ((this.facturaTipoCambio > 1) ? " TC: " + (new DecimalFormat("0.0000")).format(this.facturaTipoCambio) : "");
		return "MXN";
	}
	
	public void setFacturaMonedaTipoCambio(String value) {}

	// ------------------------------------------------------------------
	// EXTENDIDOS
	// ------------------------------------------------------------------
	
	public boolean compararEmpresionImpresa(String expresionImpresa) {
		if (expresionImpresa != null && ! "".equals(expresionImpresa.trim()) && ! "".equals(getExpresionImpresa().trim()))
			return (getExpresionImpresa().equals(expresionImpresa.trim()));
		return false;
	}
	
	public void generarUniqueValue() {
		Calendar fecha = null;
		String value = "";

		if (this.uniqueValue != null && ! "".equals(this.uniqueValue.trim()))
			return;
		
		fecha = Calendar.getInstance();
		value += ((fecha.get(Calendar.MONTH) < 10 ? "0" : "") + (fecha.get(Calendar.MONTH) + 1));
		value += ((fecha.get(Calendar.DAY_OF_MONTH) < 10 ? "0" : "") + fecha.get(Calendar.DAY_OF_MONTH));
		value += ((fecha.get(Calendar.HOUR) < 10 ? "0" : "") + fecha.get(Calendar.HOUR));
		value += ((fecha.get(Calendar.MINUTE) < 10 ? "0" : "") + fecha.get(Calendar.MINUTE));
		value += ((fecha.get(Calendar.SECOND) < 10 ? "0" : "") + fecha.get(Calendar.SECOND));
	
		this.uniqueValue = this.generateHash(value);
	}
	
	private String generateHash(String value) {
		java.security.MessageDigest md = null;
		StringBuffer sb = null;
		byte[] array = null;
		
		try {
            md = java.security.MessageDigest.getInstance("MD5");
            array = md.digest(value.getBytes());
            sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) 
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        }
		
		return null;
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */