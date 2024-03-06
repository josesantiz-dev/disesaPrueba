package net.giro.compras.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * orden_compra
 * @author javaz
 *
 */
public class OrdenCompra implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String folio;
	private Date fecha;
	private Long idCotizacion;
	private Long idObra;
	private Long idProveedor;
	private Long idSolicita;
	private Long idContacto;
	private Long idMoneda;
	private BigDecimal anticipo;
	private BigDecimal tipoCambio;
	private int plazo;
	private int tiempoEntrega;
	private String lugarEntrega;
	private double subtotal;
	private double iva;
	private double retenciones;
	private double total;
	private String nombreObra;
	private String nombreProveedor;
	private String nombreSolicita;
	private String nombreContacto;
	private int consecutivoProveedor;
	private int completa;
	private int autorizado;
	private long idUsuarioAutorizo;
	private Date fechaAutorizacion;
	private String tipoPersonaProveedor;
	private int tipo; // 1 - Inventario 2 - Administrativo
	private double flete;
	private String referencia;
	private int estatus; // 0:Activa, 1:Eliminada, 2:Suministrada
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	private int base; // 0:Cotizacion, 1:Requisicion
	private int sistema;
	private Long idEmpresa;

	
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
	}
	
	public OrdenCompra(Long id) {
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

	public Long getIdCotizacion() {
		return idCotizacion;
	}

	public void setIdCotizacion(Long idCotizacion) {
		this.idCotizacion = idCotizacion;
	}

	public Long getIdObra() {
		return idObra;
	}

	public void setIdObra(Long idObra) {
		this.idObra = idObra;
	}

	public Long getIdProveedor() {
		return idProveedor;
	}

	public void setIdProveedor(Long idProveedor) {
		this.idProveedor = idProveedor;
	}

	public Long getIdSolicita() {
		return idSolicita;
	}

	public void setIdSolicita(Long idSolicita) {
		this.idSolicita = idSolicita;
	}

	public Long getIdContacto() {
		return idContacto;
	}

	public void setIdContacto(Long idContacto) {
		this.idContacto = idContacto;
	}

	public BigDecimal getAnticipo() {
		return anticipo;
	}

	public void setAnticipo(BigDecimal anticipo) {
		this.anticipo = anticipo;
	}

	public Long getIdMoneda() {
		return idMoneda;
	}

	public void setIdMoneda(Long idMoneda) {
		this.idMoneda = idMoneda;
	}

	public BigDecimal getTipoCambio() {
		return tipoCambio;
	}

	public void setTipoCambio(BigDecimal tipoCambio) {
		this.tipoCambio = tipoCambio;
	}

	public int getPlazo() {
		return plazo;
	}

	public void setPlazo(int plazo) {
		this.plazo = plazo;
	}

	public String getLugarEntrega() {
		return lugarEntrega;
	}

	public void setLugarEntrega(String lugarEntrega) {
		this.lugarEntrega = lugarEntrega;
	}

	public int getTiempoEntrega() {
		return tiempoEntrega;
	}

	public void setTiempoEntrega(int tiempoEntrega) {
		this.tiempoEntrega = tiempoEntrega;
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

	/**
	 * 0:Activa, 1:Eliminada, 2:Suministrada
	 * @return
	 */
	public int getEstatus() {
		return estatus;
	}

	/**
	 * 0:Activa, 1:Eliminada, 2:Suministrada
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

	public String getNombreObra() {
		return nombreObra;
	}

	public void setNombreObra(String nombreObra) {
		this.nombreObra = nombreObra;
	}

	public String getNombreProveedor() {
		return nombreProveedor;
	}

	public void setNombreProveedor(String nombreProveedor) {
		this.nombreProveedor = nombreProveedor;
	}

	public String getNombreSolicita() {
		return nombreSolicita;
	}

	public void setNombreSolicita(String nombreSolicita) {
		this.nombreSolicita = nombreSolicita;
	}

	public String getNombreContacto() {
		return nombreContacto;
	}

	public void setNombreContacto(String nombreContacto) {
		this.nombreContacto = nombreContacto;
	}

	public int getCompleta() {
		return completa;
	}

	public void setCompleta(int completa) {
		this.completa = completa;
	}

	public int getConsecutivoProveedor() {
		return consecutivoProveedor;
	}

	public void setConsecutivoProveedor(int consecutivoProveedor) {
		this.consecutivoProveedor = consecutivoProveedor;
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

	public String getTipoPersonaProveedor() {
		return tipoPersonaProveedor;
	}

	public void setTipoPersonaProveedor(String tipoPersonaProveedor) {
		this.tipoPersonaProveedor = tipoPersonaProveedor;
	}

	/**
	 * Tipo: 1 - Inventario 2 - Administrativo
	 * @return
	 */
	public int getTipo() {
		return tipo;
	}

	/**
	 * Tipo: 1 - Inventario 2 - Administrativo
	 * @return
	 */
	public void setTipo(int tipo) {
		this.tipo = tipo;
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

	/**
	 * 0:Cotizacion, 1:Requisicion
	 * @return
	 */
	public int getBase() {
		return base;
	}

	/**
	 * 0:Cotizacion, 1:Requisicion
	 * @param base
	 */
	public void setBase(int base) {
		this.base = base;
	}

	public int getSistema() {
		return sistema;
	}

	public void setSistema(int sistema) {
		this.sistema = sistema;
	}

	public Long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
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