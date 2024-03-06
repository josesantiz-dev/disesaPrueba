package net.giro.inventarios.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * movimientos_detalle
 * @author javaz
 */
public class MovimientosDetalle implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private long idAlmacenMovimiento;
	private long idProducto;
	private double cantidad;
	private double precioUnitario;
	private double cantidadSolicitada;
	private int ejecutado; // 0:sin Carga/Descarga, 1:Cargado/Descargado
	private int estatus; // 0
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	public MovimientosDetalle() {
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public long getIdAlmacenMovimiento() {
		return idAlmacenMovimiento;
	}

	public void setIdAlmacenMovimiento(long idAlmacenMovimiento) {
		this.idAlmacenMovimiento = idAlmacenMovimiento;
	}

	public long getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(long idProducto) {
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
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.1 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */