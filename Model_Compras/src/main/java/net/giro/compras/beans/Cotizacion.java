package net.giro.compras.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * cotizacion
 * @author javaz
 *
 */
public class Cotizacion implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private int tipo; // 1 - Inventario 2 - Administrativo
	private Date fecha;
	private String folio;
	private Long idRequisicion;
	private Long idObra;
	private String nombreObra;
	private long idSucursal;
	private Long idProveedor;
	private String nombreProveedor;
	private String rfcProveedor;
	private String tipoPersonaProveedor;
	private int consecutivoProveedor;
	private Long idContacto;
	private String nombreContacto;
	private Long idComprador;
	private String nombreComprador;
	private double flete;
	private double margen;
	private int tiempoEntrega;
	private double porcentajeIva;
	private double subtotal;
	private double iva;
	private double total;
	private long idMoneda;
	private String moneda;
	private double tipoCambio;
	private long idEmpresa;
	private int sistema; // 0 - Normal, 1 - Sistema
	private int cerrada; // // 0 - Activa, 1 - Cerrada
	private int estatus; // 0 - Activa, 1 - Eliminada, 2 - Suministrada
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	public Cotizacion() {
		this.fecha = Calendar.getInstance().getTime();
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
		this.folio = "";
		this.tipoPersonaProveedor = "";
		this.nombreObra = "";
		this.nombreProveedor = "";
		this.nombreContacto = "";
		this.nombreComprador = "";
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

	public Long getIdRequisicion() {
		return idRequisicion;
	}

	public void setIdRequisicion(Long idRequisicion) {
		this.idRequisicion = idRequisicion;
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

	public String getRfcProveedor() {
		return rfcProveedor;
	}

	public void setRfcProveedor(String rfcProveedor) {
		this.rfcProveedor = rfcProveedor;
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

	public Long getIdComprador() {
		return idComprador;
	}

	public void setIdComprador(Long idComprador) {
		this.idComprador = idComprador;
	}

	public String getNombreComprador() {
		return nombreComprador;
	}

	public void setNombreComprador(String nombreComprador) {
		this.nombreComprador = nombreComprador;
	}

	public double getFlete() {
		return flete;
	}

	public void setFlete(double flete) {
		this.flete = flete;
	}

	public double getMargen() {
		return margen;
	}

	public void setMargen(double margen) {
		this.margen = margen;
	}

	public int getTiempoEntrega() {
		return tiempoEntrega;
	}

	public void setTiempoEntrega(int tiempoEntrega) {
		this.tiempoEntrega = tiempoEntrega;
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

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
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

	public double getTipoCambio() {
		return tipoCambio;
	}

	public void setTipoCambio(double tipoCambio) {
		this.tipoCambio = tipoCambio;
	}

	public long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(long idEmpresa) {
		this.idEmpresa = idEmpresa;
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
	 */
	public void setSistema(int sistema) {
		this.sistema = sistema;
	}

	/**
	 * CERRADA: 0 - Activa/Abierta, 1 - Cerrada
	 * @return
	 */
	public int getCerrada() {
		return cerrada;
	}

	/**
	 * CERRADA: 0 - Activa/Abierta, 1 - Cerrada
	 * @param usoParcial
	 */
	public void setCerrada(int cerrada) {
		this.cerrada = cerrada;
	}

	/**
	 * 0 - Activa, 1 - Eliminada, 2 - Suministrada
	 * @return
	 */
	public int getEstatus() {
		return estatus;
	}

	/**
	 * 0 - Activa, 1 - Eliminada, 2 - Suministrada
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
 */