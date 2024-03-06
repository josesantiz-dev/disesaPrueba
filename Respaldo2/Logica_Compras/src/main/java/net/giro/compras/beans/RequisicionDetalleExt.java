package net.giro.compras.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import net.giro.inventarios.beans.ProductoExt;

public class RequisicionDetalleExt implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private RequisicionExt idRequisicion;
	private ProductoExt idProducto;
	private double cantidad;
	private double cantidadInicial;
	private long idCotizacion;
	private String cotizacionFolio;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	
	public RequisicionDetalleExt() {
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
		this.cotizacionFolio = "";
	}
	
	public RequisicionDetalleExt(Long id) {
		this();
		this.id = id;
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

	public double getCantidadInicial() {
		return cantidadInicial;
	}

	public void setCantidadInicial(double cantidadInicial) {
		this.cantidadInicial = cantidadInicial;
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

	public long getIdCotizacion() {
		return idCotizacion;
	}

	public void setIdCotizacion(long idCotizacion) {
		this.idCotizacion = idCotizacion;
	}

	public String getCotizacionFolio() {
		return cotizacionFolio;
	}

	public void setCotizacionFolio(String cotizacionFolio) {
		this.cotizacionFolio = cotizacionFolio;
	}
	
	// -------------------------------------------------------------------------------------------------
	
	public String getProducto() {
		if (this.idProducto != null && this.idProducto.getId() != null && this.idProducto.getId() > 0 && this.idProducto.getDescripcion() != null)
			return this.idProducto.getId() + " - " + this.idProducto.getDescripcion();
		return "";
	}
	
	public void setProducto(String value) {}
	
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
			dest.setCreadoPor(this.creadoPor);
			dest.setFechaCreacion(this.fechaCreacion);
			dest.setModificadoPor(this.modificadoPor);
			dest.setFechaModificacion(this.fechaModificacion);
			dest.setIdCotizacion(this.idCotizacion);
			dest.setCotizacionFolio(this.cotizacionFolio);
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