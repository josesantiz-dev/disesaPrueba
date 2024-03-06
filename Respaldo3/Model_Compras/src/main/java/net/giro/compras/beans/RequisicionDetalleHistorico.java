package net.giro.compras.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * requisicion_detalle_historico
 * @author javaz
 */
public class RequisicionDetalleHistorico implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private long idRequisicion;
	private long idProducto;
	private double cantidad; 
	private double cantidadInicial; 
	//private double cantidadCotizada;
	private double cantidadOrdenCompra;
	private double cantidadSuministrada;
	private long creadoPor;
	private Date fechaCreacion;
	
	public RequisicionDetalleHistorico() {
		this.fechaCreacion = Calendar.getInstance().getTime();
	}

	public RequisicionDetalleHistorico(RequisicionDetalle base, long creadoPor) {
		this();
		this.id = base.getId();
		this.idRequisicion = base.getIdRequisicion();
		this.idProducto = base.getIdProducto();
		this.cantidad = base.getCantidad(); 
		this.cantidadInicial = base.getCantidadInicial(); 
		//this.cantidadCotizada = base.getCantidadCotizada();
		this.cantidadOrdenCompra = base.getCantidadOrdenCompra();
		this.cantidadSuministrada = base.getCantidadSuministrada();
		this.creadoPor = creadoPor;
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

	public Long getIdRequisicion() {
		return idRequisicion;
	}

	public void setIdRequisicion(Long idRequisicion) {
		this.idRequisicion = idRequisicion;
	}

	public Long getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(Long idProducto) {
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

	/*public double getCantidadCotizada() {
		return cantidadCotizada;
	}

	public void setCantidadCotizada(double cantidadCotizada) {
		this.cantidadCotizada = cantidadCotizada;
	}*/

	public double getCantidadOrdenCompra() {
		return cantidadOrdenCompra;
	}

	public void setCantidadOrdenCompra(double cantidadOrdenCompra) {
		this.cantidadOrdenCompra = cantidadOrdenCompra;
	}

	public double getCantidadSuministrada() {
		return cantidadSuministrada;
	}

	public void setCantidadSuministrada(double cantidadSuministrada) {
		this.cantidadSuministrada = cantidadSuministrada;
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
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.2 | 2017-07-15 | Javier Tirado 	| Añado propiedades idCotizacion y cotizacionFolio;
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */