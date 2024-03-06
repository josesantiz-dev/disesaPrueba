package net.giro.compras.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * cotizacion_detalle
 * @author javaz
 */
public class CotizacionDetalle implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long idCotizacion;
	private Long idProducto;
	private double cantidad; // Cantidad a cotizar
	private double precioUnitario;
	private double importe;
	private double margen;
	private double cantidadInicial; // cantidad inicial (Requeridos)
	private double cantidadOrdenCompra; // cantidad en ordenes de compra
	private double cantidadSuministrada; // cantidad suministrada
	private int estatus; // ESTATUS: 0 - Disponible, 1 - No Disponible
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	public CotizacionDetalle() {
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

	public Long getIdCotizacion() {
		return idCotizacion;
	}

	public void setIdCotizacion(Long idCotizacion) {
		this.idCotizacion = idCotizacion;
	}

	public Long getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(Long idProducto) {
		this.idProducto = idProducto;
	}

	/**
	 * Cantidad a cotizar
	 * @return
	 */
	public double getCantidad() {
		return cantidad;
	}

	/**
	 * Cantidad a cotizar
	 * @param cantidad
	 */
	public void setCantidad(double cantidad) {
		if (cantidad <= 0)
			cantidad = 0;
		this.cantidad = cantidad;
	}

	public double getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(double precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

	public double getImporte() {
		return importe;
	}

	public void setImporte(double importe) {
		this.importe = importe;
	}

	public double getMargen() {
		return margen;
	}

	public void setMargen(double margen) {
		this.margen = margen;
	}

	/**
	 * cantidad inicial (Requeridos)
	 * @return
	 */
	public double getCantidadInicial() {
		return cantidadInicial;
	}

	/**
	 * cantidad inicial (Requeridos)
	 * @param cantidadInicial
	 */
	public void setCantidadInicial(double cantidadInicial) {
		if (cantidadInicial <= 0)
			cantidadInicial = 0;
		this.cantidadInicial = cantidadInicial;
	}

	/**
	 * cantidad en ordenes de compra
	 * @return
	 */
	public double getCantidadOrdenCompra() {
		return cantidadOrdenCompra;
	}

	/**
	 * cantidad en ordenes de compra
	 * @param cantidadOrdenCompra
	 */
	public void setCantidadOrdenCompra(double cantidadOrdenCompra) {
		this.cantidadOrdenCompra = cantidadOrdenCompra;
	}

	/**
	 * cantidad suministrada
	 * @return
	 */
	public double getCantidadSuministrada() {
		return cantidadSuministrada;
	}

	/**
	 * cantidad suministrada
	 * @param cantidadSuministrada
	 */
	public void setCantidadSuministrada(double cantidadSuministrada) {
		this.cantidadSuministrada = cantidadSuministrada;
	}

	/**
	 * ESTATUS: 0 - Disponible, 1 - No Disponible
	 * @return
	 */
	public int getEstatus() {
		return estatus;
	}

	/**
	 * ESTATUS: 0 - Disponible, 1 - No Disponible
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

	// -------------------------------------------------------------------
	// EXTENDIDOS 
	// -------------------------------------------------------------------
	
	/**
	 * Cantidad pendiente para Comprar siempre y cuando tenga pendiente suministro
	 * @return
	 */
	public double getPendiente() {
		//return (getSuministroPendiente() > 0 ? ((this.cantidad - this.cantidadOrdenCompra) > 0 ? (this.cantidad - this.cantidadOrdenCompra) : 0) : 0);
		return (this.cantidad - this.cantidadOrdenCompra) > 0 ? (this.cantidad - this.cantidadOrdenCompra) : 0;
	}

	public void setPendiente(double cantidad) {}
	
	public double getSuministroPendiente() {
		//return ((this.cantidad - this.cantidadSuministrada) > 0) ? (this.cantidad - this.cantidadSuministrada) : 0;
		return ((this.cantidadInicial - this.cantidadSuministrada) > 0) ? (this.cantidadInicial - this.cantidadSuministrada) : 0;
	}
	
	public void setSuministroPendiente(double value) {}

	public void addCantidad(double cantidad) {
		this.cantidad += cantidad;
	}

	public void addOrdenCompra(double cantidad) {
		this.cantidadOrdenCompra += cantidad;
		this.cantidadOrdenCompra = (this.cantidadOrdenCompra > 0 ? this.cantidadOrdenCompra : 0);
		this.estatus = (this.cantidadOrdenCompra >= this.cantidad ? 1 : 0);
	}

	public void addSuministrado(double cantidad) {
		this.cantidadSuministrada += cantidad;
		this.cantidadSuministrada = (this.cantidadSuministrada > 0 ? this.cantidadSuministrada : 0);
		this.estatus = (this.cantidadSuministrada >= this.cantidad ? 1 : 0);
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */