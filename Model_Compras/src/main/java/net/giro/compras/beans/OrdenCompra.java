package net.giro.compras.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * orden_compra
 * @author javaz
 */
public class OrdenCompra implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private int tipo; // 1 - Inventario, 2 - Administrativo
	private Date fecha;
	private String folio;
	private Cotizacion idCotizacion; // puede mapearse
	private Long idObra;
	private String nombreObra;
	private long idSucursal;
	private Long idProveedor;
	private String nombreProveedor;
	private String tipoPersonaProveedor;
	private int consecutivoProveedor;
	private Long idContacto;
	private String nombreContacto;
	private Long idSolicita;
	private String nombreSolicita;
	private long idMoneda;
	private String moneda;
	private BigDecimal tipoCambio;
	private BigDecimal anticipo;
	private int plazo;
	private int tiempoEntrega;
	private String lugarEntrega;
	private double flete;
	private String referencia;
	private double porcentajeIva;
	private double subtotal;
	private double iva;
	private double retenciones;
	private double total;
	private int autorizado;
	private long idUsuarioAutorizo;
	private Date fechaAutorizacion;
	private long idEmpresa;
	private int base; // 0 - Cotizacion, 1 - Requisicion 
	private int sistema; // 0 - Normal, 1 - Sistema
	private int completa; // 0 - No, 1 - Si
	private int estatus; // 0 - Activa, 1 - Eliminada, 2 - Suministrada
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;

	public OrdenCompra() {
		this.anticipo = BigDecimal.ZERO;
		this.tipoCambio = BigDecimal.ZERO;
		this.fecha = Calendar.getInstance().getTime();
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
		this.fechaAutorizacion = Calendar.getInstance().getTime();
		this.folio = "";
		this.lugarEntrega = "";
		this.nombreObra = "";
		this.nombreProveedor = "";
		this.nombreSolicita = "";
		this.nombreContacto = "";
		this.tipoPersonaProveedor = "";
		this.referencia = "";
		this.moneda = "";
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

	/**
	 * 1 - Inventario 2 - Administrativo
	 * @return
	 */
	public int getTipo() {
		return tipo;
	}

	/**
	 * 1 - Inventario 2 - Administrativo
	 * @param tipo
	 */
	public void setTipo(int tipo) {
		this.tipo = tipo;
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

	public Cotizacion getIdCotizacion() {
		return idCotizacion;
	}

	public void setIdCotizacion(Cotizacion idCotizacion) {
		this.idCotizacion = idCotizacion;
	}

	public Long getIdObra() {
		return idObra;
	}

	public void setIdObra(Long idObra) {
		this.idObra = idObra;
	}

	public String getNombreObra() {
		return nombreObra;
	}

	public void setNombreObra(String nombreObra) {
		this.nombreObra = nombreObra;
	}

	public long getIdSucursal() {
		return idSucursal;
	}

	public void setIdSucursal(long idSucursal) {
		this.idSucursal = idSucursal;
	}

	public Long getIdProveedor() {
		return idProveedor;
	}

	public void setIdProveedor(Long idProveedor) {
		this.idProveedor = idProveedor;
	}

	public String getNombreProveedor() {
		return nombreProveedor;
	}

	public void setNombreProveedor(String nombreProveedor) {
		this.nombreProveedor = nombreProveedor;
	}

	public String getTipoPersonaProveedor() {
		return tipoPersonaProveedor;
	}

	public void setTipoPersonaProveedor(String tipoPersonaProveedor) {
		this.tipoPersonaProveedor = tipoPersonaProveedor;
	}

	public int getConsecutivoProveedor() {
		return consecutivoProveedor;
	}

	public void setConsecutivoProveedor(int consecutivoProveedor) {
		this.consecutivoProveedor = consecutivoProveedor;
	}

	public Long getIdContacto() {
		return idContacto;
	}

	public void setIdContacto(Long idContacto) {
		this.idContacto = idContacto;
	}

	public String getNombreContacto() {
		return nombreContacto;
	}

	public void setNombreContacto(String nombreContacto) {
		this.nombreContacto = nombreContacto;
	}

	public Long getIdSolicita() {
		return idSolicita;
	}

	public void setIdSolicita(Long idSolicita) {
		this.idSolicita = idSolicita;
	}

	public String getNombreSolicita() {
		return nombreSolicita;
	}

	public void setNombreSolicita(String nombreSolicita) {
		this.nombreSolicita = nombreSolicita;
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
		this.moneda = moneda;
	}

	public BigDecimal getTipoCambio() {
		return tipoCambio;
	}

	public void setTipoCambio(BigDecimal tipoCambio) {
		this.tipoCambio = tipoCambio;
	}

	public BigDecimal getAnticipo() {
		return anticipo;
	}

	public void setAnticipo(BigDecimal anticipo) {
		this.anticipo = anticipo;
	}

	public int getPlazo() {
		return plazo;
	}

	public void setPlazo(int plazo) {
		this.plazo = plazo;
	}

	public int getTiempoEntrega() {
		return tiempoEntrega;
	}

	public void setTiempoEntrega(int tiempoEntrega) {
		this.tiempoEntrega = tiempoEntrega;
	}

	public String getLugarEntrega() {
		return lugarEntrega;
	}

	public void setLugarEntrega(String lugarEntrega) {
		this.lugarEntrega = lugarEntrega;
	}

	public double getFlete() {
		return flete;
	}

	public void setFlete(double flete) {
		this.flete = flete;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public double getPorcentajeIva() {
		return porcentajeIva;
	}

	public void setPorcentajeIva(double porcentajeIva) {
		this.porcentajeIva = porcentajeIva;
	}

	public double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}

	public double getIva() {
		return iva;
	}

	public void setIva(double iva) {
		this.iva = iva;
	}

	public double getRetenciones() {
		return retenciones;
	}

	public void setRetenciones(double retenciones) {
		this.retenciones = retenciones;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public int getAutorizado() {
		return autorizado;
	}

	public void setAutorizado(int autorizado) {
		this.autorizado = autorizado;
	}

	public long getIdUsuarioAutorizo() {
		return idUsuarioAutorizo;
	}

	public void setIdUsuarioAutorizo(long idUsuarioAutorizo) {
		this.idUsuarioAutorizo = idUsuarioAutorizo;
	}

	public Date getFechaAutorizacion() {
		return fechaAutorizacion;
	}

	public void setFechaAutorizacion(Date fechaAutorizacion) {
		this.fechaAutorizacion = fechaAutorizacion;
	}

	public long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	/**
	 * 0 - Cotizacion, 1 - Requisicion
	 * @return
	 */
	public int getBase() {
		return base;
	}

	/**
	 * 0 - Cotizacion, 1 - Requisicion
	 * @param base
	 */
	public void setBase(int base) {
		this.base = base;
	}

	/**
	 * 0 - Normal, 1 - Sistema
	 * @return
	 */
	public int getSistema() {
		return sistema;
	}

	/**
	 * 0 - Normal, 1 - Sistema
	 * @param sistema
	 */
	public void setSistema(int sistema) {
		this.sistema = sistema;
	}

	/**
	 * COMPLETA: 0 - No, 1 - Si
	 * @return
	 */
	public int getCompleta() {
		return completa;
	}

	/**
	 * COMPLETA: 0 - No, 1 - Si
	 * @param completa
	 */
	public void setCompleta(int completa) {
		this.completa = completa;
	}

	/**
	 * ESTATUS: 0 - Activa, 1 - Eliminada, 2 - Suministrada
	 * @return
	 */
	public int getEstatus() {
		return estatus;
	}

	/**
	 * ESTATUS: 0 - Activa, 1 - Eliminada, 2 - Suministrada
	 * @param estatus
	 */
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
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.2 | 2017-07-15 | Javier Tirado 	| Añado tipo al entity
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 *  2.1 | 2017-01-13 | Javier Tirado 	| Añado propiedad tipoPersonaProveedor
 */