package net.giro.inventarios.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * solicitud_bodega_estatus 
 * @author javaz
 */
public class SolicitudBodegaEstatus implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private long idSolicitud;
	private long idMovimiento; // movimiento de salida
	private long idTraspaso; 
	private long idEmpresa;
	private int estatus; // 0:Pendiente, 1:Cancelado, 2:Traspaso, 3:Recibido
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	public SolicitudBodegaEstatus() {
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}

	public SolicitudBodegaEstatus(long idSolicitud, long idMovimiento, long idTraspaso, long creadoPor, long idEmpresa) {
		this.idSolicitud = idSolicitud;
		this.idMovimiento = idMovimiento;
		this.idTraspaso = idTraspaso;
		this.idEmpresa = idEmpresa;
		this.estatus = 0;
		this.creadoPor = creadoPor;
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.modificadoPor = creadoPor;
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

	public long getIdSolicitud() {
		return idSolicitud;
	}

	public void setIdSolicitud(long idSolicitud) {
		this.idSolicitud = idSolicitud;
	}

	public long getIdMovimiento() {
		return idMovimiento;
	}

	public void setIdMovimiento(long idMovimiento) {
		this.idMovimiento = idMovimiento;
	}

	public long getIdTraspaso() {
		return idTraspaso;
	}

	public void setIdTraspaso(long idTraspaso) {
		this.idTraspaso = idTraspaso;
	}

	public long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	/**
	 * 0:Pendiente, 1:Cancelado, 2:Traspaso, 3:Recibido
	 * @return
	 */
	public int getEstatus() {
		return estatus;
	}

	/**
	 * 0:Pendiente, 1:Cancelado, 2:Traspaso, 3:Recibido
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
 *  3.1 | 2019-07-16 | Javier Tirado 	| Creacion de Entity
 */
