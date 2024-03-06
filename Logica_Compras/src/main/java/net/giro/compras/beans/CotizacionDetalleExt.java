package net.giro.compras.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import net.giro.inventarios.beans.ProductoExt;

/**
 * cotizacion_detalle
 * @author javaz
 *
 */
public class CotizacionDetalleExt implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private CotizacionExt idCotizacion;
	private ProductoExt idProducto;
	private double cantidad; // Cantidad a cotizar
	private double precioUnitario;
	private double importe;
	private double margen;
	private double cantidadInicial; // Cantidad Disponible al momento de Cotizar
	private double cantidadOrdenCompra; // Cantidad en Ordenes de Compra
	private double cantidadSuministrada; // Cantidad Comprada e Ingresada en Almacen
	private int estatus; // ESTATUS: 0 - Disponible, 1 - No Disponible
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	public CotizacionDetalleExt() {
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CotizacionExt getIdCotizacion() {
		return idCotizacion;
	}

	public void setIdCotizacion(CotizacionExt idCotizacion) {
		this.idCotizacion = idCotizacion;
	}

	public ProductoExt getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(ProductoExt idProducto) {
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
	 * Cantidad Disponible al momento de Cotizar
	 * @return
	 */
	public double getCantidadInicial() {
		return cantidadInicial;
	}

	/**
	 * Cantidad Disponible al momento de Cotizar
	 * @param cantidad
	 */
	public void setCantidadInicial(double cantidadInicial) {
		this.cantidadInicial = cantidadInicial;
	}

	/**
	 * Cantidad en Ordenes de Compra
	 * @return
	 */
	public double getCantidadOrdenCompra() {
		return cantidadOrdenCompra;
	}

	/**
	 * Cantidad en Ordenes de Compra
	 * @param cantidadOrdenCompra
	 */
	public void setCantidadOrdenCompra(double cantidadOrdenCompra) {
		this.cantidadOrdenCompra = cantidadOrdenCompra;
	}

	/**
	 * Cantidad Comprada e Ingresada en Almacen
	 * @return
	 */
	public double getCantidadSuministrada() {
		return cantidadSuministrada;
	}

	/**
	 * Cantidad Comprada e Ingresada en Almacen
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
		return (getSuministroPendiente() > 0 ? ((this.cantidad - this.cantidadOrdenCompra) > 0 ? (this.cantidad - this.cantidadOrdenCompra) : 0) : 0);
		//return (this.cantidad - this.cantidadOrdenCompra) > 0 ? (this.cantidad - this.cantidadOrdenCompra) : 0;
	}

	public void setPendiente(double cantidadPendiente) {}
	
	public double getSuministroPendiente() {
		return ((this.cantidad - this.cantidadSuministrada) > 0) ? (this.cantidad - this.cantidadSuministrada) : 0;
	}
	
	public void setSuministroPendiente(double value) {}

	public void addCantidad(double cantidad) {
		this.cantidad += cantidad;
	}

	public void addOrdenCompra(double cantidadOrdenCompra) {
		this.cantidadOrdenCompra += cantidadOrdenCompra;
		this.cantidadOrdenCompra = (this.cantidadOrdenCompra > 0 ? this.cantidadOrdenCompra : 0);
		this.estatus = (this.cantidadOrdenCompra >= this.cantidad ? 1 : 0);
	}

	public void addSuministrado(double cantidadSuministrada) {
		this.cantidadSuministrada += cantidadSuministrada;
		this.cantidadSuministrada = (this.cantidadSuministrada > 0 ? this.cantidadSuministrada : 0);
		this.estatus = (this.cantidadSuministrada >= this.cantidad ? 1 : 0);
	}

	// -------------------------------------------------------------------
	
	public String getProducto() {
		if (this.idProducto != null && this.idProducto.getId() != null && this.idProducto.getId() > 0 && this.idProducto.getDescripcion() != null)
			return this.idProducto.getId() + " - " + this.idProducto.getDescripcion();
		return "";
	}
	
	public void setProducto(String value) {}
	
	public String getProductoClave() {
		if (this.idProducto != null && this.idProducto.getId() != null && this.idProducto.getId() > 0 && this.idProducto.getClave() != null)
			return this.idProducto.getClave();
		return "";
	}
	
	public void setProductoClave(String value) {}
	
	public String getProductoNombre() {
		if (this.idProducto != null && this.idProducto.getDescripcion() != null)
			return this.idProducto.getDescripcion();
		return "";
	}
	
	public void setProductoNombre(String value) {}
	
	public double getProductoPrecioUnitario() {
		if (this.idProducto != null && this.idProducto.getId() != null && this.idProducto.getPrecioUnitario() > 0)
			return this.idProducto.getPrecioUnitario();
		return 0;
	}
	
	public void setProductoPrecioUnitario(double precioUnitario) {}
	
	public String getProductoUnidad() {
		if (this.idProducto != null && this.idProducto.getUnidadMedida() != null && this.idProducto.getUnidadMedida().getId() > 0)
			return this.idProducto.getUnidadMedida().getDescripcion();
		return "";
	}
	
	public void setProductoUnidad(String value) {}
	
	public String getFamilia() {
		if (this.idProducto != null && this.idProducto.getFamilia() != null && this.idProducto.getFamilia().getId() > 0)
			return this.idProducto.getFamilia().getDescripcion();
		return "";
	}
	
	public void setFamilia(String value) {}
		
	public double getProductoImporte() {
		if (this.idProducto != null && this.idProducto.getId() != null && this.cantidad > 0)
			return (this.idProducto.getPrecioUnitario() * this.cantidad);
		return 0;
	}
	
	public void setProductoImporte(double value) {}
	
	public double getProductoExistensia() {
		if (this.idProducto != null && this.idProducto.getId() != null && this.cantidad > 0)
			return this.idProducto.getExistencia();
		return 0;
	}
	
	public void setProductoExistensia(double value) {}

	// ---------------------------------------------------------------------------------------------------------
	
	public CotizacionDetalleExt getCopia() {
		return this.Copia();
	}
	
	public CotizacionDetalleExt Copia() {
		CotizacionDetalleExt dest = null;
		
		try {
			dest = new CotizacionDetalleExt();
			dest.setId(this.id);
			dest.setIdCotizacion(this.idCotizacion);
			dest.setIdProducto(this.idProducto);
			dest.setCantidad(this.cantidad);
			//dest.setCotizar(this.cotizar);
			dest.setCantidadInicial(this.cantidadInicial);
			dest.setCantidadOrdenCompra(this.cantidadOrdenCompra);
			dest.setCantidadSuministrada(this.cantidadSuministrada);
			dest.setPrecioUnitario(this.precioUnitario);
			dest.setImporte(this.importe);
			dest.setMargen(this.margen);
			dest.setEstatus(this.estatus);
			dest.setCreadoPor(this.creadoPor);
			dest.setFechaCreacion(this.fechaCreacion);
			dest.setModificadoPor(this.modificadoPor);
			dest.setFechaModificacion(this.fechaModificacion);
		} catch (Exception e) {
			throw e;
		}
		
		return dest;
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */