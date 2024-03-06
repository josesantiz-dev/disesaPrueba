package net.giro.inventarios.beans;

import java.io.Serializable;
import java.util.Date;

public class ProductoHistorial implements Serializable {
	private static final long serialVersionUID = 1L;
	private long idMovimiento;
	private int tipoMovimiento; // -1:Desconocido, 0:Entrada, 1:Salida
	private int conceptoMovimiento; //  0:Desconocido, 1:Orden de Compra, 2:Traspaso, 3:Devolucion, 4:Intalacion
	private String referencia;
	private Date fecha;
	// ---------------------------------------------------------------------------------
	private long idAlmacen;
	private String almacen;
	// ---------------------------------------------------------------------------------
	private long idProducto;
	private double cantidad;

	public ProductoHistorial(AlmacenMovimientos movimiento, long idProducto, double cantidad) {
		if (movimiento == null || idProducto <= 0L)
			return;
		this.fecha = movimiento.getFecha();
		this.idMovimiento = movimiento.getId();
		this.tipoMovimiento = movimiento.getTipo();
		if (this.tipoMovimiento == 0 && movimiento.getIdOrdenCompra() > 0L) {
			this.conceptoMovimiento = 1;
			this.referencia = movimiento.getFolioOrdenCompra();
		} else if (this.tipoMovimiento == 0 && movimiento.getIdTraspaso() > 0L) {
			this.conceptoMovimiento = 2;
			this.referencia = String.valueOf(movimiento.getIdTraspaso());
		} else if (this.tipoMovimiento == 1 && movimiento.getIdTraspaso() > 0L) {
			this.conceptoMovimiento = 3;
			this.referencia = String.valueOf(movimiento.getIdTraspaso());
		} else if (this.tipoMovimiento == 1 && movimiento.getIdObra() > 0L) {
			this.conceptoMovimiento = 4;
			this.referencia = movimiento.getIdObra() + " - " + movimiento.getNombreObra();
		} else {
			this.conceptoMovimiento = -1;
			this.referencia = "";
		}
		// ---------------------------------------------------------------------------------
		this.idAlmacen = movimiento.getIdAlmacen().getId();
		this.almacen = movimiento.getIdAlmacen().getNombre();
		// ---------------------------------------------------------------------------------
		this.idProducto = idProducto;
		this.cantidad = cantidad;
	}

	public ProductoHistorial(AlmacenMovimientosExt movimiento, long idProducto, double cantidad) {
		if (movimiento == null || idProducto <= 0L)
			return;
		this.fecha = movimiento.getFecha();
		this.idMovimiento = movimiento.getId();
		this.tipoMovimiento = movimiento.getTipo();
		if (this.tipoMovimiento == 0 && movimiento.getIdOrdenCompra() > 0L) {
			this.conceptoMovimiento = 1;
			this.referencia = movimiento.getFolioOrdenCompra();
		} else if (this.tipoMovimiento == 0 && movimiento.getIdTraspaso() > 0L) {
			this.conceptoMovimiento = 2;
			this.referencia = String.valueOf(movimiento.getIdTraspaso());
		} else if (this.tipoMovimiento == 1 && movimiento.getIdTraspaso() > 0L) {
			this.conceptoMovimiento = 3;
			this.referencia = String.valueOf(movimiento.getIdTraspaso());
		} else if (this.tipoMovimiento == 1 && movimiento.getIdObra() > 0L) {
			this.conceptoMovimiento = 4;
			this.referencia = movimiento.getIdObra() + " - " + movimiento.getNombreObra();
		} else {
			this.conceptoMovimiento = -1;
			this.referencia = "";
		}
		// ---------------------------------------------------------------------------------
		this.idAlmacen = movimiento.getAlmacen().getId();
		this.almacen = movimiento.getAlmacen().getNombre();
		// ---------------------------------------------------------------------------------
		this.idProducto = idProducto;
		this.cantidad = cantidad;
	}

	public ProductoHistorial(long idMovimiento, int tipoMovimiento, int conceptoMovimiento, Date fecha, long idAlmacen, String almacen, long idProducto, double cantidad) {
		this.idMovimiento = idMovimiento;
		this.tipoMovimiento = tipoMovimiento;
		this.conceptoMovimiento = conceptoMovimiento;
		this.fecha = fecha;
		this.referencia = "NA";
		// ---------------------------------------------------------------------------------
		this.idAlmacen = idAlmacen;
		this.almacen = almacen;
		// ---------------------------------------------------------------------------------
		this.idProducto = idProducto;
		this.cantidad = cantidad;
	}

	
	public long getIdMovimiento() {
		return idMovimiento;
	}

	public void setIdMovimiento(long idMovimiento) {
		this.idMovimiento = idMovimiento;
	}

	/**
	 * -1:Desconocido, 0:Entrada, 1:Salida
	 * @return
	 */
	public int getTipoMovimiento() {
		return tipoMovimiento;
	}

	/**
	 * -1:Desconocido, 0:Entrada, 1:Salida
	 * @param tipoMovimiento
	 */
	public void setTipoMovimiento(int tipoMovimiento) {
		this.tipoMovimiento = tipoMovimiento;
	}

	/**
	 * -1:Desconocido, 1:Orden de Compra, 2:Traspaso, 3:Devolucion, 4:Intalacion
	 * @return
	 */
	public int getConceptoMovimiento() {
		return conceptoMovimiento;
	}

	/**
	 * -1:Desconocido, 1:Orden de Compra, 2:Traspaso, 3:Devolucion, 4:Intalacion
	 * @param conceptoMovimiento
	 */
	public void setConceptoMovimiento(int conceptoMovimiento) {
		this.conceptoMovimiento = conceptoMovimiento;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public long getIdAlmacen() {
		return idAlmacen;
	}

	public void setIdAlmacen(long idAlmacen) {
		this.idAlmacen = idAlmacen;
	}

	public String getAlmacen() {
		return almacen;
	}

	public void setAlmacen(String almacen) {
		this.almacen = almacen;
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

	// -------------------------------------------------------------------------------------
	// EXTENDIDOS
	// -------------------------------------------------------------------------------------
	
	public String getDescripcion() {
		String descripcion = "";

		descripcion += (this.tipoMovimiento == 0) ? "Entrada" : (this.tipoMovimiento == 1 ? "Salida" : "Movimiento");
		if (this.conceptoMovimiento == 1)
			descripcion += " por Orden de Compra";
		else if (this.conceptoMovimiento == 2)
			descripcion += " por Traspaso";
		else if (this.conceptoMovimiento == 3)
			descripcion += " por Devolucion";
		else if (this.conceptoMovimiento == 4)
			descripcion += " a Instalacion";
		else
			descripcion += "desconocido";
		
		return descripcion;
	}
	
	public void setDescripcion(String value) {}
}
