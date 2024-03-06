package net.giro.inventarios.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * traspaso_detalle
 * @author javaz
 *
 */
public class TraspasoDetalle implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long idAlmacenTraspaso;
	private long idProducto;
	private double cantidad;
	private double precioUnitario;
	private double cantidadRecibida;
	private long idProductoPrevio;
	private int estatus; // ESTATUS: 1-transito, 2-completado
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	public TraspasoDetalle() {
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

	public Long getIdAlmacenTraspaso() {
		return idAlmacenTraspaso;
	}

	public void setIdAlmacenTraspaso(Long idAlmacenTraspaso) {
		this.idAlmacenTraspaso = idAlmacenTraspaso;
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
	
	public double getCantidadRecibida() {
		return cantidadRecibida;
	}

	public void setCantidadRecibida(double cantidadRecibida) {
		this.cantidadRecibida = cantidadRecibida;
	}
	
	public long getIdProductoPrevio() {
		return idProductoPrevio;
	}
	
	public void setIdProductoPrevio(long idProductoPrevio) {
		this.idProductoPrevio = idProductoPrevio;
	}

	/**
	 * ESTATUS: 1-transito, 2-completado
	 * @return
	 */
	public int getEstatus() {
		return estatus;
	}

	/**
	 * ESTATUS: 1-transito, 2-completado
	 * @param estatus
	 */
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