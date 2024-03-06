package net.giro.compras.beans;

import java.io.Serializable;

import net.giro.adp.beans.InsumosDetallesExt;
import net.giro.inventarios.beans.ProductoExt;

public class PreCotizacionDetalle implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private ProductoExt idProducto;
	private double cantidad;
	private double cantidadInicial;
	private double precioUnitario;
	private double importe;
	private double existensia;
	private double cotizar;
	private boolean seleccionar;
	private long origenId;
	private String origenDescripcion;
	private String ordenCompra;
	private int estatus;

	private String FROM_COTIZACION = "COTIZACION";
	private String FROM_INSUMOS = "INSUMOS";
	private String FROM_REQUISICION = "REQUISICION";
	
	
	public PreCotizacionDetalle() {
		this.origenDescripcion = "";
		this.ordenCompra = "";
	}
	
	public PreCotizacionDetalle(CotizacionDetalleExt cotiDetalle) {
		this();
		this.id = cotiDetalle.getId();
		this.idProducto = cotiDetalle.getIdProducto();
		this.existensia = cotiDetalle.getIdProducto().getExistencia();
		this.cantidad = cotiDetalle.getCantidad();
		this.cantidadInicial = cotiDetalle.getCantidadInicial();
		this.precioUnitario = cotiDetalle.getPrecioUnitario();
		this.ordenCompra = "";
		this.estatus = cotiDetalle.getEstatus();
		this.seleccionar = true;
		
		this.origenId = cotiDetalle.getId();
		this.origenDescripcion = FROM_COTIZACION;
		
		this.setCotizar(this.cantidad - this.existensia);
		this.getImporte();
	}
	
	public PreCotizacionDetalle(InsumosDetallesExt insumoDetalle) {
		this();
		this.idProducto = insumoDetalle.getIdProducto();
		this.existensia = insumoDetalle.getIdProducto().getExistencia();
		this.cantidad = insumoDetalle.getCantidad();
		this.cantidadInicial = insumoDetalle.getCantidad();
		this.precioUnitario = insumoDetalle.getPrecioUnitario();
		this.ordenCompra = "";
		this.estatus = 0;
		this.seleccionar = false;
		
		this.origenId = insumoDetalle.getId();
		this.origenDescripcion = FROM_INSUMOS;
		
		this.setCotizar(this.cantidad - this.existensia);
		this.getImporte();
	}
	
	public PreCotizacionDetalle(RequisicionDetalleExt reqDetalle) {
		this();
		this.idProducto = reqDetalle.getIdProducto();
		this.existensia = reqDetalle.getIdProducto().getExistencia();
		this.cantidad = reqDetalle.getCantidad();
		this.cantidadInicial = reqDetalle.getCantidadInicial();
		this.precioUnitario = reqDetalle.getProductoPrecioCompra();
		this.ordenCompra = "";
		this.estatus = 0;
		this.seleccionar = true;
		
		this.origenId = reqDetalle.getId();
		this.origenDescripcion = FROM_REQUISICION;
		
		this.setCotizar(this.cantidad - this.existensia);
		this.getImporte();
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
		this.precioUnitario = idProducto.getPrecioUnitario();
	}

	public double getCantidad() {
		return cantidad;
	}

	public void setCantidad(double cantidad) {
		if(cantidad <= 0) 
			cantidad = 0;
		this.cantidad = cantidad;
	}

	public double getCotizar() {
		return cotizar;
	}

	public void setCotizar(double cotizar) {
		if (cotizar < 0) 
			cotizar = 0;
		this.cotizar = cotizar;
	}

	public double getCantidadInicial() {
		return cantidadInicial;
	}

	public void setCantidadInicial(double cantidadInicial) {
		this.cantidadInicial = cantidadInicial;
	}

	public double getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(double precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

	public double getImporte() {
		importe = this.precioUnitario * this.cotizar;
		return importe;
	}

	public void setImporte(double importe) {
		this.importe = importe;
	}

	public double getExistensia() {
		return existensia;
	}

	public void setExistensia(double existensia) {
		this.existensia = existensia;
	}

	public boolean getSeleccionar() {
		return seleccionar;
	}

	public void setSeleccionar(boolean seleccionar) {
		this.seleccionar = seleccionar;
	}

	public double getNuevoCotizar() {
		return cotizar;
	}

	public void setNuevoCotizar(double nuevoCotizar) {
		this.setCotizar(nuevoCotizar);
	}
	
	public long getOrigenId() {
		return origenId;
	}
	
	public void setOrigenId(long origenId) {
		this.origenId = origenId;
	}
	
	public String getOrigenDescripcion() {
		return origenDescripcion;
	}
	
	public void setOrigenDescripcion(String origenDescripcion) {
		this.origenDescripcion = origenDescripcion;
	}

	public String getOrdenCompra() {
		return ordenCompra;
	}

	public void setOrdenCompra(String ordenCompra) {
		this.ordenCompra = ordenCompra;
	}

	public int getEstatus() {
		return estatus;
	}

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
	
	public CotizacionDetalleExt getCotizacion() {
		CotizacionDetalleExt dest = new CotizacionDetalleExt();

		dest.setId(this.id);
		dest.setIdProducto(this.idProducto);
		dest.setCantidad(this.cotizar);
		dest.setCantidad(this.cantidadInicial);
		dest.setPrecioUnitario(this.precioUnitario);
		dest.setImporte(this.getImporte());
		dest.setMargen(0); // TO DO
		
		return dest;
	}
}
