package net.giro.inventarios.beans;

import java.io.Serializable;
import java.util.Date;

public class DesgloceSBO implements Serializable {
	private static final long serialVersionUID = 1L;
	private long idSolicitud;
	private Date fecha;
	private long idAlmacen;
	private String almacen;
	private long idBodega;
	private String bodega;
	// ---------------------------------------------------------------------------------
	private long idProducto;
	private double cantidad;
	
	public DesgloceSBO(AlmacenTraspaso solicitud, long idProducto, double cantidad) {
		if (solicitud == null || idProducto <= 0L)
			return;
		this.idSolicitud = solicitud.getId();
		this.fecha = solicitud.getFecha();
		this.idAlmacen = solicitud.getIdAlmacenOrigen().getId();
		this.almacen = solicitud.getIdAlmacenOrigen().getNombre();
		this.idBodega = solicitud.getIdAlmacenDestino().getId();
		this.bodega = solicitud.getIdAlmacenDestino().getNombre();
		// ---------------------------------------------------------------------------------
		this.idProducto = idProducto;
		this.cantidad = cantidad;
	}

	public DesgloceSBO(AlmacenTraspasoExt solicitud, long idProducto, double cantidad) {
		if (solicitud == null || idProducto <= 0L)
			return;
		this.idSolicitud = solicitud.getId();
		this.fecha = solicitud.getFecha();
		this.idAlmacen = solicitud.getAlmacenOrigen().getId();
		this.almacen = solicitud.getAlmacenOrigen().getNombre();
		this.idBodega = solicitud.getAlmacenDestino().getId();
		this.bodega = solicitud.getAlmacenDestino().getNombre();
		// ---------------------------------------------------------------------------------
		this.idProducto = idProducto;
		this.cantidad = cantidad;
	}

	public long getIdSolicitud() {
		return idSolicitud;
	}

	public void setIdSolicitud(long idSolicitud) {
		this.idSolicitud = idSolicitud;
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

	public long getIdBodega() {
		return idBodega;
	}

	public void setIdBodega(long idBodega) {
		this.idBodega = idBodega;
	}

	public String getBodega() {
		return bodega;
	}

	public void setBodega(String bodega) {
		this.bodega = bodega;
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
}
