package net.giro.cxp.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import net.giro.adp.beans.Obra;
import net.giro.compras.beans.OrdenCompra;
import net.giro.plataforma.beans.ConValores;

public class PagosGastosDetExt implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private PagosGastosExt idPagosGastos;
	private PersonaExt idProveedor;
	private String tipoPersonaProveedor; // P:Persona, N:Negocio
	private ConValores idConcepto;
	private String referencia;
	private Date fecha;
	private Double subtotal;
	private String observaciones;
	private Double totalImpuestos;
	private Double totalRetenciones;
	private String estatus;
	private Short facturado;
	private Long idXml;
	private Obra idObra; 
	private OrdenCompra idOrdenCompra;
	private boolean esCredito;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	private String facturaUuid;
	private String facturaSerie;
	private String facturaFolio;
	private String facturaRfc;
	private String facturaRazonSocial;
	private String facturaTotal;


	/** default constructor */
	public PagosGastosDetExt() {
		this.fecha = Calendar.getInstance().getTime();
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
		this.subtotal = 0D;
		this.totalImpuestos = 0D;
		this.totalRetenciones = 0D;
		this.referencia = "";
		this.observaciones = "";
		this.estatus = "";
		this.facturaUuid = "";
		this.facturaSerie = "";
		this.facturaFolio = "";
		this.facturaRfc = "";
		this.facturaRazonSocial = "";
		this.facturaTotal = "";
	}

	/** minimal constructor */
	public PagosGastosDetExt(Long id) {
		this();
		this.id = id;
	}

	
	// Property accessors
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PagosGastosExt getIdPagosGastos() {
		return this.idPagosGastos;
	}

	public void setIdPagosGastos(PagosGastosExt idPagosGastos) {
		this.idPagosGastos = idPagosGastos;
	}

	public PersonaExt getIdProveedor() {
		return this.idProveedor;
	}

	public void setIdProveedor(PersonaExt idProveedor) {
		this.idProveedor = idProveedor;
	}

	public String getReferencia() {
		return this.referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Double getSubtotal() {
		if (this.subtotal == null) this.subtotal = 0D;
		return this.subtotal;
	}

	public void setSubtotal(Double subtotal) {
		this.subtotal = subtotal;
	}

	public long getCreadoPor() {
		return this.creadoPor;
	}

	public void setCreadoPor(long creadoPor) {
		this.creadoPor = creadoPor;
	}

	public Date getFechaCreacion() {
		return this.fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	
	public long getModificadoPor() {
		return this.modificadoPor;
	}

	public void setModificadoPor(long modificadoPor) {
		this.modificadoPor = modificadoPor;
	}

	public Date getFechaModificacion() {
		return this.fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public String getObservaciones() {
		return this.observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public ConValores getIdConcepto() {
		return this.idConcepto;
	}

	public void setIdConcepto(ConValores idConcepto) {
		this.idConcepto = idConcepto;
	}

	public Double getTotalImpuestos() {
		if (this.totalImpuestos == null) this.totalImpuestos = 0D;
		return this.totalImpuestos;
	}

	public void setTotalImpuestos(Double totalImpuestos) {
		this.totalImpuestos =totalImpuestos;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}
	
	public Double getTotalRetenciones() {
		if (this.totalRetenciones == null) this.totalRetenciones = 0D;
		return totalRetenciones;
	}

	public void setTotalRetenciones(Double totalRetenciones) {
		this.totalRetenciones = totalRetenciones;
	}

	public Short getFacturado() {
		return facturado;
	}

	public void setFacturado(Short facturado) {
		this.facturado = facturado;
	}

	public Long getIdXml() {
		return idXml;
	}

	public void setIdXml(Long idXml) {
		this.idXml = idXml;
	}

	public Obra getIdObra() {
		return idObra;
	}

	public void setIdObra(Obra idObra) {
		this.idObra = idObra;
	}

	public OrdenCompra getIdOrdenCompra() {
		return idOrdenCompra;
	}

	public void setIdOrdenCompra(OrdenCompra idOrdenCompra) {
		this.idOrdenCompra = idOrdenCompra;
	}

	public boolean getEsCredito() {
		return esCredito;
	}

	public void setEsCredito(boolean esCredito) {
		this.esCredito = esCredito;
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
		return facturaRfc;
	}

	public void setFacturaRfc(String facturaRfc) {
		this.facturaRfc = facturaRfc;
	}

	public String getFacturaRazonSocial() {
		return facturaRazonSocial;
	}

	public void setFacturaRazonSocial(String facturaRazonSocial) {
		this.facturaRazonSocial = facturaRazonSocial;
	}

	public String getFacturaTotal() {
		return facturaTotal;
	}

	public void setFacturaTotal(String facturaTotal) {
		this.facturaTotal = facturaTotal;
	}

	/**
	 *  P:Persona, N:Negocio
	 * @return
	 */
	public String getTipoPersonaProveedor() {
		return tipoPersonaProveedor;
	}

	/**
	 *  P:Persona, N:Negocio
	 * @param tipoPersonaProveedor
	 */
	public void setTipoPersonaProveedor(String tipoPersonaProveedor) {
		this.tipoPersonaProveedor = tipoPersonaProveedor;
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */