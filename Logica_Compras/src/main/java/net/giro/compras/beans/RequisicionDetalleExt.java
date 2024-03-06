package net.giro.compras.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import net.giro.inventarios.beans.ProductoExt;

/**
 * requisicion_detalle
 * @author javaz
 */
public class RequisicionDetalleExt implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private RequisicionExt idRequisicion;
	private ProductoExt idProducto;
	private double cantidad; // 
	private double cantidadInicial; // Cantidad inicial al momento del registro
	private double cantidadOrdenCompra; // Cantidad en Ordenes de Compra
	private double cantidadSuministrada; // Cantidad Comprada y en Almacen
	private int estatus; // ESTATUS: 0 - Disponible, 1 - No Disponible
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	public RequisicionDetalleExt() {
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public RequisicionExt getIdRequisicion() {
		return idRequisicion;
	}

	public void setIdRequisicion(RequisicionExt idRequisicion) {
		this.idRequisicion = idRequisicion;
	}

	public ProductoExt getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(ProductoExt idProducto) {
		this.idProducto = idProducto;
	}

	public double getCantidad() {
		return cantidad;
	}

	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}

	/**
	 * Cantidad inicial al momento del registro
	 * @return
	 */
	public double getCantidadInicial() {
		return cantidadInicial;
	}

	/**
	 * Cantidad inicial al momento del registro
	 * @param cantidadInicial
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
	 * Cantidad Comprada y en Almacen
	 * @return
	 */
	public double getCantidadSuministrada() {
		return cantidadSuministrada;
	}

	/**
	 * Cantidad Comprada y en Almacen
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

	// -------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// -------------------------------------------------------------------------------------------------

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
		this.cantidad = (this.cantidad > 0 ? this.cantidad : 0);
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

	// -------------------------------------------------------------------
	
	public String getProducto() {
		if (this.idProducto != null && this.idProducto.getId() != null && this.idProducto.getId() > 0 && this.idProducto.getDescripcion() != null)
			return this.idProducto.getId() + " - " + this.idProducto.getDescripcion();
		return "";
	}
	
	public void setProducto(String value) {}

	public long getProductoId() {
		if (this.idProducto != null && this.idProducto.getId() != null && this.idProducto.getId() > 0L)
			return this.idProducto.getId();
		return 0L;
	}
	
	public void setProductoId(long value) {}
	
	public String getProductoClave() {
		if (this.idProducto != null && this.idProducto.getClave() != null)
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

	public double getProductoPrecioCompra() {
		if (this.idProducto != null && this.idProducto.getId() != null && this.idProducto.getPrecioCompra() > 0)
			return this.idProducto.getPrecioCompra();
		return 0;
	}
	
	public void setProductoPrecioCompra(double precioCompra) {}

	public double getProductoPrecioVenta() {
		if (this.idProducto != null && this.idProducto.getId() != null && this.idProducto.getPrecioVenta() > 0)
			return this.idProducto.getPrecioVenta();
		return 0;
	}
	
	public void setProductoPrecioVenta(double precioVenta) {}
	
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
	
	public String getSuministrado() {
		if (this.cantidadSuministrada >= this.cantidad)
			return "SUMINISTRADO";
		if (this.cantidadSuministrada > 0)
			return "Suministrados: " + this.cantidadSuministrada;
		return "";
	}
	
	public void setSuministrado(String value) {}

	// -------------------------------------------------------------------------------------------------
	// METODOS
	// -------------------------------------------------------------------------------------------------
	
	public RequisicionDetalleExt getCopia() {
		return this.Copia();
	}
	
	public RequisicionDetalleExt Copia() {
		RequisicionDetalleExt dest = new RequisicionDetalleExt();
		
		try {
			dest.setId(this.id);
			dest.setIdRequisicion(this.idRequisicion);
			dest.setIdProducto(this.idProducto);
			dest.setCantidad(this.cantidad);
			dest.setCantidadInicial(this.cantidadInicial);
			//dest.setCantidadCotizada(this.cantidadCotizada);
			dest.setCantidadOrdenCompra(this.cantidadOrdenCompra);
			dest.setCantidadSuministrada(this.cantidadSuministrada);
			//dest.setIdCotizacion(this.idCotizacion);
			//dest.setCotizacionFolio(this.cotizacionFolio);
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