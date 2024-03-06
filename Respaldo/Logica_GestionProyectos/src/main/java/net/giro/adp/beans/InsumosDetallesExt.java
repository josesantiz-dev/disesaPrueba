package net.giro.adp.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import net.giro.inventarios.beans.ProductoExt;


public class InsumosDetallesExt implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private InsumosExt idInsumos;
	private ProductoExt idProducto;
	private String nombreProducto;
	private	double cantidad;
	private double precioUnitario;
	private BigDecimal monto;
	private double porcentaje;
	private int tipo;
	private double cantidadSurtida;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	
	public InsumosDetallesExt() {
		this.nombreProducto = "";
		this.monto = BigDecimal.ZERO;
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public InsumosExt getIdInsumos() {
		return idInsumos;
	}
	
	public void setIdInsumos(InsumosExt idInsumos) {
		this.idInsumos = idInsumos;
	}
	
	public ProductoExt getIdProducto() {
		return idProducto;
	}
	
	public void setIdProducto(ProductoExt idProducto) {
		this.idProducto = idProducto;
		if (this.idProducto != null)
			this.nombreProducto = this.idProducto.getDescripcion();
	}
	
	public String getNombreProducto() {
		return nombreProducto;
	}
	
	public void setNombreProducto(String nombreProducto) {
		this.nombreProducto = nombreProducto;
	}
	
	public double getCantidad() {
		return cantidad;
	}
	
	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}
	
	public double getPrecioUnitario() {
		return this.precioUnitario;
	}
	
	public void setPrecioUnitario(double precioUnitario) {
		this.precioUnitario = precioUnitario;
	}
	
	public BigDecimal getMonto() {
		return monto;
	}
	
	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}
	
	public double getPorcentaje() {
		return porcentaje;
	}
	
	public void setPorcentaje(double porcentaje) {
		this.porcentaje = porcentaje;
	}
	
	/**
	 * TIPO DE INSUMO: 1 - MATERIALES, 2 - MANO DE OBRA, 3 - HERRAMIENTA
	 * @return
	 */
	public int getTipo() {
		return tipo;
	}
	
	/**
	 * TIPO DE INSUMO: 1 - MATERIALES, 2 - MANO DE OBRA, 3 - HERRAMIENTA
	 * @param tipo
	 */
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	
	public double getCantidadSurtida() {
		return cantidadSurtida;
	}
	
	public void setCantidadSurtida(double cantidadSurtida) {
		this.cantidadSurtida = cantidadSurtida;
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

	// ---------------------------------------------------------------------------------
	// EXTENDIDOS 
	// ---------------------------------------------------------------------------------
	
	public String getProducto() {
		if (this.idProducto != null && this.idProducto.getId() != null && this.idProducto.getId() > 0 && this.idProducto.getDescripcion() != null)
			return this.idProducto.getId() + " - " + this.idProducto.getDescripcion();
		return "";
	}
	
	public void setProducto(String value) {}
	
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
	
	public String getUnidad() {
		if (this.idProducto != null && this.idProducto.getUnidadMedida() != null && this.idProducto.getUnidadMedida().getId() > 0)
			return this.idProducto.getUnidadMedida().getDescripcion();
		return "";
	}
	
	public void setUnidad(String value) {}
	
	public String getFamilia() {
		if (this.idProducto != null && this.idProducto.getFamilia() != null && this.idProducto.getFamilia().getId() > 0)
			return this.idProducto.getFamilia().getDescripcion();
		return "";
	}
	
	public void setFamilia(String value) {}
		
	public double getImporte() {
		if (this.idProducto != null && this.idProducto.getId() != null && this.cantidad > 0)
			return (this.idProducto.getPrecioUnitario() * this.cantidad);
		return 0;
	}
	
	public void setImporte(double value) {}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */