package net.giro.cxp.beans;

import java.util.Calendar;
import java.util.Date;

import net.giro.cxp.beans.PagosGastos;

/**
 * pagos_gastos_det
 * @author javaz
 *
 */
public class PagosGastosDet implements java.io.Serializable {
	private static final long serialVersionUID = -8788796846813172225L;
	
	private Long id;
	private PagosGastos idPagosGastos;
	private Long idProveedor;
	private String tipoPersonaProveedor; // P:Persona, N:Negocio
	private Date fecha;
	private Double subtotal;
	private Long idConcepto;
	private String referencia;
	private Double totalImpuestos;
	private Double totalRetenciones;
	private String observaciones;
	private String estatus;
	private Short facturado;
	private Long idXml;
	private Long idObra; 
	private Long idOrdenCompra;
	private String facturaRazonSocial;
	private int esCredito;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	

	public PagosGastosDet() {
		this.fecha = Calendar.getInstance().getTime();
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
		this.subtotal = 0D;
		this.totalImpuestos = 0D;
		this.totalRetenciones = 0D;
		this.referencia = "";
		this.facturaRazonSocial = "";
		this.observaciones = "";
		this.estatus = "";
	}

	public PagosGastosDet(Long id) {
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

	public PagosGastos getIdPagosGastos() {
		return idPagosGastos;
	}

	public void setIdPagosGastos(PagosGastos idPagosGastos) {
		this.idPagosGastos = idPagosGastos;
	}

	public Long getIdProveedor() {
		return idProveedor;
	}

	public void setIdProveedor(Long idProveedor) {
		this.idProveedor = idProveedor;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(Double subtotal) {
		this.subtotal = subtotal;
	}

	public Long getIdConcepto() {
		return idConcepto;
	}

	public void setIdConcepto(Long idConcepto) {
		this.idConcepto = idConcepto;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
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

	public Long getIdObra() {
		return idObra;
	}

	public void setIdObra(Long idObra) {
		this.idObra = idObra;
	}

	public Long getIdOrdenCompra() {
		return idOrdenCompra;
	}

	public void setIdOrdenCompra(Long idOrdenCompra) {
		this.idOrdenCompra = idOrdenCompra;
	}

	public String getFacturaRazonSocial() {
		return facturaRazonSocial;
	}

	public void setFacturaRazonSocial(String facturaRazonSocial) {
		this.facturaRazonSocial = facturaRazonSocial;
	}

	public int getEsCredito() {
		return esCredito;
	}

	public void setEsCredito(int esCredito) {
		this.esCredito = esCredito;
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

	/**
	 * P:Persona, N:Negocio
	 * @return
	 */
	public String getTipoPersonaProveedor() {
		return tipoPersonaProveedor;
	}
	/**
	 * P:Persona, N:Negocio
	 * @return
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
 * 	2.2 | 2017-08-05 | Javier Tirado 	| Añado propiedades idObra y idOrdenCompra
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */