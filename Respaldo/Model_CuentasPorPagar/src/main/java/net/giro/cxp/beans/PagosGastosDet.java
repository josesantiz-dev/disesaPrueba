package net.giro.cxp.beans;

import java.util.Calendar;
import java.util.Date;

import net.giro.cxp.beans.PagosGastos;

public class PagosGastosDet implements java.io.Serializable {
	private static final long serialVersionUID = -8788796846813172225L;
	
	private Long id;
	private PagosGastos idPagosGastos;
	private Long idProveedor;
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
		return this.idPagosGastos;
	}

	public void setIdPagosGastos(PagosGastos idPagosGastos) {
		this.idPagosGastos = idPagosGastos;
	}

	public Long getIdProveedor() {
		return this.idProveedor;
	}

	public void setIdProveedor(Long idProveedor) {
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

	public Long getIdConcepto() {
		return this.idConcepto;
	}

	public void setIdConcepto(Long idConcepto) {
		this.idConcepto = idConcepto;
	}

	public Double getTotalImpuestos() {
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

	public int getEsCredito() {
		return this.esCredito;
	}

	public void setEsCredito(int esCredito) {
		this.esCredito = esCredito;
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