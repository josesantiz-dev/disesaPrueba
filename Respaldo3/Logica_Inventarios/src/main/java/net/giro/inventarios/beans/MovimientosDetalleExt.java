package net.giro.inventarios.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class MovimientosDetalleExt implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private long idAlmacenMovimiento;
	private ProductoExt producto;
	private double cantidad;		//Tambien fungirá como cantidadRecibida
	private double precioUnitario;
	private double cantidadSolicitada;
	private int ejecutado; // 0:sin Carga/Descarga, 1:Cargado/Descargado
	private int estatus;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	private double cantidad_auxiliar1; // Cantidad pendiente
	
	public MovimientosDetalleExt() {
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getIdAlmacenMovimiento() {
		return idAlmacenMovimiento;
	}

	public void setIdAlmacenMovimiento(long idAlmacenMovimiento) {
		this.idAlmacenMovimiento = idAlmacenMovimiento;
	}

	public ProductoExt getProducto() {
		return producto;
	}

	public void setProducto(ProductoExt producto) {
		this.producto = producto;
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
	
	public double getCantidadSolicitada() {
		return cantidadSolicitada;
	}

	public void setCantidadSolicitada(double cantidadSolicitada) {
		this.cantidadSolicitada = cantidadSolicitada;
	}

	/**
	 * 0:sin Carga/Descarga, 1:Cargado/Descargado
	 * @return
	 */
	public int getEjecutado() {
		return ejecutado;
	}

	/**
	 * 0:sin Carga/Descarga, 1:Cargado/Descargado
	 * @param ejecutado
	 */
	public void setEjecutado(int ejecutado) {
		this.ejecutado = ejecutado;
	}

	public int getEstatus() {
		return estatus;
	}

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

	/**
	 * Cantidad pendiente
	 * @return
	 */
	public double getCantidad_auxiliar1() {
		return cantidad_auxiliar1;
	}

	/**
	 * Cantidad pendiente
	 * @param cantidad_auxiliar1
	 */
	public void setCantidad_auxiliar1(double cantidad_auxiliar1) {
		this.cantidad_auxiliar1 = cantidad_auxiliar1;
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.1 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */