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
	private OrdenCompraDetalleExt backup;
	
	
	public PreOrdenDetalle() {}
	
	public PreOrdenDetalle(OrdenCompraDetalleExt value) {
		this();
		this.id = value.getId();
		this.idProducto = value.getIdProducto();
		this.cantidad = value.getCantidad();
		this.precioUnitario = value.getPrecioUnitario();
		this.importe = value.getImporte();
		this.seleccionado = true;
		
		this.backup = value.Copia();
	}
	
	public PreOrdenDetalle(CotizacionDetalleExt value) {
		this();
		this.id = 0L;
		this.idProducto = value.getIdProducto();
		this.cantidad = value.getCantidad();
		this.precioUnitario = value.getPrecioUnitario();
		this.importe = value.getImporte();
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
		importe = this.precioUnitario * this.cantidad;
		return importe;
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
	
	
	public OrdenCompraDetalleExt getOrdenCompraDetalle() {
		if (this.backup != null) {
			this.backup.setCantidad(this.cantidad);
			this.backup.setPrecioUnitario(this.precioUnitario);
		} else {
			this.backup = new OrdenCompraDetalleExt();

			this.backup.setId(this.id);
			this.backup.setIdProducto(this.idProducto);
			this.backup.setCantidad(this.cantidad);
			this.backup.setPrecioUnitario(this.precioUnitario);
			this.backup.setImporte(this.getImporte());
		}
		
		return this.backup;
	}
}
