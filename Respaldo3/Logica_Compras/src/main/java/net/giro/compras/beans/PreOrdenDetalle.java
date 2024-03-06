package net.giro.compras.beans;

import java.io.Serializable;

import net.giro.inventarios.beans.ProductoExt;

public class PreOrdenDetalle implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private ProductoExt idProducto;
	private double cantidad;
	private double precioUnitario;
	private double importe;
	private boolean seleccionado;
	private double cantidadOriginal;
	private double cantidadAlmacen;
	private double cantidadTraspasar;
	private double suministrado;
	// ----------------------------------
	private OrdenCompraDetalleExt backup;
	
	public PreOrdenDetalle() {}
	
	public PreOrdenDetalle(OrdenCompraDetalleExt value) {
		this();
		this.id = value.getId();
		this.idProducto = value.getIdProducto();
		this.cantidadOriginal = value.getCantidad();
		this.cantidad = value.getCantidad();
		this.cantidadAlmacen = 0;
		this.precioUnitario = value.getPrecioUnitario();
		this.importe = value.getImporte();
		this.suministrado = value.getCantidadSuministrada();
		this.seleccionado = true;
		// ----------------------------------
		this.backup = value.Copia();
	}
	
	public PreOrdenDetalle(CotizacionDetalleExt value) {
		this();
		this.id = 0L;
		this.idProducto = value.getIdProducto();
		this.cantidadOriginal = value.getPendiente(); 
		this.cantidad = value.getPendiente();
		this.cantidadAlmacen = 0;
		this.precioUnitario = value.getPrecioUnitario();
		this.importe = value.getImporte();
		this.suministrado = 0;
		this.seleccionado = false;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public double getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(double precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

	public double getImporte() {
		this.importe = this.precioUnitario * this.cantidad;
		return this.importe;
	}

	public void setImporte(double importe) {
		this.importe = importe;
	}

	public boolean isSeleccionado() {
		return seleccionado;
	}

	public void setSeleccionado(boolean seleccionado) {
		this.seleccionado = seleccionado;
	}

	public double getCantidadOriginal() {
		return cantidadOriginal;
	}

	public void setCantidadOriginal(double cantidadOriginal) {
		this.cantidadOriginal = cantidadOriginal;
	}

	public double getCantidadAlmacen() {
		return cantidadAlmacen;
	}

	public void setCantidadAlmacen(double cantidadAlmacen) {
		this.cantidadAlmacen = cantidadAlmacen;
	}

	public double getCantidadTraspasar() {
		return cantidadTraspasar;
	}

	public double getSuministrado() {
		return suministrado;
	}

	public void setSuministrado(double cantidad) {}

	// --------------------------------------------------------------------
	// EXTENDIDOS
	// --------------------------------------------------------------------

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

	// --------------------------------------------------------------------
	// METODOS
	// --------------------------------------------------------------------
	
	public OrdenCompraDetalleExt getOrdenCompraDetalle() {
		if (this.backup != null) {
			this.backup.setCantidad(this.cantidad);
			this.backup.setPrecioUnitario(this.precioUnitario);
			this.backup.setCantidadSuministrada(this.suministrado);
		} else {
			this.backup = new OrdenCompraDetalleExt();
			this.backup.setId(this.id);
			this.backup.setIdProducto(this.idProducto);
			this.backup.setCantidad(this.cantidad);
			this.backup.setPrecioUnitario(this.precioUnitario);
			this.backup.setImporte(this.getImporte());
			this.backup.setCantidadSuministrada(this.suministrado);
		}
		
		return this.backup;
	}
	
	public void normalizaCantidad() {
		this.cantidadTraspasar = 0;
		if (this.cantidadAlmacen <= 0)
			return;
		if (this.cantidadOriginal <= 0)
			return;
		
		this.cantidad = this.cantidadOriginal;
		if (this.cantidad > this.cantidadAlmacen) {
			this.cantidad = this.cantidad - this.cantidadAlmacen;
			this.cantidadTraspasar = this.cantidadAlmacen;
		} else if (this.cantidad <= this.cantidadAlmacen) {
			this.cantidadTraspasar = this.cantidad;
		}
		
		getImporte();
	}
}
