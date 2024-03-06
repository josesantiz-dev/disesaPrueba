package net.giro.rh.admon.beans;

import java.io.Serializable;
import java.util.Date;

/**
 * bb72d4c5be
 * @author javaz
 *
 */
public class PuestoCategoria implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Puesto idPuesto;
	private Categoria idCategoria;
	private int estatus;
	private Long creadoPor;
	private Date fechaCreacion;
	private Long modificadoPor;
	private Date fechaModificacion;

	public PuestoCategoria() {
		this.id = 0L;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setId(long id) {	//meotodo necesatio para tomar el correlativo
		this.id = id;
	}

	public Long getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(Long creadoPor) {
		this.creadoPor = creadoPor;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Long getModificadoPor() {
		return modificadoPor;
	}

	public void setModificadoPor(Long modificadoPor) {
		this.modificadoPor = modificadoPor;
	}

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public Puesto getIdPuesto() {
		return this.idPuesto;
	}

	public void setIdPuesto(Puesto idPuesto) {
		this.idPuesto = idPuesto;
	}

	public Categoria getIdCategoria() {
		return this.idCategoria;
	}

	public void setIdCategoria(Categoria idCategoria) {
		this.idCategoria = idCategoria;
	}

	public int getEstatus() {
		return estatus;
	}

	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}
	
}