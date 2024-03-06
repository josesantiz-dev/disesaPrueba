package net.giro.compras.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * orden_compra_detalle
 * @author javaz
 */
public class OrdenCompraDetalle implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private long idOrdenCompra;
	private long idProducto;
	private double cantidad;
	private double precioUnitario;
	private double importe;
	private double cantidadSuministrada;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	public OrdenCompraDetalle() {
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

	public Long getIdOrdenCompra() {
		return idOrdenCompra;
	}

	public void setIdOrdenCompra(Long idOrdenCompra) {
		idOrdenCompra = idOrdenCompra != null ? idOrdenCompra : 0L;
		this.idOrdenCompra = idOrdenCompra;
	}

	public Long getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(Long idProducto) {
		idProducto = idProducto != null ? idProducto : 0L;
		this.idProducto = idProducto;
	}

	public double getCantidad() {
		return cantidad;
	}

	public void setCantidad(double cantidad) {
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

	public double getCantidadSuministrada() {
		return cantidadSuministrada;
	}

	public void setCantidadSuministrada(double cantidad) {
		this.cantidadSuministrada = cantidad;
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
	 * Cantidad pendiente por suministrar
	 * @return
	 */
	public double getPendiente() {
		return ((this.cantidad - this.cantidadSuministrada) > 0) ? (this.cantidad - this.cantidadSuministrada) : 0;
	}
	
	public void setPendiente(double cantidad) {}
	
	public void addSuministrada(double cantidad) {
		this.cantidadSuministrada += cantidad;
		this.cantidadSuministrada = this.cantidadSuministrada > 0 ? this.cantidadSuministrada : 0;
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */