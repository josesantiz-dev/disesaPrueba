package net.giro.inventarios.beans;

import java.io.Serializable;
import java.util.Date;

public class MovimientosDetalleExt implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private long idAlmacenMovimiento;
	private ProductoExt producto;
	private double cantidad;		//Tambien fungirá como cantidadRecibida
	private double cantidadSolicitada;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	private int estatus;
	private double cantidad_auxiliar1;
	
	
	public MovimientosDetalleExt() {}

	
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

	public int getEstatus() {
		return estatus;
	}

	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}

	public double getCantidadSolicitada() {
		return cantidadSolicitada;
	}

	public void setCantidadSolicitada(double cantidadSolicitada) {
		this.cantidadSolicitada = cantidadSolicitada;
	}

	public double getCantidad_auxiliar1() {
		return cantidad_auxiliar1;
	}

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