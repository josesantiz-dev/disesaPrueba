package net.giro.rh.admon.beans;

import java.io.Serializable;
import java.util.Date;

import net.giro.rh.admon.beans.Puesto;
import net.giro.rh.admon.beans.Categoria;

public class PuestoCategoriaExt implements Serializable{
	private static final long serialVersionUID = 1L;
	
	// Fields
	private Long id;
	private Long creadoPor;
	private Date fechaCreacion;
	private Long modificadoPor;
	private Date fechaModificacion;
	private Puesto idPuesto;
	private Categoria idCategoria;

	private int estatus;
	
	public PuestoCategoriaExt() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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
		return idPuesto;
	}

	public void setIdPuesto(Puesto idPuesto) {
		this.idPuesto = idPuesto;
	}

	public Categoria getIdCategoria() {
		return idCategoria;
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
