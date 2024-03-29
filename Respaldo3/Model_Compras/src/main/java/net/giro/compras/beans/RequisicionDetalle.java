package net.giro.compras.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * requisicion_detalle
 * @author javaz
 */
public class RequisicionDetalle implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long idRequisicion;
	private Long idProducto;
	private double cantidad; 
	private double cantidadInicial; // Cantidad inicial al momento del registro
	private double cantidadOrdenCompra; // Cantidad en Ordenes de Compra
	private double cantidadSuministrada; // Cantidad Comprada y en Almacen
	private int estatus; // ESTATUS: 0 - Disponible, 1 - No Disponible
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	public RequisicionDetalle() {
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

	public Long getIdRequisicion() {
		return idRequisicion;
	}

	public void setIdRequisicion(Long idRequisicion) {
		this.idRequisicion = idRequisicion;
	}

	public Long getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(Long idProducto) {
		this.idProducto = idProducto;
	}

	public double getCantidad() {
		return cantidad;
	}

	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}

	public double getCantidadInicial() {
		return cantidadInicial;
	}

	public void setCantidadInicial(double cantidadInicial) {
		this.cantidadInicial = cantidadInicial;
	}

	public double getCantidadOrdenCompra() {
		return cantidadOrdenCompra;
	}

	public void setCantidadOrdenCompra(double cantidadOrdenCompra) {
		this.cantidadOrdenCompra = cantidadOrdenCompra;
	}

	public double getCantidadSuministrada() {
		return cantidadSuministrada;
	}

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

	public boolean isEditable() {
		return getEditable();
	}
	
	public boolean getEditable() {
		return (this.estatus == 0);
	}

	public void setEditable(boolean value) {}
	
	/**
	 * Cantidad pendiente para Comprar siempre y cuando tenga pendiente suministro
	 * @return
	 */
	public double getPendiente() {
		return (getSuministroPendiente() > 0 ? ((this.cantidad - this.cantidadOrdenCompra) > 0 ? (this.cantidad - this.cantidadOrdenCompra) : 0) : 0);
		//return ((this.cantidad - this.cantidadOrdenCompra) > 0) ? (this.cantidad - this.cantidadOrdenCompra) : 0;
	}

	public void setPendiente(double cantidad) {}

	public double getSuministroPendiente() {
		return ((this.cantidad - this.cantidadSuministrada) > 0) ? (this.cantidad - this.cantidadSuministrada) : 0;
	}
	
	public void setSuministroPendiente(double cantidad) {}

	// -------------------------------------------------------------------
	
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
 *  2.2 | 2017-07-15 | Javier Tirado 	| Añado propiedades idCotizacion y cotizacionFolio;
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */