package net.giro.cxp.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;

public class GastosExt implements Serializable {
	private static final long serialVersionUID = 1L;
	private long id;
	private String descripcion;
	private long idProducto;
	private String claveProducto;
	private ConValores idTipoEgreso;
	private ConGrupoValores idGrupo;
	private boolean tieneImpuestos;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	public GastosExt() {
		this.descripcion = "";
		this.claveProducto = "";
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public long getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(long idProducto) {
		this.idProducto = idProducto;
	}

	public String getClaveProducto() {
		return claveProducto;
	}

	public void setClaveProducto(String claveProducto) {
		this.claveProducto = claveProducto;
	}

	public ConValores getIdTipoEgreso() {
		return idTipoEgreso;
	}

	public void setIdTipoEgreso(ConValores idTipoEgreso) {
		this.idTipoEgreso = idTipoEgreso;
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

	public ConGrupoValores getIdGrupo() {
		return idGrupo;
	}

	public void setIdGrupo(ConGrupoValores idGrupo) {
		this.idGrupo = idGrupo;
	}

	public boolean isTieneImpuestos() {
		return tieneImpuestos;
	}

	public void setTieneImpuestos(boolean tieneImpuestos) {
		this.tieneImpuestos = tieneImpuestos;
	}
}
