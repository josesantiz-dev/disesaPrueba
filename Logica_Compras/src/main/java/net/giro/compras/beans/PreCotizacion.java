package net.giro.compras.beans;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import net.giro.adp.beans.InsumosDetallesExt;
import net.giro.inventarios.beans.ProductoExt;

public class PreCotizacion implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private long idOrigen;
	private int origen;
	private List<String> origenes = null;
	// -------------------------------------------------------------------------------------------------------
	private ProductoExt idProducto;
	private double cantidad; 	 // Cantidad a cotizar
	private double precioUnitario;
	private double importe;
	private double requeridos; 	 // Cantidad inicial o requerida dependiendo del origen (Explosion, Requisicion)
	private double suministrado; // Cantidad suministrada en origen (Explosion, Requisicion)
	private double existencias;  // Existencias en Almacenes
	// -------------------------------------------------------------------------------------------------------
	private double cantidadOriginal;
	private boolean seleccionado;
	private boolean actualizado;
	private int estatus; // ESTATUS: 0-Cotizable, 1-Solicitud, 2-Cotizacion/Solicitud, 3-Suministrado, 4-Cotizado

	public PreCotizacion() {
		this.origenes = Arrays.asList("COTIZACION", "EXPLOSION INSUMOS", "REQUISICION");
		this.idProducto = new ProductoExt();
		this.origen = 0;
		this.id = null;
		this.idOrigen = 0L;
		this.estatus = 0;
		this.cantidad = 0;
		this.precioUnitario = 0;
		this.requeridos = 0;
		this.suministrado = 0;
		this.existencias = 0;
		this.seleccionado = true;
		this.actualizado = false;
	}
	
	public PreCotizacion(CotizacionDetalleExt detalle) {
		this();
		this.origen = 0;
		// -----------------------------------------------------------
		this.id = detalle.getId();
		this.idOrigen = 0L;
		// -----------------------------------------------------------
		this.idProducto = detalle.getIdProducto();
		this.cantidad = detalle.getCantidad();
		this.precioUnitario = detalle.getPrecioUnitario();
		this.requeridos = this.cantidad;
		this.suministrado = detalle.getCantidadSuministrada();
		this.seleccionado = true;
		this.actualizado = false;
		this.existencias = 0;
		// -----------------------------------------------------------
		this.calcular();
		this.getImporte();
		this.cantidadOriginal = this.cantidad;
	}
	
	public PreCotizacion(InsumosDetallesExt detalle) {
		this();
		this.origen = 1;
		// -----------------------------------------------------------
		this.id = 0L;
		this.idOrigen = detalle.getId();
		// -----------------------------------------------------------
		this.idProducto = detalle.getIdProducto();
		this.cantidad = detalle.getPendiente(); // Cantidad sugerida: Se modifica con el metodo calcular()
		this.precioUnitario = detalle.getPrecioUnitario();
		this.requeridos = this.cantidad;
		this.suministrado = 0;
		this.seleccionado = false;
		this.actualizado = false;
		this.existencias = 0;
		// -----------------------------------------------------------
		this.calcular();
		this.getImporte();
		this.cantidadOriginal = this.cantidad;
	}
	
	public PreCotizacion(RequisicionDetalleExt detalle) {
		this();
		this.origen = 2;
		// -----------------------------------------------------------
		this.id = 0L;
		this.idOrigen = detalle.getId();
		// -----------------------------------------------------------
		this.idProducto = detalle.getIdProducto();
		this.cantidad = detalle.getCantidad(); // Cantidad sugerida: Se modifica con el metodo calcular()
		this.precioUnitario = detalle.getProductoPrecioCompra();
		this.requeridos = this.cantidad;
		this.suministrado = 0;
		this.seleccionado = true;
		this.actualizado = false;
		this.existencias = 0;
		// -----------------------------------------------------------
		this.calcular();
		this.getImporte();
		this.cantidadOriginal = this.cantidad;
	}

	// -------------------------------------------------------------------------------------------------
	// METODOS
	// -------------------------------------------------------------------------------------------------

	public CotizacionDetalleExt getCotizacion() {
		CotizacionDetalleExt cotizacion = null;

		cotizacion = new CotizacionDetalleExt();
		cotizacion.setId(this.id);
		cotizacion.setIdProducto(this.idProducto);
		cotizacion.setCantidad(this.cantidad);
		cotizacion.setPrecioUnitario(this.precioUnitario);
		cotizacion.setImporte(this.getImporte());
		cotizacion.setCantidadInicial(this.requeridos);
		cotizacion.setCantidadSuministrada(this.suministrado);
		cotizacion.setMargen(0); 
		
		return cotizacion;
	}
	
	public SolicitudBodegaProducto getSolicitud() {
		SolicitudBodegaProducto solicitud = null;

		solicitud = new SolicitudBodegaProducto();
		solicitud.setIdProducto(this.idProducto.getId());
		solicitud.setClave(this.idProducto.getClave());
		solicitud.setDescripcion(this.idProducto.getDescripcion());
		if (this.idProducto.getFamilia() != null) {
			solicitud.setIdFamilia(this.idProducto.getFamilia().getId());
			solicitud.setFamilia(this.idProducto.getFamilia().getDescripcion());
		}
		if (this.idProducto.getUnidadMedida() != null) {
			solicitud.setIdUnidadMedida(this.idProducto.getUnidadMedida().getId());
			solicitud.setUnidadMedida(this.idProducto.getUnidadMedida().getDescripcion());
		}
		solicitud.setRequeridos(this.requeridos);
		solicitud.setIdAlmacen(0L);
		solicitud.setDisponible(0);
		solicitud.setSolicitud(0);
		
		return solicitud;
	}
	
	/**
	 * Establece la cantidad cotizable del producto, establece el estatus e indica si sufrio algun cambio
	 */
	public void recalcular() {
		double calculo = 0;
		
		calculo = this.cantidad;
		calcular();
		this.actualizado = false;
		if (calculo != this.cantidad)
			this.actualizado = true;
	}

	/**
	 * Establece la cantidad cotizable del producto y establece el estatus del producto
	 */
	private void calcular() {
		// Si el origen es una Cotizacion, no calculamos e indicamos que el producto ya ha sido cotizado
		if (this.origen == 0) {
			this.estatus = 4;
			return;
		}
		
		// Si ya ha sido suministrado, no calculamos e indicamos que ya esta suministrado el producto
		if (this.requeridos == this.suministrado) {
			this.cantidad = 0;
			this.estatus = 3;
			return;
		}
		
		// Cantidad a cotizar
		this.cantidad = this.requeridos; //(this.requeridos - this.suministrado);
		if (this.cantidad < 0)
			this.cantidad = 0;
		if (this.existencias > 0)
			this.cantidad = this.cantidad - this.existencias;
		if (this.cantidad < 0)
			this.cantidad = 0;
		
		// Estatus
		this.estatus = 0;
		if (this.cantidad <= 0 && this.existencias > 0)
			this.estatus = 1;
		else if (this.cantidad > 0 && this.existencias > 0)
			this.estatus = 2;
	}
	
	// -------------------------------------------------------------------------------------------------
	// PROPIEDADES
	// -------------------------------------------------------------------------------------------------
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public long getIdOrigen() {
		return idOrigen;
	}
	
	public void setIdOrigen(long idOrigen) {
		this.idOrigen = idOrigen;
	}
	
	public int getOrigen() {
		return origen;
	}
	
	public void setOrigen(int origen) {
		this.origen = origen;
	}

	public String getOrigenDescripcion() {
		return this.origenes.get(this.origen);
	}
	
	public void setOrigenDescripcion(String origenDescripcion) {}
	
	public ProductoExt getIdProducto() {
		return idProducto;
	}
	
	public void setIdProducto(ProductoExt idProducto) {
		this.idProducto = idProducto;
	}
	
	/**
	 * Cantidad inicial o requerida dependiendo del origen (Explosion, Requisicion)
	 * @return
	 */
	public double getRequeridos() {
		return requeridos;
	}
	
	/**
	 * Cantidad inicial o requerida dependiendo del origen (Explosion, Requisicion)
	 * @param requeridos
	 */
	public void setRequeridos(double requeridos) {
		if (requeridos <= 0)
			requeridos = 0;
		this.requeridos = requeridos;
	}
	
	/**
	 * Cantidad suministrada en origen (Explosion, Requisicion)
	 * @return
	 */
	public double getSuministrado() {
		return suministrado;
	}
	
	/**
	 * Cantidad suministrada en origen (Explosion, Requisicion)
	 * @param suministrado
	 */
	public void setSuministrado(double suministrado) {
		if (suministrado <= 0)
			suministrado = 0;
		this.suministrado = suministrado;
	}
	
	public boolean getEditable() {
		return this.suministrado < this.cantidad;
	}
	
	public void setEditable(boolean value) {}
	
	/**
	 * Existencias en Almacenes
	 * @return
	 */
	public double getExistencias() {
		return existencias;
	}
	
	/**
	 * Existencias en Almacenes
	 * @param existencias
	 */
	public void setExistencias(double existencias) {
		if (existencias <= 0)
			existencias = 0;
		this.existencias = existencias;
		this.calcular();
		this.getImporte();
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
		getImporte();
	}
	
	public double getPrecioUnitario() {
		return precioUnitario;
	}
	
	public void setPrecioUnitario(double precioUnitario) {
		if (precioUnitario <= 0)
			precioUnitario = 0;
		this.precioUnitario = precioUnitario;
	}
	
	public double getImporte() {
		this.importe = this.precioUnitario * this.cantidad;
		return this.importe;
	}
	
	public void setImporte(double importe) {}
	
	public boolean isSeleccionado() {
		return seleccionado;
	}
	
	public void setSeleccionado(boolean seleccionado) {
		this.seleccionado = seleccionado;
	}
	
	public boolean isActualizado() {
		return actualizado;
	}
	
	public void setActualizado(boolean actualizado) {
		this.actualizado = actualizado;
	}
	
	/**
	 * ESTATUS: 0-Cotizacion, 1-Solicitud, 2-Cotizacion/Solicitud, 3-Suministrado, 4-Cotizado
	 * @return
	 */
	public int getEstatus() {
		return estatus;
	}
	
	/**
	 * ESTATUS: 0-Cotizacion, 1-Solicitud, 2-Cotizacion/Solicitud, 3-Suministrado, 4-Cotizado
	 * @param estatus
	 */
	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}
	
	// -----------------------------------------------------------------------------------------
	
	public long getProductoId() {
		if (this.idProducto != null && this.idProducto.getId() != null)
			return this.idProducto.getId();
		return 0L;
	}
	
	public void setProductoId(long value) {}

	public String getProducto() {
		if (this.idProducto != null && this.idProducto.getId() != null && this.idProducto.getClave() != null && this.idProducto.getDescripcion() != null)
			return this.idProducto.getClave() + " - " + this.idProducto.getDescripcion();
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
	
	public String getProductoFamilia() {
		if (this.idProducto != null && this.idProducto.getFamilia() != null && this.idProducto.getFamilia().getId() > 0)
			return this.idProducto.getFamilia().getDescripcion();
		return "";
	}
	
	public void setProductoFamilia(String value) {}
	
	public long getProductoFamiliaId() {
		if (this.idProducto != null && this.idProducto.getFamilia() != null && this.idProducto.getFamilia().getId() > 0)
			return this.idProducto.getFamilia().getId();
		return 0L;
	}
	
	public void setProductoFamiliaId(long value) {}

	public double getProductoExistensia() {
		if (this.idProducto != null && this.idProducto.getId() != null && this.cantidad > 0)
			return this.idProducto.getExistencia();
		return 0;
	}
	
	public void setProductoExistensia(double value) {}

	public double getProductoImporte() {
		if (this.idProducto != null && this.idProducto.getId() != null && this.cantidad > 0)
			return (this.idProducto.getPrecioUnitario() * this.cantidad);
		return 0;
	}
	
	public void setProductoImporte(double value) {}
	
	public boolean cantidadModificada() {
		return (this.cantidadOriginal != this.cantidad);
	}

	// ---------------------------------------------------------------
	// METODOS
	// ---------------------------------------------------------------
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof PreCotizacion) {
			if (this.getProductoId() != ((PreCotizacion) o).getProductoId())
				return false;
			if (this.requeridos != ((PreCotizacion) o).getRequeridos())
				return false;
			if (this.cantidad != ((PreCotizacion) o).getCantidad())
				return false;
			return true;
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		int valObject = 0;
		int hash = 7; // factor hash
		
		valObject = (int) (this.getProductoId() + this.requeridos + this.cantidad);
		hash = 97 * hash + valObject;
		
		return hash;
	}
}
